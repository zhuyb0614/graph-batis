package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.mapper.TeacherRoomDao;

@Component
public class TeacherRoomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private TeacherRoomDao teacherRoomDao;
}