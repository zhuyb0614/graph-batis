package org.zhuyb.graphbatis.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Condition;
import org.jooq.JoinType;
import org.jooq.TableLike;

/**
 * @author zhuyb
 */
@Data
@NoArgsConstructor
public class TableJoinCondition {
    private TableLike<?> table;
    private Condition condition;
    private TableLike<?> relationTable;
    private JoinType joinType;

    public TableJoinCondition(TableLike<?> table, Condition condition, TableLike<?> relationTable, JoinType joinType) {
        this.table = table;
        this.condition = condition;
        this.relationTable = relationTable;
        this.joinType = joinType;
    }
}
