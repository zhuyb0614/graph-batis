package org.zhuyb.graphbatis.controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.pagehelper.PageInfo;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhuyb.graphbatis.entity.vo.RoomVo;
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
        PageInfo<RoomVo> pageInfo = roomService.getPageInfo(0, 10, roomVo);
        return pageInfo.getList();
    }
}