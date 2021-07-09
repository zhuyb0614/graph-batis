package org.zhuyb.graphbatis.dq;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
public interface DynamicQueryDao<T> {
    List<T> findAll(Map<String, Object> arguments);
}
