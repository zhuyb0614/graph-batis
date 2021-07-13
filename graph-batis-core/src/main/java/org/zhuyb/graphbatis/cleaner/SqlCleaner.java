package org.zhuyb.graphbatis.cleaner;

import org.zhuyb.graphbatis.entity.FetchingData;

import java.util.Properties;

/**
 * SQL清理器
 *
 * @author zhuyunbo
 */
public interface SqlCleaner {
    /**
     * 清理不需要的表
     *
     * @param fetchingData 查询数据
     * @param originSql    原始SQL
     * @return 根据查询数据, 过滤后的SQL
     */
    String cleanSql(FetchingData fetchingData, String originSql);

    /**
     * 通过mybatis-config中的配置文件初始化
     * 如
     * <plugin interceptor="org.zhuyb.graphbatis.interceptor.MybatisCleanSqlInterceptor">
     * <property name="dialect" value="mysql"/>
     * <property name="maxLoopDeep" value="1"/>
     * <property name="openCache" value="false"/>
     * <property name="cleaner" value="org.zhuyb.graphbatis.cleaner.MybatisSqlCleanerImpl"/>
     * </plugin>
     *
     * @param properties property标签配置的变量
     */
    void setUp(Properties properties);
}
