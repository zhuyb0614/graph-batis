package org.zhuyb.graphbatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zhuyb.graphbatis.entity.vo.RoomVo;

import java.util.List;

/**
 * @author zhuyb
 * @date 2020/4/25
 */
public interface RoomMapper {
    @Select("      SELECT\n" +
            "         st.student_name,\n" +
            "         st.student_id,\n" +
            "         su.subject_name,\n" +
            "         t.teacher_name,\n" +
            "         t.teacher_id,\n" +
            "         r.room_name\n" +
            "      FROM\n" +
            "         t_teacher_room AS tr\n" +
            "      JOIN t_room AS r ON tr.room_id = r.room_id\n" +
            "      JOIN t_student AS st ON st.room_id = r.room_id\n" +
            "      JOIN t_teacher AS t ON tr.teacher_id = t.teacher_id\n" +
            "      JOIN t_subject AS su ON su.subject_id = t.subject_id" +
            " where r.room_name = #{rv.roomName} and su.subject_name = #{rv.subjectName}")
    List<RoomVo> findRoomVos(@Param("rv") RoomVo roomVo);
}
