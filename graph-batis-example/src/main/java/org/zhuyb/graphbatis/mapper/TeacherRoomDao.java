package org.zhuyb.graphbatis.mapper;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.TeacherRoom;

@Repository
public interface TeacherRoomDao {
    /**
     * @mbg.generated 2020-11-25
     */
    int deleteById(Integer teacherRoomId);

    /**
     * @mbg.generated 2020-11-25
     */
    int insert(TeacherRoom record);

    /**
     * @mbg.generated 2020-11-25
     */
    int insertSelective(TeacherRoom record);

    /**
     * @mbg.generated 2020-11-25
     */
    TeacherRoom selectById(Integer teacherRoomId);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateByIdSelective(TeacherRoom record);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateById(TeacherRoom record);
}