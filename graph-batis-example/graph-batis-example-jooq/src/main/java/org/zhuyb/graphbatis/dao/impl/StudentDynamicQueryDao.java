package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Student;

/**
 * @author zhuyb
 */
@Repository
public class StudentDynamicQueryDao extends ClassSQLDynamicQueryDao<Student> {

    public StudentDynamicQueryDao(DSLContext create) {
        super(create, Student.class);
    }
}
