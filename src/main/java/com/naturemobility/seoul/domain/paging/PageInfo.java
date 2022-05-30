package com.naturemobility.seoul.domain.paging;

import com.naturemobility.seoul.domain.DTOCommon;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Optional;


@Getter
@Setter
@Slf4j
public class PageInfo {

    private final int RECODES_PER_PAGE = 10;
    private final int PAGE_SIZE = 10;

    private int totalData;
    private int totalPage;
    private int firstIndex;
    private int lastIndex;
    private int firstPage;
    private int lastPage;
    private boolean hasNext;
    private boolean hasPrev;
    private int page;
    private int recordsPerPage; //한 번에 불러오는 목록의 수
    private int pageSize; // 웹 페이지 하단에 표시되는 페이지 수

    public PageInfo(int page, int total) {
        this.recordsPerPage=RECODES_PER_PAGE;
        this.pageSize = PAGE_SIZE;

        this.page=page;
        this.totalData = total;
        if (totalData>0)
            calculation();
    }

    public PageInfo() {
        this.page = 1;
        this.recordsPerPage=RECODES_PER_PAGE;
        this.pageSize = PAGE_SIZE;
    }

    public void setTotalPage(int totalData){
        this.totalData=totalData;
        if (totalData>0)
            calculation();
    }
    public void setPage(Integer page){
        if (page != null && page >= 1)
            this.page = page;
    }
    private void calculation(){
        totalPage=((totalData-1)/ recordsPerPage)+1;
        firstIndex=(page-1)* recordsPerPage;
        lastIndex=page* recordsPerPage;
        firstPage=((page-1)/ pageSize+1);
        lastPage=firstPage+ pageSize-1;
        if (lastPage>totalPage)
            lastPage=totalPage;
        hasPrev=(firstPage!=1);
        hasNext=(lastPage* recordsPerPage)<totalPage;
    }

    public PageInfo getPageInfo(Integer page, int total){
        if (total == 0)
            return null;

        if (total > 0) {
            if (page != null && page >= 1) {
                this.page = page;
            }
            this.totalData = total;
            calculation();

            if (page != null && page > this.totalPage) {
                return null;
            }

            return this;
        } else if (page > total)
            return null;
        else return null;
    }
}
