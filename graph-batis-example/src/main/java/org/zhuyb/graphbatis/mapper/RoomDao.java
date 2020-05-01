package org.zhuyb.graphbatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.entity.RoomVo;

import java.util.List;

public interface RoomDao {

    int deleteByPrimaryKey(Integer roomId);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer roomId);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    List<RoomVo> findRoomVos(@Param("rv") RoomVo roomVo);
}