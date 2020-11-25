package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.mapper.SubjectDao;

@Component
public class SubjectQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private SubjectDao subjectDao;
}