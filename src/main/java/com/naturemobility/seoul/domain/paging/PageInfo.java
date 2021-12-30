package com.naturemobility.seoul.domain.paging;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private Criteria criteria;
    private int totalData;
    private int totalPage;
    private int firstPage;
    private int lastPage;
    private int firstIndex;
    private int lastIndex;
    private boolean hasPrePage;
    private boolean hasNextPage;

    public PageInfo(Criteria criteria){
        if (criteria.getPage()<1)
            criteria.setPage(1);
        if (criteria.getRecordsPerPage()<1 || criteria.getRecordsPerPage()>100)
            criteria.setRecordsPerPage(10);
        if (criteria.getPageSize()<5 || criteria.getPageSize()>20)
            criteria.setPageSize(10);
        this.criteria=criteria;
    }

    public void SetTotalData(int totalData){
        this.totalData=totalData;
        if (totalData>0)
            Calculation();
    }
    private void Calculation(){
        totalPage=((totalData-1)/ criteria.getRecordsPerPage())+1;
        if (criteria.getPage()>totalPage)
            criteria.setPage(totalPage);
        firstPage=((criteria.getPage())-1)/ criteria.getPageSize()+1;
        lastPage=firstPage+ criteria.getPageSize()-1;
        if (lastPage>totalPage)
            lastPage=totalPage;
        firstIndex=(criteria.getPage()-1)* criteria.getRecordsPerPage();
        lastIndex=criteria.getPage()* criteria.getRecordsPerPage();
        hasPrePage=firstPage!=1;
        hasNextPage=(lastPage* criteria.getRecordsPerPage())<totalPage;
    }
}
