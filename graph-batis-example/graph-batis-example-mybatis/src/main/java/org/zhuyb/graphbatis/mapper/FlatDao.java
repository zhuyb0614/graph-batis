package org.zhuyb.graphbatis.mapper;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Flat;
import org.zhuyb.graphbatis.entity.Room;

import java.util.List;
import java.util.Map;

@Repository
public interface FlatDao {

    List<Flat> findAll(Map<String, Object> arguments);
}