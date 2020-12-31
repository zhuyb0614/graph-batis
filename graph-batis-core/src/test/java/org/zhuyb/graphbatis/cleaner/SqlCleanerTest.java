package org.zhuyb.graphbatis.cleaner;

import org.junit.Test;

/**
 * @author zhuyb
 * @date 2020/12/31
 */
public class SqlCleanerTest {

    @Test
    public void cleanSql() {
        SqlCleaner sqlCleaner = new MybatisSqlCleanerImpl();
    }
}