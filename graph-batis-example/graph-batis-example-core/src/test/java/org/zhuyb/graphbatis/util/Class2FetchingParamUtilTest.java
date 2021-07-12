package org.zhuyb.graphbatis.util;

import org.junit.jupiter.api.Test;
import org.zhuyb.graphbatis.entity.Room;

/**
 * @author yunbo.zhu
 * @version 1.0
 * @date 2021/7/9 6:29 下午
 */
public class Class2FetchingParamUtilTest {
    @Test
    public void toQueryStringTest() {
        String queryString = Class2FetchingParamUtil.toQueryString(Room.class, 3, null);
        System.out.println(queryString);
        String queryString1 = Class2FetchingParamUtil.toQueryString(Room.class);
        System.out.println(queryString1);
    }
}
