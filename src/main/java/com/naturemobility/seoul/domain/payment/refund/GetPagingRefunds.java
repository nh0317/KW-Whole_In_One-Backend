package com.naturemobility.seoul.domain.payment.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GetPagingRefunds {
    int totalPage;
    int itemsPerPage;
    List<GetRefundsRes> refunds;

    public GetPagingRefunds() {
        this.refunds = new ArrayList<>();
    }
}
