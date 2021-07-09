package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Teacher;

/**
 * @author zhuyb
 */
@Repository
public class TeacherDynamicQueryDao extends ClassSQLDynamicQueryDao<Teacher> {

    public TeacherDynamicQueryDao(DSLContext create) {
        super(create, Teacher.class);
    }
}
