package org.zhuyb.graphbatis.service;

import com.github.pagehelper.PageInfo;
import org.zhuyb.graphbatis.entity.vo.RoomVo;

/**
 * @author zhuyb
 * @date 2020/4/25
 */
public interface RoomService {
    PageInfo<RoomVo> getPageInfo(int pageNum, int pageSize, RoomVo roomVo);
}
