package org.zhuyb.graphbatis.interceptor;

import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhuyb.graphbatis.DataFetchingEnvHolder;
import org.zhuyb.graphbatis.util.SqlCleaner;
import org.zhuyb.graphbatis.util.SqlCleanerImpl;

import java.util.Properties;

/**
 * @author zhuyb
 * 如果没有
 * @see FourArgsInterceptor
 * 该interceptor无法生效
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class CleanSqlInterceptor implements Interceptor {

    public static final Logger logger = LoggerFactory.getLogger(CleanSqlInterceptor.class);
    public static final int BOUND_SQL_INDEX = 5;
    public static final int MAPPED_STATEMENT_INDEX = 0;
    private SqlCleaner sqlCleaner;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result;
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            result = invocation.proceed();
        } else {
            long startTime = System.currentTimeMillis();
            DataFetchingEnvironment dataFetchingEnvironment = DataFetchingEnvHolder.get();
            if (dataFetchingEnvironment != null) {
                Invocation changedInvocation = new Invocation(invocation.getTarget(), invocation.getMethod(), args);
                BoundSql originBoundSql = (BoundSql) args[BOUND_SQL_INDEX];
                String originSql = originBoundSql.getSql();
                String cleanSql = sqlCleaner.cleanSql(dataFetchingEnvironment, originSql);
                BoundSql cleanBoundSql = new BoundSql(mappedStatement.getConfiguration(), cleanSql, originBoundSql.getParameterMappings(), originBoundSql.getParameterObject());
                args[BOUND_SQL_INDEX] = cleanBoundSql;
                logger.debug("clean sql cost {}ms", System.currentTimeMillis() - startTime);
                result = changedInvocation.proceed();
            } else {
                result = invocation.proceed();
            }
        }
        return result;
    }



    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String maxLoopDeepProp = properties.getProperty("maxLoopDeep");
        if (StringUtils.isNumeric(maxLoopDeepProp)) {
            sqlCleaner = new SqlCleanerImpl(Integer.valueOf(maxLoopDeepProp), 4096);
        }
        logger.info("properties {}", properties);
    }

}
