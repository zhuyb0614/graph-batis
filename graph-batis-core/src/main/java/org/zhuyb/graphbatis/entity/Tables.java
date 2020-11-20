package org.zhuyb.graphbatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;

import java.util.List;

/**
 * @author zhuyb
 * @date 2020/11/20
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Tables {
    private FromItem fromItem;
    private List<Join> joins;
}
