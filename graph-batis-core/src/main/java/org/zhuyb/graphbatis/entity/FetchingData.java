package org.zhuyb.graphbatis.entity;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class FetchingData {
    private Set<String> fieldNames;
    private Map<String, Object> arguments;
}
