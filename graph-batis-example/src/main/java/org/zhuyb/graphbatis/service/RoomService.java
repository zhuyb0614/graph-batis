package org.zhuyb.graphbatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhuyb.graphbatis.entity.RoomVo;
import org.zhuyb.graphbatis.mapper.RoomDao;

import java.util.List;

/**
 * @author zhuyb
 * @date 2020/4/25
 */
@Service
public class RoomService {
    @Autowired(required = false) //idea 报红
    private RoomDao roomDao;

    public List<RoomVo> findRoomVos(RoomVo roomVo) {
        return roomDao.findRoomVos(roomVo);
    }
}
