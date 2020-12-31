package org.zhuyb.graphbatis.cleaner;

import org.zhuyb.graphbatis.entity.FetchingData;

public interface SqlCleaner {
    String cleanSql(FetchingData fetchingData, String originSql);
}
