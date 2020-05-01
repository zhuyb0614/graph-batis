package org.zhuyb.graphbatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhuyb.graphbatis.entity.vo.RoomVo;
import org.zhuyb.graphbatis.mapper.RoomMapper;
import org.zhuyb.graphbatis.service.RoomService;

import java.util.List;

/**
 * @author zhuyb
 * @date 2020/4/25
 */
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired(required = false) //idea 报红
    private RoomMapper roomMapper;

    @Override
    public PageInfo<RoomVo> getPageInfo(int pageNum, int pageSize, RoomVo roomVo) {
        PageHelper.startPage(pageNum, pageSize);
        List<RoomVo> roomVos = roomMapper.findRoomVos(roomVo);
        return new PageInfo<>(roomVos);
    }
}
