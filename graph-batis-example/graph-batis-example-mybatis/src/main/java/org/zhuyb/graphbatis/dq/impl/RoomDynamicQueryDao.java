package org.zhuyb.graphbatis.dq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.mapper.RoomDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class RoomDynamicQueryDao implements DynamicQueryDao<Room> {

    @Autowired
    private RoomDao roomDao;

    @Override
    public List<Room> findAll(Map<String, Object> arguments) {
        return roomDao.findAll(arguments);
    }
}
