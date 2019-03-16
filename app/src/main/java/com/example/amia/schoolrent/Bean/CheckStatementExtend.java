package com.example.amia.schoolrent.Bean;

public class CheckStatementExtend  extends CheckStatement{
    private int page;
    private int pageSize;
    private String userId;

    public CheckStatementExtend() {
        pageSize = 15;
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

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
