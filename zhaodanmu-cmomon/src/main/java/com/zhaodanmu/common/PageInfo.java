package com.zhaodanmu.common;

import java.util.List;

public class PageInfo<E> {

    private int total;

    private int size;

    private List<E> list;

    public PageInfo() {
    }

    public PageInfo(int total, int size, List<E> list) {
        this.total = total;
        this.size = size;
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "total=" + total +
                ", size=" + size +
                ", list len" + list.size() +
                '}';
    }
}
