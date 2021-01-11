package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.jooq.tables.*;
import org.zhuyb.graphbatis.mapper.RoomDao;

import java.util.List;
import java.util.Map;

/**
 * @author zhuyb
 */
@Repository
public class RoomDaoImpl implements RoomDao {
    @Autowired
    protected DSLContext create;

    @Override
    public int deleteById(Integer roomId) {
        return 0;
    }

    @Override
    public int insert(Room record) {
        return 0;
    }

    @Override
    public int insertSelective(Room record) {
        return 0;
    }

    @Override
    public Room selectById(Integer roomId) {
        return null;
    }

    @Override
    public int updateByIdSelective(Room record) {
        return 0;
    }

    @Override
    public int updateById(Room record) {
        return 0;
    }

    /**
     * SELECT st.student_name,
     * st.student_id,
     * su.subject_id,
     * su.subject_name,
     * t.teacher_id,
     * t.teacher_name,
     * r.room_id,
     * r.room_name
     * FROM t_room AS r
     * JOIN t_teacher_room AS tr ON tr.room_id = r.room_id
     * JOIN t_student AS st ON st.room_id = r.room_id
     * JOIN t_teacher AS t ON t.teacher_id = tr.teacher_id
     * JOIN t_subject AS su ON su.subject_id = t.subject_id
     *
     * @param arguments
     * @return
     */
    @Override
    public List<Room> findAll(Map<String, Object> arguments) {
        TRoom r = TRoom.T_ROOM.as("r");
        TTeacherRoom tr = TTeacherRoom.T_TEACHER_ROOM.as("tr");
        TStudent st = TStudent.T_STUDENT.as("st");
        TTeacher t = TTeacher.T_TEACHER.as("t");
        TSubject su = TSubject.T_SUBJECT.as("su");
        return create.select(
                st.STUDENT_NAME,
                st.STUDENT_ID,
                su.SUBJECT_ID,
                su.SUBJECT_NAME,
                t.TEACHER_ID,
                t.TEACHER_NAME,
                r.ROOM_ID,
                r.ROOM_NAME
        )
                .from(r)
                .join(tr).on(tr.ROOM_ID.eq(r.ROOM_ID))
                .join(st).on(st.ROOM_ID.eq(r.ROOM_ID))
                .join(t).on(t.TEACHER_ID.eq(tr.TEACHER_ID))
                .join(su).on(su.SUBJECT_ID.eq(t.SUBJECT_ID))
                .fetchInto(Room.class);
    }
}
