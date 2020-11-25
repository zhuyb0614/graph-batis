package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.mapper.TeacherDao;

@Component
public class TeacherQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private TeacherDao teacherDao;
}