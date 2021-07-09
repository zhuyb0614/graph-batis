package org.zhuyb.graphbatis.aspect;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.FetchingDataHolder;
import org.zhuyb.graphbatis.entity.FetchingData;
import org.zhuyb.graphbatis.util.GraphQLTransformUtil;

/**
 * @author zhuyb
 * @date 2020/12/20
 */
@Slf4j
@Aspect
@Component
public class DataFetchingEnvSetUpAspect {

    @Pointcut("within(org.zhuyb.graphbatis.resolver..*)")
    public void optionalDataFetchPoint() {
    }

    @Before(value = "optionalDataFetchPoint()")
    public void envSetUp(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof DataFetchingEnvironment) {
                    FetchingData fetchingData = new FetchingData();
                    DataFetchingEnvironment dataFetchingEnvironment = (DataFetchingEnvironment) arg;
                    fetchingData.setFieldNames(GraphQLTransformUtil.getAllGraphQLFieldNames(dataFetchingEnvironment));
                    fetchingData.setArguments(dataFetchingEnvironment.getArguments());
                    FetchingDataHolder.put(fetchingData);
                }
            }
        }
    }

    @After(value = "optionalDataFetchPoint()")
    public void envRemove(JoinPoint joinPoint) throws Throwable {
        FetchingDataHolder.remove();
    }
}
