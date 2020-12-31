package org.zhuyb.graphbatis.util;

import graphql.schema.DataFetchingEnvironment;
import net.sf.jsqlparser.JSQLParserException;

public interface SqlCleaner {
    /**
     * 清理SQL
     *
     * @param dataFetchingEnvironment
     * @param originSql
     * @return
     * @throws JSQLParserException
     */
    String cleanSql(DataFetchingEnvironment dataFetchingEnvironment, String originSql) throws JSQLParserException;
}
