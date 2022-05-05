package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.payment.PaymentInfo;
import com.naturemobility.seoul.domain.payment.refund.GetRefundsRes;
import com.naturemobility.seoul.domain.payment.refund.GetRefunsResNoPaging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PaymentMapper {
    int savePayment(PaymentInfo paymentInfo);
    Optional<PaymentInfo> findPayment (String merchantUid);
    int updateRefundInfo(@Param("merchantUid")String mercantUid, @Param("refundReason")String refundReason,
                         @Param("refundHolder")String refundHolder, @Param("refundBank") String refundBank,
                         @Param("refundAccount")String refundAccount, @Param("refundStatus") int refundStaus,
                         @Param("modiBy") Long userIdx, @Param("modiRole") String modiRole);
    int updateRefundApproveInfo(@Param("merchantUid") String merchantUid,@Param("refundStatus") int resfundStaus,
                                @Param("cancelAmount")int cancelAmount, @Param("modiBy") Long userIdx,
                                @Param("modiRole") String modiRole);
    Optional<String> findMerchantUidByRefundStatus(@Param("reservationIdx") Long reservationIdx,@Param("refundStatus")int refundStatus);

    Optional<Integer> findUserIdx(@Param("merchantUid") String merchantUid);
    List<GetRefundsRes> findAllRefunds(@Param("storeIdx")Long storeIdx, @Param("refundStatus")Integer refundStatus);
    List<GetRefundsRes> findAllRequestingRefunds(GetRefundsRes getRefundsRes);
    List<GetRefundsRes> findAllApprovedRefunds(GetRefundsRes getRefundsRes);
    int cntTotalRequesting(@Param("storeIdx") Long storeIdx);
    int cntTotalApproved(@Param("storeIdx") Long storeIdx);
}
