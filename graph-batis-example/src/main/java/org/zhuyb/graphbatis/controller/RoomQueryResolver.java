package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.DataFetchingEnvHolder;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.mapper.RoomDao;

import java.util.List;

@Component
public class RoomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private RoomDao roomDao;

    public List<Room> findRooms(
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        DataFetchingEnvHolder.put(dataFetchingEnvironment);
        return roomDao.findAll();
    }


}