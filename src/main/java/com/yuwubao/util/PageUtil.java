package com.yuwubao.util;

/**
 * Created by yangyu on 2018/1/16.
 */
public class PageUtil {

    private int totalPages; // 总页数
    private int index;  //第几页
    private int size;  //每页几条
    private int totalElements; //总共多少条数据

    public int getTotalPages() {
        return totalPages;
    }

    // 计算总页数
    public void setTotalPages(int numPerPage,int totalRows) {
        if (totalRows % numPerPage == 0) {
            this.totalPages = totalRows / numPerPage;
        } else {
            this.totalPages = (totalRows / numPerPage) + 1;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
}
