package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.SelectFieldOrAsterisk;
import org.zhuyb.graphbatis.dao.TableJoinCondition;

import static org.zhuyb.graphbatis.dao.Constants.*;

/**
 * @author zhuyb
 */
public class ClassSQLDynamicQueryDao<T> extends JooqDynamicQuery<T> {

    public ClassSQLDynamicQueryDao(DSLContext create, Class<T> fetchInto) {
        super(new SelectFieldOrAsterisk[]{
                st.STUDENT_NAME,
                st.STUDENT_ID,
                su.SUBJECT_ID,
                su.SUBJECT_NAME,
                t.TEACHER_ID,
                t.TEACHER_NAME,
                r.ROOM_ID,
                r.ROOM_NAME
        }, new TableJoinCondition[]{
                new TableJoinCondition(tr, tr.ROOM_ID.eq(r.ROOM_ID), r, JoinType.JOIN),
                new TableJoinCondition(st, st.ROOM_ID.eq(r.ROOM_ID), r, JoinType.JOIN),
                new TableJoinCondition(t, t.TEACHER_ID.eq(tr.TEACHER_ID), tr, JoinType.JOIN),
                new TableJoinCondition(su, su.SUBJECT_ID.eq(t.SUBJECT_ID), t, JoinType.JOIN)
        }, fetchInto, create, r);
    }
}
