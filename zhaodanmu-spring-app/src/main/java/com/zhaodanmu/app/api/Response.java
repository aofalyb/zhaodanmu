package com.zhaodanmu.app.api;

public class Response<T> {

    private T data;
    /**
     * success default
     */
    private int code = 0;

    private String descZh = "success";


    public Response(T data) {
        this.data = data;
    }

    public Response(int code, String descZh) {
        this.code = code;
        this.descZh = descZh;
    }

    public Response(T data, String descZh) {
        this.data = data;
        this.descZh = descZh;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescZh() {
        return descZh;
    }

    public void setDescZh(String descZh) {
        this.descZh = descZh;
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                ", code=" + code +
                ", descZh='" + descZh + '\'' +
                '}';
    }
}
