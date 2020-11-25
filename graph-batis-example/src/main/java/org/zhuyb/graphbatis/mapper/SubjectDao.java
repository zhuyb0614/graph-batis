package org.zhuyb.graphbatis.mapper;

import org.zhuyb.graphbatis.entity.Subject;

public interface SubjectDao {
    /**
     * @mbg.generated 2020-11-25
     */
    int deleteById(Integer subjectId);

    /**
     * @mbg.generated 2020-11-25
     */
    int insert(Subject record);

    /**
     * @mbg.generated 2020-11-25
     */
    int insertSelective(Subject record);

    /**
     * @mbg.generated 2020-11-25
     */
    Subject selectById(Integer subjectId);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateByIdSelective(Subject record);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateById(Subject record);
}