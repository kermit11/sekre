package com.kermit11.sekre.controller;

public class PaginationInfo
{
    private int pageStart;
    private int pageSize;
    private int totalSize;

    public PaginationInfo(int pageStart, int pageSize, int totalSize)
    {
        this.pageStart = pageStart;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public int getPageStart() {
        return pageStart;
    }
    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
