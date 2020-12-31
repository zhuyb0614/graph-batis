package org.zhuyb.graphbatis;

import lombok.experimental.UtilityClass;
import org.zhuyb.graphbatis.entity.FetchingData;

@UtilityClass
public class FetchingDataHolder {
    private static final ThreadLocal<FetchingData> FETCHING_DATA_THREAD_LOCAL = new ThreadLocal<>();

    public static void put(FetchingData fetchingData) {
        FETCHING_DATA_THREAD_LOCAL.set(fetchingData);
    }

    public static FetchingData get() {
        return FETCHING_DATA_THREAD_LOCAL.get();
    }

    public static void remove() {
        FETCHING_DATA_THREAD_LOCAL.remove();
    }
}
