package org.zhuyb.graphbatis.dao.impl;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Student;
import org.zhuyb.graphbatis.mapper.StudentDao;

import java.util.List;

/**
 * @author zhuyb
 */
@Repository
public class StudentDaoImpl implements StudentDao {
    @Override
    public int deleteById(Integer studentId) {
        return 0;
    }

    @Override
    public int insert(Student record) {
        return 0;
    }

    @Override
    public int insertSelective(Student record) {
        return 0;
    }

    @Override
    public Student selectById(Integer studentId) {
        return null;
    }

    @Override
    public int updateByIdSelective(Student record) {
        return 0;
    }

    @Override
    public int updateById(Student record) {
        return 0;
    }

    @Override
    public List<Student> findAll() {
        return null;
    }
}
