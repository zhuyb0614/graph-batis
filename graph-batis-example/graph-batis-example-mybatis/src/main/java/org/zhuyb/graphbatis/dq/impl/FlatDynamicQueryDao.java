package org.zhuyb.graphbatis.dq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Flat;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.mapper.FlatDao;
import org.zhuyb.graphbatis.mapper.RoomDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class FlatDynamicQueryDao implements DynamicQueryDao<Flat> {

    @Autowired
    private FlatDao flatDao;

    @Override
    public List<Flat> findAll(Map<String, Object> arguments) {
        return flatDao.findAll(arguments);
    }
}
