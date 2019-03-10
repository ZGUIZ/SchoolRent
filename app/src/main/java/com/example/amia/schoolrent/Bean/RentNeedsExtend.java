package com.example.amia.schoolrent.Bean;

public class RentNeedsExtend extends RentNeeds{
    private int page;
    private int pageSize;

    public RentNeedsExtend() {
        pageSize = 6;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStart(){
        return (page-1) * pageSize;
    }

    public int getPageSize(){
        return pageSize;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
}
