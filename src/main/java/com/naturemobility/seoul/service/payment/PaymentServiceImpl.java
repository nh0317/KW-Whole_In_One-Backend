package com.naturemobility.seoul.service.payment;


import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.payment.*;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import com.naturemobility.seoul.domain.payment.imp.PostIMPPayReq;
import com.naturemobility.seoul.domain.payment.imp.PostIMPRefundReq;
import com.naturemobility.seoul.domain.payment.refund.PostApproveRefundReq;
import com.naturemobility.seoul.domain.payment.refund.PostApproveRefundRes;
import com.naturemobility.seoul.domain.payment.refund.PostReqRefundReq;
import com.naturemobility.seoul.domain.payment.refund.PostReqRefundRes;
import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import com.naturemobility.seoul.domain.payment.subscription.PostPayRes;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.PaymentMapper;
import com.naturemobility.seoul.mapper.StoresMapper;
import com.naturemobility.seoul.mapper.UsersMapper;
import com.naturemobility.seoul.utils.IMPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import static com.naturemobility.seoul.config.BaseResponseStatus.*;
import static com.naturemobility.seoul.utils.ExternalAPI.getResponse;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    StoresMapper storesMapper;
    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    IMPService impService;

    @Override
    @Transactional
    public PostClientPayRes payment(PostGeneralPayReq postGeneralPayReq, Long userIdx) throws Exception {
        String storeName = storesMapper.findStoreName(postGeneralPayReq.getStoreIdx()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String name = storeName + "_골프예약_" + LocalDateTime.now();
        PostPayRes postPayRes = new PostPayRes(postGeneralPayReq.getImpUid(), postGeneralPayReq.getMerchantUid());
        PostPayReq postPayReq = new PostPayReq(postGeneralPayReq.getReservationIdx(),
                postGeneralPayReq.getStoreIdx(), postGeneralPayReq.getCouponIdx(), null,
                postGeneralPayReq.getPayMethod(), postGeneralPayReq.getAmount(), postGeneralPayReq.getPoint());
        return validateAndSave(userIdx, name, postPayRes, postPayReq);
    }


    @Override
    @Transactional
    public PostClientPayRes billingKeyPayment(PostPayReq postPayReq, Long userIdx) throws Exception {
        String storeName = storesMapper.findStoreName(postPayReq.getStoreIdx())
                .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String name = storeName + "_골프예약_" + LocalDateTime.now();
        PostPayRes getPaymentData = requestBillingKeyPay(postPayReq, name);

        return validateAndSave(userIdx, name, getPaymentData, postPayReq);
    }
    @Transactional
    public PostClientPayRes savePayment(PostPayRes getPaymentData, PostPayReq postPayReq,
                                        Long userIdx, String name) throws BaseException {
        try{
            UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
            int earnPoint = Double.valueOf(postPayReq.getAmount() * 0.03).intValue();
            int point = earnPoint - postPayReq.getPoint();
            if (userInfo.getUserPoint() - point < 0)
                point = userInfo.getUserPoint();

            paymentMapper.savePayment(new PaymentInfo(getPaymentData.getMerchant_uid(), postPayReq.getReservationIdx(),
                    postPayReq.getUserPaymentIdx(), postPayReq.getCouponIdx(), getPaymentData.getImp_uid(),
                    postPayReq.getPayMethod(), userIdx, postPayReq.getStoreIdx(), postPayReq.getAmount(), name,
                    point, userIdx, "ROLE_MEMBER"));
            usersMapper.updateUserPoint(userIdx, point);
            return new PostClientPayRes(getPaymentData.getMerchant_uid(), postPayReq.getAmount(),
                    earnPoint,postPayReq.getPoint(), userInfo.getUserPoint());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public String validatePayment(PostPayRes getPaymentData, PostPayReq postPayReq) throws BaseException {
        String url = ("https://api.iamport.kr/payments/"+getPaymentData.getImp_uid());
        Map<String, Object> result = getResponse(impService.getIMPToken(), HttpMethod.POST,null, url);
        Double amount = (Double) ((Map)result.get("response")).get("amount");
        String status = ((Map)result.get("response")).get("status").toString();
        if (Objects.equals(postPayReq.getAmount(), amount.intValue())){
            return status;
        }
        else throw new BaseException(RESPONSE_ERROR);
    }

    @Transactional
    public PostPayRes requestBillingKeyPay(PostPayReq postPayReq, String name) throws Exception{
        String customerUid =impService.decryptBillingKey(postPayReq.getUserPaymentIdx());

        String url = "https://api.iamport.kr/subscribe/payments/again";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        PostIMPPayReq postIMPPayReq = new PostIMPPayReq(customerUid, "order_" + now.format(formatter),
                postPayReq.getAmount(), name);
        Map<String, Object> response = getResponse(impService.getIMPToken(),HttpMethod.POST, postIMPPayReq,url);
        return new PostPayRes(((Map)response.get("response")).get("imp_uid").toString(),
                ((Map)response.get("response")).get("merchant_uid").toString());
    }

    @Transactional
    public PostClientPayRes validateAndSave(Long userIdx, String name, PostPayRes postPayRes, PostPayReq postPayReq) throws BaseException {
        String status = validatePayment(postPayRes, postPayReq);
        PostClientPayRes postClientPayRes;
        switch (status) {
            case "ready":
                postClientPayRes = savePayment(postPayRes, postPayReq, userIdx, name);
                postClientPayRes.setStatus("가상계좌 발급 성공(미결제)");
                log.info("가상계좌 발급 성공(미결제)");
                return postClientPayRes;
            case "paid":
                postClientPayRes = savePayment(postPayRes, postPayReq, userIdx, name);
                postClientPayRes.setStatus("결제 성공");
                log.info("결제 성공");
                return postClientPayRes;
            case "cancelled":
                log.info("결제 취소");
                throw new BaseException(PAYMENT_CANCEL);
            case "failed":
                log.info("결제 실패");
                throw new BaseException(PAYMENT_FAILED);
            default:
                throw new BaseException(RESPONSE_ERROR);
        }
    }

    @Override
    public PostReqRefundRes requestRefund(PostReqRefundReq postRefundReq, Long userIdx) throws BaseException {
        String merchantUid = paymentMapper.findMerchantUidByRefundStatus(postRefundReq.getReservationIdx(),RefundStatus.NOT_REFUND.getValue())
                .orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        paymentMapper.updateRefundInfo(merchantUid,postRefundReq.getRefundReason(), postRefundReq.getRefundHolder(),
                postRefundReq.getRefundBank(),postRefundReq.getRefundAccount(),RefundStatus.REQUEST_REFUND.getValue(),
                userIdx, "ROLE_MEMBER");
        return new PostReqRefundRes(postRefundReq.getReservationIdx(), postRefundReq.getRefundReason(),
                postRefundReq.getRefundHolder(), postRefundReq.getRefundBank(), postRefundReq.getRefundAccount(),
                RefundStatus.REQUEST_REFUND.getMsg());
    }

    @Transactional
    @Override
    public PostApproveRefundRes approveRefund(PostApproveRefundReq postApproveRefund, Long partnerIdx) throws BaseException{
        //환불 가능 여부 검토
        PaymentInfo paymentInfo = validateRefund(postApproveRefund);
        int status = requestRefund(paymentInfo,postApproveRefund);
        if (status==0) {
            return saveRefund(paymentInfo,postApproveRefund, partnerIdx);
        } else
            throw new BaseException(RESPONSE_ERROR);
    }

    public PaymentInfo validateRefund(PostApproveRefundReq postApproveRefund) throws BaseException {
        String merchantUid = paymentMapper.findMerchantUidByRefundStatus(postApproveRefund.getReservationIdx(),
                RefundStatus.REQUEST_REFUND.getValue()).orElseThrow(() -> new BaseException(NOT_REQUESTED_REFUND));
        PaymentInfo paymentInfo = paymentMapper.findPayment(merchantUid).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        if (!paymentInfo.getRefundStatus().equals(RefundStatus.REQUEST_REFUND))
            throw new BaseException(NOT_REQUESTED_REFUND);
        int cancelableAmount = paymentInfo.getAmount() - paymentInfo.getCancelAmount();
        if (cancelableAmount <= 0)
            throw new BaseException(ALREADY_REFUND);
        return paymentInfo;
    }

    public int requestRefund(PaymentInfo paymentInfo,PostApproveRefundReq postApproveRefund) throws BaseException {
        int cancelableAmount = paymentInfo.getAmount() - paymentInfo.getCancelAmount();
        String url = "https://api.iamport.kr/payments/cancel";
        PostIMPRefundReq postIMPRefundReq =
                new PostIMPRefundReq(paymentInfo.getRefundReason(),paymentInfo.getImpUid(), postApproveRefund.getCancelAmount(),
                        cancelableAmount,paymentInfo.getRefundHolder(),paymentInfo.getRefundBank(),paymentInfo.getRefundAccount());
        Map<String, Object> response = getResponse(impService.getIMPToken(), HttpMethod.POST, postIMPRefundReq, url);
        int status = ((Double)response.get("code")).intValue();
        return status;
    }

    public PostApproveRefundRes saveRefund(PaymentInfo paymentInfo,PostApproveRefundReq postApproveRefund, Long partnerIdx)
            throws BaseException {
        usersMapper.updateUserPoint(paymentInfo.getUserIdx(), -paymentInfo.getPoint());
        paymentMapper.updateRefundApproveInfo(paymentInfo.getMerchantUid(), RefundStatus.COMPLETE.getValue(),
                postApproveRefund.getCancelAmount(), partnerIdx, "ROLE_ADMIN");
        paymentInfo = paymentMapper.findPayment(paymentInfo.getMerchantUid()).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        UserInfo userInfo = usersMapper.findByIdx(paymentInfo.getUserIdx()).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        return new PostApproveRefundRes(postApproveRefund.getReservationIdx(), paymentInfo.getRefundReason(),
                paymentInfo.getCancelAmount(),userInfo.getUserPoint(), RefundStatus.COMPLETE.getMsg());

    }
}
