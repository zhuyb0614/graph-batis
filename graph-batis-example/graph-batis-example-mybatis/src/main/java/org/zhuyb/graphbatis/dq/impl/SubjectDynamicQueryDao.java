package org.zhuyb.graphbatis.dq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Subject;
import org.zhuyb.graphbatis.mapper.SubjectDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class SubjectDynamicQueryDao implements DynamicQueryDao<Subject> {

    @Autowired
    private SubjectDao subjectDao;

    @Override
    public List<Subject> findAll(Map<String, Object> arguments) {
        return subjectDao.findAll();
    }
}
