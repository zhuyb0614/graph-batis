package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Student;

import java.util.List;

@Component
public class StudentQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private DynamicQueryDao<Student> studentDynamicQueryDao;

    public List<Student> findStudents(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return studentDynamicQueryDao.findAll(dataFetchingEnvironment.getArguments());
    }

}