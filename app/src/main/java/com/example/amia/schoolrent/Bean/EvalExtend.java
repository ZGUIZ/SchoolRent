package com.example.amia.schoolrent.Bean;

import java.util.Date;

public class EvalExtend extends Eval{
    private int page;
    private int pageSize;
    private String userId;
    private Date evalDate;

    public EvalExtend() {
        pageSize = 10;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart(){
        return (page-1) * pageSize;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Date getEvalDate() {
        return evalDate;
    }

    @Override
    public void setEvalDate(Date evalDate) {
        this.evalDate = evalDate;
    }
}
