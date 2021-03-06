package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.entity.Student;
import org.zhuyb.graphbatis.mapper.StudentDao;

import java.util.List;

@Component
public class StudentQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private StudentDao studentDao;

    public List<Student> findStudents(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return studentDao.findAll();
    }

}