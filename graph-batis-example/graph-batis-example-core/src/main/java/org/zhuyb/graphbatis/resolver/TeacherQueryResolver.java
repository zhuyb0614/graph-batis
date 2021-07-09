package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Teacher;

import java.util.List;

@Component
public class TeacherQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private DynamicQueryDao<Teacher> teacherDynamicQueryDao;

    public List<Teacher> findTeachers(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return teacherDynamicQueryDao.findAll(dataFetchingEnvironment.getArguments());
    }

}