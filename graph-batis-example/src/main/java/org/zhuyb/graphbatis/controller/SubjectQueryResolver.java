package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.DataFetchingEnvHolder;
import org.zhuyb.graphbatis.entity.Subject;
import org.zhuyb.graphbatis.mapper.SubjectDao;

import java.util.List;

@Component
public class SubjectQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private SubjectDao subjectDao;

    public List<Subject> findSubjects(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        DataFetchingEnvHolder.put(dataFetchingEnvironment);
        return subjectDao.findAll();
    }

}