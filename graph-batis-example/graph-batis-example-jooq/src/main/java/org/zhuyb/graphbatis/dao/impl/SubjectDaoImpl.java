package org.zhuyb.graphbatis.dao.impl;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Subject;
import org.zhuyb.graphbatis.mapper.SubjectDao;

import java.util.List;

/**
 * @author zhuyb
 */
@Repository
public class SubjectDaoImpl implements SubjectDao {
    @Override
    public int deleteById(Integer subjectId) {
        return 0;
    }

    @Override
    public int insert(Subject record) {
        return 0;
    }

    @Override
    public int insertSelective(Subject record) {
        return 0;
    }

    @Override
    public Subject selectById(Integer subjectId) {
        return null;
    }

    @Override
    public int updateByIdSelective(Subject record) {
        return 0;
    }

    @Override
    public int updateById(Subject record) {
        return 0;
    }

    @Override
    public List<Subject> findAll() {
        return null;
    }
}
