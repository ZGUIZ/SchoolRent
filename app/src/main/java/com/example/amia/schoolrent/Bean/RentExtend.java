package com.example.amia.schoolrent.Bean;

public class RentExtend extends Rent{
    private int page;
    private int pageSize;
    private int status;

    public RentExtend() {
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

    @Override
    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
