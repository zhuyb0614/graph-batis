package org.zhuyb.graphbatis.entity;

import lombok.Data;

import java.util.List;

/**
 * @author yunbo.zhu
 * @version 1.0
 * @date 2021/7/9 6:05 下午
 */
@Data
public class FetchingField {
    private String filedName;
    private boolean primitive;
    private List<FetchingField> subParams;
}
