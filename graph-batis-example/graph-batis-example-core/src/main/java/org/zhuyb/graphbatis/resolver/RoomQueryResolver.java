package org.zhuyb.graphbatis.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.dq.DynamicQueryDao;
import org.zhuyb.graphbatis.entity.Room;

import java.util.List;

@Component
public class RoomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private DynamicQueryDao<Room> roomDynamicQueryDao;

    /**
     * 参数必须定义,虽然dataFetchingEnvironment里已经持有了这些参数
     *
     * @param roomId
     * @param teachId
     * @param dataFetchingEnvironment
     * @return
     */
    public List<Room> findRooms(
            Integer roomId,
            Integer teachId,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        return roomDynamicQueryDao.findAll(dataFetchingEnvironment.getArguments());
    }


}