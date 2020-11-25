package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.mapper.StudentDao;

@Component
public class StudentQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private StudentDao studentDao;
}