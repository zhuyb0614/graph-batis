package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Room;

/**
 * @author zhuyb
 */
@Repository
public class RoomDynamicQueryDao extends ClassSQLDynamicQueryDao<Room> {

    public RoomDynamicQueryDao(DSLContext create) {
        super(create, Room.class);
    }
}
