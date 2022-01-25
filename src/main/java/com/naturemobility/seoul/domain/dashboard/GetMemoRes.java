package com.naturemobility.seoul.domain.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetMemoRes {
    final Long memoIdx;
    final String memo;
    final String createdAt;
    final String updatedAt;
}
