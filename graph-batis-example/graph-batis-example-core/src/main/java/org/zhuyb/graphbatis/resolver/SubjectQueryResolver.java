package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Subject;

import java.util.List;

@Component
public class SubjectQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private DynamicQueryDao<Subject> subjectDynamicQueryDao;

    public List<Subject> findSubjects(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return subjectDynamicQueryDao.findAll(dataFetchingEnvironment.getArguments());
    }

}