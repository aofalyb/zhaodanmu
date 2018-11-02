package com.zhaodanmu.app.api;


public class DouyuDanmuQuery implements Query {


    private String key;
    private String keyWord;

    public DouyuDanmuQuery(String key, String keyWord) {
        this.key = key;
        this.keyWord = keyWord;
    }

    public DouyuDanmuQuery() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String toString() {
        return "DouyuDanmuQuery{" +
                "key='" + key + '\'' +
                ", keyWord='" + keyWord + '\'' +
                '}';
    }
}
