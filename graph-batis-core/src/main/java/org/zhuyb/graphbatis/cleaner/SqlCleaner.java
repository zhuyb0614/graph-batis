package org.zhuyb.graphbatis.cleaner;

import org.zhuyb.graphbatis.entity.FetchingData;

import java.util.Properties;

public interface SqlCleaner {
    String cleanSql(FetchingData fetchingData, String originSql);

    void setUp(Properties properties);
}
