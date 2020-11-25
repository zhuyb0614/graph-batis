package org.zhuyb.graphbatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.entity.RoomVo;

import java.util.List;

public interface RoomDao {
    /**
     * @mbg.generated 2020-11-25
     */
    int deleteById(Integer roomId);

    /**
     * @mbg.generated 2020-11-25
     */
    int insert(Room record);

    /**
     * @mbg.generated 2020-11-25
     */
    int insertSelective(Room record);

    /**
     * @mbg.generated 2020-11-25
     */
    Room selectById(Integer roomId);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateByIdSelective(Room record);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateById(Room record);

    List<RoomVo> findRoomVos(@Param("rv") RoomVo roomVo);
}