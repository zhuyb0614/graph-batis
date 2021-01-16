package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Subject;

/**
 * @author zhuyb
 */
@Repository
public class SubjectDynamicQueryDao extends ClassSQLDynamicQueryDao<Subject> {

    public SubjectDynamicQueryDao(DSLContext create) {
        super(create, Subject.class);
    }
}
