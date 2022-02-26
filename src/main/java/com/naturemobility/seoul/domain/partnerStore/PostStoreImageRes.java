package com.naturemobility.seoul.domain.partnerStore;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostStoreImageRes {
    private String mainStoreImage;
    private List<String> storeImages;
}
