package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.entity.RoomVo;
import org.zhuyb.graphbatis.interceptor.DataFetchingEnvHolder;
import org.zhuyb.graphbatis.service.RoomService;

import java.util.List;

@Component
public class RoomQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private RoomService roomService;

    public List<RoomVo> findRoomVos(
            Integer studentId,
            Integer teacherId,
            DataFetchingEnvironment dataFetchingEnvironment
    ) {
        RoomVo roomVo = new RoomVo();
        roomVo.setStudentId(studentId);
        roomVo.setTeacherId(teacherId);
        DataFetchingEnvHolder.put(dataFetchingEnvironment);
        List<RoomVo> roomVos = roomService.findRoomVos(roomVo);
        return roomVos;
    }
}