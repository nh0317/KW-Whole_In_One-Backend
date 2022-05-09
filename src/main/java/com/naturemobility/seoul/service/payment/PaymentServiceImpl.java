package com.naturemobility.seoul.service.payment;


import com.naturemobility.seoul.config.BaseException;
import com.naturemobility.seoul.domain.paging.PageInfo;
import com.naturemobility.seoul.domain.payment.*;
import com.naturemobility.seoul.domain.payment.general.PostGeneralPayReq;
import com.naturemobility.seoul.domain.payment.imp.PostIMPPayReq;
import com.naturemobility.seoul.domain.payment.imp.PostIMPRefundReq;
import com.naturemobility.seoul.domain.payment.refund.*;
import com.naturemobility.seoul.domain.payment.subscription.PostPayReq;
import com.naturemobility.seoul.domain.payment.subscription.PostPayRes;
import com.naturemobility.seoul.domain.reservations.PostRezReq;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.mapper.*;
import com.naturemobility.seoul.service.reservations.ReservationsService;
import com.naturemobility.seoul.utils.IMPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    ReservationsService reservationsService;
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    IMPService impService;
    @Autowired
    PartnerMapper partnerMapper;

    @Override
    @Transactional
    public PostClientPayRes payment(PostGeneralPayReq postGeneralPayReq, Long userIdx) throws Exception {
        String storeName = storesMapper.findStoreName(postGeneralPayReq.getStoreIdx()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String name = storeName + "_골프예약_" + LocalDateTime.now();
        PostPayRes postPayRes = new PostPayRes(postGeneralPayReq.getImpUid(), postGeneralPayReq.getMerchantUid());
        PostPayReq postPayReq = new PostPayReq(postGeneralPayReq);
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
    private GetUserPoint updateUserPoint(Long userIdx, int amount, int usedPoint) throws BaseException {
        UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        int earnPoint = Double.valueOf(amount * 0.03).intValue();
        int point = earnPoint - usedPoint;
        if (userInfo.getUserPoint() - point < 0)
            point = userInfo.getUserPoint();
        usersMapper.updateUserPoint(userIdx, point);
        userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        return new GetUserPoint(point, earnPoint, userInfo.getUserPoint());
    }
    @Transactional
    @Override
    public PostClientPayRes offlinePayment(PostGeneralPayReq postGeneralPayReq, Long userIdx) throws Exception{
        String storeName = storesMapper.findStoreName(postGeneralPayReq.getStoreIdx()).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        String name = storeName + "_골프예약_" + LocalDateTime.now();

        GetUserPoint getUserPoint = updateUserPoint(userIdx, postGeneralPayReq.getAmount(), postGeneralPayReq.getPoint());
        PaymentInfo paymentInfo = new PaymentInfo("현장_결제_"+LocalDateTime.now(),postGeneralPayReq,
                userIdx,"ROLE_MEMBER", name);
        paymentInfo.setCreatedAt(null);
        PostRezReq postRezReq = new PostRezReq(postGeneralPayReq, paymentInfo.getMerchantUid());

        PostClientPayRes postClientPayRes = savePayment(paymentInfo, getUserPoint, postRezReq, userIdx);
        postClientPayRes.setStatus("결제 성공");
        return postClientPayRes;
    }

    @Transactional
    public PostClientPayRes savePayment(PaymentInfo paymentInfo,GetUserPoint getUserPoint, PostRezReq postRezReq,
                                        Long userIdx) throws BaseException {
        paymentMapper.savePayment(paymentInfo);
        reservationsService.postReservation(postRezReq, userIdx);
        usersMapper.updateUserCoupon(userIdx, paymentInfo.getCouponIdx(), Boolean.TRUE);
        return new PostClientPayRes(paymentInfo.getMerchantUid(), paymentInfo.getAmount(),
                getUserPoint.getEarnPoint(),getUserPoint.getPoint(), getUserPoint.getUserPoint());
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

        GetUserPoint getUserPoint = updateUserPoint(userIdx, postPayReq.getAmount(), postPayReq.getPoint());
        PaymentInfo paymentInfo = new PaymentInfo(postPayRes.getMerchant_uid(),
                postPayReq.getUserPaymentIdx(), postPayReq.getCouponIdx(), postPayRes.getImp_uid(),
                postPayReq.getPayMethod(), userIdx, postPayReq.getStoreIdx(), postPayReq.getAmount(), name,
                getUserPoint.getPoint(), userIdx, "ROLE_MEMBER");
        PostRezReq postRezReq = new PostRezReq(postPayReq, postPayRes.getMerchant_uid());
        PostClientPayRes postClientPayRes;
        switch (status) {
            case "ready":
                postClientPayRes = savePayment(paymentInfo,getUserPoint,postRezReq, userIdx);
                postClientPayRes.setStatus("가상계좌 발급 성공(미결제)");
                log.info("가상계좌 발급 성공(미결제)");
                return postClientPayRes;
            case "paid":
                postClientPayRes = savePayment(paymentInfo,getUserPoint,postRezReq, userIdx);
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
    @Transactional
    public PostReqRefundRes requestRefund(PostReqRefundReq postRefundReq, Long userIdx) throws BaseException {
        String merchantUid = paymentMapper.findMerchantUidByRefundStatus(postRefundReq.getReservationIdx(),RefundStatus.NOT_REFUND.getValue())
                .orElseGet(() -> null);
        paymentMapper.updateRefundInfo(merchantUid,postRefundReq.getRefundReason(), postRefundReq.getRefundHolder(),
                postRefundReq.getRefundBank(),postRefundReq.getRefundAccount(),RefundStatus.REQUEST_REFUND.getValue(),
                userIdx, "ROLE_MEMBER");
        reservationMapper.updateAlreadyUsed(true, postRefundReq.getReservationIdx());
        return new PostReqRefundRes(postRefundReq.getReservationIdx(), postRefundReq.getRefundReason(),
                postRefundReq.getRefundHolder(), postRefundReq.getRefundBank(), postRefundReq.getRefundAccount(),
                RefundStatus.REQUEST_REFUND.getMsg());
    }

    @Transactional
    @Override
    public PostApproveRefundRes approveRefund(PostApproveRefundReq postApproveRefund, Long partnerIdx) throws BaseException{
        //환불 가능 여부 검토
        PaymentInfo paymentInfo = new PaymentInfo();
        try {
            paymentInfo = validateRefund(postApproveRefund);
        }
        catch (BaseException e){
            e.printStackTrace();
            if (e.getStatus()==NOT_PAID) {
                String merchantUid = paymentMapper.findMerchantUidByRefundStatus(postApproveRefund.getReservationIdx(),
                        RefundStatus.REQUEST_REFUND.getValue()).orElseThrow(() -> new BaseException(NOT_REQUESTED_REFUND));
                paymentInfo = paymentMapper.findPayment(merchantUid).orElseThrow(() -> new BaseException(NOT_REQUESTED_REFUND));
                return saveRefund(paymentInfo, postApproveRefund, partnerIdx);
            }
            else throw e;
        }
        int status = requestRefund(paymentInfo,postApproveRefund);
        if (status==0) {
            return saveRefund(paymentInfo,postApproveRefund, partnerIdx);
        } else
            throw new BaseException(RESPONSE_ERROR);
    }

    public PaymentInfo validateRefund(PostApproveRefundReq postApproveRefund) throws BaseException {
        String merchantUid = paymentMapper.findMerchantUidByRefundStatus(postApproveRefund.getReservationIdx(),
                RefundStatus.REQUEST_REFUND.getValue()).orElseThrow(() -> new BaseException(NOT_REQUESTED_REFUND));
        PaymentInfo paymentInfo = paymentMapper.findPayment(merchantUid).orElseThrow(() -> new BaseException(NOT_REQUESTED_REFUND));
        log.info(paymentInfo.toString());
        if (!paymentInfo.getRefundStatus().equals(RefundStatus.REQUEST_REFUND.getValue()))
            throw new BaseException(NOT_REQUESTED_REFUND);
        if (paymentInfo.getAmount() == 0)
            throw new BaseException(NOT_PAID);
        int cancelableAmount = paymentInfo.getAmount() - paymentInfo.getCancelAmount();
        if (cancelableAmount <= 0)
            throw new BaseException(ALREADY_REFUND);
        return paymentInfo;
    }

    public int requestRefund(PaymentInfo paymentInfo,PostApproveRefundReq postApproveRefund) throws BaseException {
        try{
        int cancelableAmount = paymentInfo.getAmount() - paymentInfo.getCancelAmount();
        String url = "https://api.iamport.kr/payments/cancel";
        PostIMPRefundReq postIMPRefundReq =
                new PostIMPRefundReq(paymentInfo.getRefundReason(),paymentInfo.getImpUid(), postApproveRefund.getCancelAmount(),
                        cancelableAmount,paymentInfo.getRefundHolder(),paymentInfo.getRefundBank(),paymentInfo.getRefundAccount());
        Map<String, Object> response = getResponse(impService.getIMPToken(), HttpMethod.POST, postIMPRefundReq, url);
        int status = ((Double)response.get("code")).intValue();
        log.info(response.toString());
        return status;}
        catch (Exception e){
            e.printStackTrace();
            throw e;

        }
    }

    @Transactional
    public PostApproveRefundRes saveRefund(PaymentInfo paymentInfo,PostApproveRefundReq postApproveRefund, Long partnerIdx)
            throws BaseException {
        usersMapper.updateUserPoint(paymentInfo.getUserIdx(), -paymentInfo.getPoint());
        paymentMapper.updateRefundApproveInfo(paymentInfo.getMerchantUid(), RefundStatus.COMPLETE.getValue(),
                postApproveRefund.getCancelAmount(), partnerIdx, "ROLE_ADMIN");
        paymentInfo = paymentMapper.findPayment(paymentInfo.getMerchantUid()).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        UserInfo userInfo = usersMapper.findByIdx(paymentInfo.getUserIdx()).orElseThrow(()->new BaseException(NOT_FOUND_DATA));
        usersMapper.updateUserCoupon(paymentInfo.getUserIdx(), paymentInfo.getCouponIdx(), Boolean.FALSE);
        reservationMapper.updateAlreadyUsed(true, paymentInfo.getReservationIdx());
        return new PostApproveRefundRes(postApproveRefund.getReservationIdx(), paymentInfo.getRefundReason(),
                paymentInfo.getCancelAmount(),userInfo.getUserPoint(), RefundStatus.COMPLETE.getMsg());

    }

    @Override
    public GetUserInfoInPayment getUserInfo(Long userIdx) throws BaseException{
        UserInfo userInfo = usersMapper.findByIdx(userIdx).orElseThrow(() -> new BaseException(NOT_FOUND_DATA));
        GetUserInfoInPayment getUserInfoInPayment = new GetUserInfoInPayment(userInfo.getUserPhoneNumber(), userInfo.getUserName(), userInfo.getUserEmail(), userInfo.getUserPoint());
        getUserInfoInPayment.setUserCoupon(usersMapper.cntCoupon(userIdx).orElseGet(()->0));
        getUserInfoInPayment.setUserCoupons(usersMapper.getUserCouponsList(userIdx).stream().filter((items)->items.getCouponStatus()==0).collect(Collectors.toList()));
        return getUserInfoInPayment;
    }


    @Override
    public List<GetRefundsRes> getRefundsList(Long partnerIdx, String status) throws BaseException{

        Long store = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA));
        List<GetRefundsRes> allRefunds = new ArrayList<>();
        if (status.equals("Requesting"))
                allRefunds.addAll(paymentMapper.findAllRefunds(store,1));
        else if (status.equals("Approved"))
            allRefunds.addAll(paymentMapper.findAllRefunds(store,2));
        return allRefunds;
    }

    @Override
    public GetPagingRefunds getPagingRefundsList(Long partnerIdx, Integer page, String status) throws BaseException{

        Long store = partnerMapper.findStoreIdx(partnerIdx).orElseThrow(()-> new BaseException(NOT_FOUND_DATA));
        GetPagingRefunds getPagingRefunds = new GetPagingRefunds();
        GetRefundsRes getRefundsRes = new GetRefundsRes(store);
        int total = 0;
        if (status.equals("Requesting"))
            total=paymentMapper.cntTotalRequesting(store);
        else if(status.equals("Approved"))
            total = paymentMapper.cntTotalApproved(store);
        else return getPagingRefunds;
        if (total == 0){
            getPagingRefunds.setRefunds(new ArrayList<>());
            getPagingRefunds.setTotalPage(0);
            getPagingRefunds.setItemsPerPage(getRefundsRes.getRecordsPerPage());
        }
        if (page==null) {
            if (status.equals("Requesting"))
                getPagingRefunds.setRefunds(paymentMapper.findAllRefunds(store,1));
            else if (status.equals("Approved"))
                getPagingRefunds.setRefunds(paymentMapper.findAllRefunds(store,2));
        }else{
            getPagingRefunds.setRefunds(getRefundsList(page, status, getRefundsRes, total));
            getPagingRefunds.setTotalPage(getRefundsRes.getPageInfo().getTotalPage());
            getPagingRefunds.setItemsPerPage(getRefundsRes.getRecordsPerPage());
        }
        return getPagingRefunds;
    }

    private List<GetRefundsRes> getRefundsList(Integer page, String status, GetRefundsRes getRefundsRes, Integer total) throws BaseException {
        if (page == null){
            if(status.equals("Requesting"))
                return paymentMapper.findAllRefunds(getRefundsRes.getStoreIdx(),1);
            else if(status.equals("Approved"))
                return  paymentMapper.findAllRefunds(getRefundsRes.getStoreIdx(),2);
            else return new ArrayList<>();
        }
        else if (page != null && total != null && total > 0) {
            if (page != null && page >= 1) {
                getRefundsRes.setPage(page);
            }
            PageInfo pageInfo = new PageInfo(getRefundsRes);
            pageInfo.SetTotalData(total);
            getRefundsRes.setPageInfo(pageInfo);

            if (page > getRefundsRes.getPageInfo().getTotalPage()) {
                return new ArrayList<>();
            }
            if(status.equals("Requesting"))
                return paymentMapper.findAllRequestingRefunds(getRefundsRes);
            else if(status.equals("Approved"))
                return  paymentMapper.findAllApprovedRefunds(getRefundsRes);
            else return new ArrayList<>();
        }
        else if (page!=null && page > total)
            return new ArrayList<>();
        else return new ArrayList<>();
    }
}
