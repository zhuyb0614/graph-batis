package org.zhuyb.graphbatis.interceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * @author zhuyb
 * @date 2020/4/25
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class SqlInterceptor implements Interceptor {

    public static final Logger logger = LoggerFactory.getLogger(SqlInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result;
        Object[] args = invocation.getArgs();
        if (!(args[0] instanceof MappedStatement) || !SqlCommandType.SELECT.equals(((MappedStatement) args[0]).getSqlCommandType())) {
            result = invocation.proceed();
        } else {
            Invocation changedInvocation = new Invocation(invocation.getTarget(), invocation.getMethod(), args);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof BoundSql) {
                    String sql = ((BoundSql) arg).getSql();
                    logger.info("origin sql {}", sql);
                    List<ParameterMapping> parameterMappings = ((BoundSql) arg).getParameterMappings();
                    logger.info("parameters {}", parameterMappings);
                    Select selectSql = (Select) CCJSqlParserUtil.parse(sql);
                    PlainSelect selectBody = (PlainSelect) selectSql.getSelectBody();
                    List<SelectItem> selectItems = selectBody.getSelectItems();
                    Expression where = selectBody.getWhere();
                    List<Join> joins = selectBody.getJoins();
                    logger.info("selectSql {}", selectSql);
                }
            }
            result = changedInvocation.proceed();
        }
        return result;
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
