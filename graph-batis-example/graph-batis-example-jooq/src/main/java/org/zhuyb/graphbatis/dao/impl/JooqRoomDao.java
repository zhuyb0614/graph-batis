package org.zhuyb.graphbatis.dao.impl;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.SelectFieldOrAsterisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.dao.TableJoinCondition;
import org.zhuyb.graphbatis.entity.Room;
import org.zhuyb.graphbatis.mapper.RoomDao;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static org.zhuyb.graphbatis.dao.Constants.*;

/**
 * @author zhuyb
 */
@Repository
public class JooqRoomDao implements RoomDao {
    @Autowired
    protected DSLContext create;
    private JooqDynamicQuery<Room> jooqDynamicQuery;

    @PostConstruct
    public void setUp() {
        jooqDynamicQuery = new JooqDynamicQuery<>(new SelectFieldOrAsterisk[]{
                st.STUDENT_NAME,
                st.STUDENT_ID,
                su.SUBJECT_ID,
                su.SUBJECT_NAME,
                t.TEACHER_ID,
                t.TEACHER_NAME,
                r.ROOM_ID,
                r.ROOM_NAME
        }, new TableJoinCondition[]{
                new TableJoinCondition(tr, tr.ROOM_ID.eq(r.ROOM_ID), r, JoinType.JOIN),
                new TableJoinCondition(st, st.ROOM_ID.eq(r.ROOM_ID), r, JoinType.JOIN),
                new TableJoinCondition(t, t.TEACHER_ID.eq(tr.TEACHER_ID), tr, JoinType.JOIN),
                new TableJoinCondition(su, su.SUBJECT_ID.eq(t.SUBJECT_ID), t, JoinType.JOIN)
        }, Room.class, create, r);
    }

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

    @Override
    public List<Room> findAll(Map<String, Object> arguments) {
        return jooqDynamicQuery.findAll(arguments);
    }


}
