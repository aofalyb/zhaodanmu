package com.zhaodanmu.persistence.api;

import java.util.List;

/**
 * 数据持久化服务
 */
public interface PersistenceService {

    void init(Object...args);

    boolean isStart();

    void insert(Model model);

    void bufferedInsert(Model model);

    void batchInsert(boolean async,List<Model> models);

    String search(Search search);

    void shutdown(Object...args);
}
