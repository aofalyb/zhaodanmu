package com.zhaodanmu.persistence.api;

/**
 * Created by Administrator on 2018/10/31.
 */
public interface Search {

    int from();

    String getType();

    String getIndex();

    String getKey();

    String getKeyWord();
}
