package org.zhuyb.graphbatis.dao.impl;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.TeacherRoom;
import org.zhuyb.graphbatis.mapper.TeacherRoomDao;

/**
 * @author zhuyb
 */
@Repository
public class TeacherRoomDaoImpl implements TeacherRoomDao {
    @Override
    public int deleteById(Integer teacherRoomId) {
        return 0;
    }

    @Override
    public int insert(TeacherRoom record) {
        return 0;
    }

    @Override
    public int insertSelective(TeacherRoom record) {
        return 0;
    }

    @Override
    public TeacherRoom selectById(Integer teacherRoomId) {
        return null;
    }

    @Override
    public int updateByIdSelective(TeacherRoom record) {
        return 0;
    }

    @Override
    public int updateById(TeacherRoom record) {
        return 0;
    }
}
