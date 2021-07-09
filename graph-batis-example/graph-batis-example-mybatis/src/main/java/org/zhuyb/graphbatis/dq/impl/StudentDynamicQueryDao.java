package org.zhuyb.graphbatis.dq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Student;
import org.zhuyb.graphbatis.mapper.StudentDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class StudentDynamicQueryDao implements DynamicQueryDao<Student> {

    @Autowired
    private StudentDao studentDao;

    @Override
    public List<Student> findAll(Map<String, Object> arguments) {
        return studentDao.findAll();
    }
}
