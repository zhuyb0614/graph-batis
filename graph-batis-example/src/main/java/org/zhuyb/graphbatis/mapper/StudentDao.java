package org.zhuyb.graphbatis.mapper;

import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Student;

import java.util.List;

@Repository
public interface StudentDao {
    /**
     * @mbg.generated 2020-11-25
     */
    int deleteById(Integer studentId);

    /**
     * @mbg.generated 2020-11-25
     */
    int insert(Student record);

    /**
     * @mbg.generated 2020-11-25
     */
    int insertSelective(Student record);

    /**
     * @mbg.generated 2020-11-25
     */
    Student selectById(Integer studentId);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateByIdSelective(Student record);

    /**
     * @mbg.generated 2020-11-25
     */
    int updateById(Student record);

    List<Student> findAll();
}