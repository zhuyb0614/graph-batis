package org.zhuyb.graphbatis;

import graphql.schema.DataFetchingEnvironment;
import lombok.experimental.UtilityClass;

/**
 * @author zhuyb
 * @date 2020/5/1
 */
@UtilityClass
public class DataFetchingEnvHolder {
    private static final ThreadLocal<DataFetchingEnvironment> ENVIRONMENT_THREAD_LOCAL = new ThreadLocal<>();

    public static void put(DataFetchingEnvironment env) {
        ENVIRONMENT_THREAD_LOCAL.set(env);
    }

    public static DataFetchingEnvironment get() {
        return ENVIRONMENT_THREAD_LOCAL.get();
    }

    public static void remove() {
        ENVIRONMENT_THREAD_LOCAL.remove();
    }
}
