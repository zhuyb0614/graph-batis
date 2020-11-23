package org.zhuyb.graphbatis.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author zhuyb
 * @see CachingExecutor#query(MappedStatement, Object, RowBounds, ResultHandler)
 * 查看4参的query发现,他调用6参的query是内部this调用的.
 * 所以6参query没办法被Interceptor拦截.
 * 这里将6参query改为在这执行,其他6参interceptor就可用了
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        }
)
public class FourArgsInterceptor implements Interceptor {

    public static final Logger logger = LoggerFactory.getLogger(FourArgsInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        CacheKey cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
        return executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logger.info("properties {}", properties);
    }

}
