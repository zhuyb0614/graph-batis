package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.mapper.RoomDao;

@Component
public class RoomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private RoomDao roomDao;
}