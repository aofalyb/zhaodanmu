package com.zhaodanmu.persistence.api;

import com.zhaodanmu.common.PageInfo;

import java.util.List;

/**
 * 数据持久化服务
 */
public interface PersistenceService {

    void insert(Model model);

    void bufferedInsert(Object model);

    void batchInsert(boolean async,List<Object> models);

    PageInfo search(Search search);

}
