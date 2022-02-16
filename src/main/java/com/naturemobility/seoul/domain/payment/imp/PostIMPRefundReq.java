package com.naturemobility.seoul.domain.payment.imp;

import io.micrometer.core.instrument.util.StringEscapeUtils;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class PostIMPRefundReq {
    private final String reason;
    private final String imp_uid;
    private final int amount;
    private final int checksum;
    private String refund_holder;
    private String refund_bank;
    private String refund_account;

    public PostIMPRefundReq(String reason, String imp_uid, int amount, int checksum, String refund_holder, String refund_bank, String refund_account) {
        this.reason = reason;
        this.imp_uid = imp_uid;
        this.amount = amount;
        this.checksum = checksum;
        if(!StringUtils.isEmpty(refund_account)) {
            this.refund_holder = refund_holder;
            this.refund_bank = refund_bank;
            this.refund_account = refund_account;
        }
    }
}
