package org.zhuyb.graphbatis.util;

import org.junit.Test;

/**
 * @author zhuyb
 * @date 2020/12/31
 */
public class SqlCleanerTest {

    @Test
    public void cleanSql() {
        SqlCleaner sqlCleaner = new SqlCleanerImpl(1, 4096);
    }
}