package org.zhuyb.graphbatis.mapper;

import org.zhuyb.graphbatis.entity.Teacher;

public interface TeacherDao {
    /**
     * @mbg.generated 2020-11-25
     */
    int deleteById(Integer teacherId);

    /**
     * @mbg.generated 2020-11-25
     */
    int insert(Teacher record);

    /**
     * @mbg.generated 2020-11-25
     */
    int insertSelective(Teacher record);

    /**
     * @mbg.generated 2020-11-25
     */
    Teacher selectById(Integer teacherId);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateByIdSelective(Teacher record);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateById(Teacher record);
}