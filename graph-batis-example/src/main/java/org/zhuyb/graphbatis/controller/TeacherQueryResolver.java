package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.DataFetchingEnvHolder;
import org.zhuyb.graphbatis.entity.Teacher;
import org.zhuyb.graphbatis.mapper.TeacherDao;

import java.util.List;

@Component
public class TeacherQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private TeacherDao teacherDao;

    public List<Teacher> findTeachers(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        DataFetchingEnvHolder.put(dataFetchingEnvironment);
        return teacherDao.findAll();
    }

}