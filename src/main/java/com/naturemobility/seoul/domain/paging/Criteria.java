package com.naturemobility.seoul.domain.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Getter
@Setter
public class Criteria {
    private int page;
    private int recordsPerPage; //한 번에 불러오는 목록의 수
    private int pageSize; // 웹 페이지 하단에 표시되는 페이지 수
    private String searchKeyword;
    private String searchType;

    public Criteria() {
        this.page=1;
        this.recordsPerPage=10;
        this.pageSize=10;
    }
    public int getStartPage(){
        return (page-1)*recordsPerPage;
    }
    public String makeQueryString(int pageNo){
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParam("Page", pageNo)
                .queryParam("recordsPerPage", recordsPerPage)
                .queryParam("searchType", searchType)
                .queryParam("text", searchKeyword)
                .build()
                .encode();
        return uriComponents.toUriString();
    }
}
