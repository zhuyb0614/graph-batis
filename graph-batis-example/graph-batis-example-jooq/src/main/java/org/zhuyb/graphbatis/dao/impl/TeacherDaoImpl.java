package org.zhuyb.graphbatis.dao.impl;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Teacher;
import org.zhuyb.graphbatis.mapper.TeacherDao;

import java.util.List;

/**
 * @author zhuyb
 */
@Repository
public class TeacherDaoImpl implements TeacherDao {
    @Override
    public int deleteById(Integer teacherId) {
        return 0;
    }

    @Override
    public int insert(Teacher record) {
        return 0;
    }

    @Override
    public int insertSelective(Teacher record) {
        return 0;
    }

    @Override
    public Teacher selectById(Integer teacherId) {
        return null;
    }

    @Override
    public int updateByIdSelective(Teacher record) {
        return 0;
    }

    @Override
    public int updateById(Teacher record) {
        return 0;
    }

    @Override
    public List<Teacher> findAll() {
        return null;
    }
}
