package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Flat;
import org.zhuyb.graphbatis.entity.Room;

/**
 * @author zhuyb
 */
@Repository
public class FlatDynamicQueryDao extends ClassSQLDynamicQueryDao<Flat> {

    public FlatDynamicQueryDao(DSLContext create) {
        super(create, Flat.class);
    }
}
