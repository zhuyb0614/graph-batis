package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Flat;

import java.util.List;

@Component
public class FlatQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private DynamicQueryDao<Flat> flatDynamicQueryDao;

    /**
     * 参数必须定义,虽然dataFetchingEnvironment里已经持有了这些参数
     *
     * @param roomId
     * @param teacherId
     * @param studentId
     * @param dataFetchingEnvironment
     * @return
     */
    public List<Flat> findFlat(
            Integer roomId,
            Integer teacherId,
            Integer studentId,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return flatDynamicQueryDao.findAll(dataFetchingEnvironment.getArguments());
    }


}