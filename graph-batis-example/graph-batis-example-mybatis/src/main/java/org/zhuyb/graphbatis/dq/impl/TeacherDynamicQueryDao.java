package org.zhuyb.graphbatis.dq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Teacher;
import org.zhuyb.graphbatis.mapper.TeacherDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class TeacherDynamicQueryDao implements DynamicQueryDao<Teacher> {

    @Autowired
    private TeacherDao teacherDao;

    @Override
    public List<Teacher> findAll(Map<String, Object> arguments) {
        return teacherDao.findAll();
    }
}
