/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.zhuyb.graphbatis.jooq.tables.TTeacherRoom;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.12.4"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TTeacherRoomRecord extends UpdatableRecordImpl<TTeacherRoomRecord> implements Record3<Integer, Integer, Integer> {

    private static final long serialVersionUID = -1501527616;

    /**
     * Create a detached TTeacherRoomRecord
     */
    public TTeacherRoomRecord() {
        super(TTeacherRoom.T_TEACHER_ROOM);
    }

    /**
     * Create a detached, initialised TTeacherRoomRecord
     */
    public TTeacherRoomRecord(Integer teacherRoomId, Integer teacherId, Integer roomId) {
        super(TTeacherRoom.T_TEACHER_ROOM);

        set(0, teacherRoomId);
        set(1, teacherId);
        set(2, roomId);
    }

    /**
     * Getter for <code>t_teacher_room.teacher_room_id</code>.
     */
    public Integer getTeacherRoomId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>t_teacher_room.teacher_room_id</code>.
     */
    public TTeacherRoomRecord setTeacherRoomId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>t_teacher_room.teacher_id</code>.
     */
    public Integer getTeacherId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>t_teacher_room.teacher_id</code>.
     */
    public TTeacherRoomRecord setTeacherId(Integer value) {
        set(1, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>t_teacher_room.room_id</code>.
     */
    public Integer getRoomId() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>t_teacher_room.room_id</code>.
     */
    public TTeacherRoomRecord setRoomId(Integer value) {
        set(2, value);
        return this;
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row3<Integer, Integer, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Integer, Integer, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return TTeacherRoom.T_TEACHER_ROOM.TEACHER_ROOM_ID;
    }

    @Override
    public Field<Integer> field2() {
        return TTeacherRoom.T_TEACHER_ROOM.TEACHER_ID;
    }

    @Override
    public Field<Integer> field3() {
        return TTeacherRoom.T_TEACHER_ROOM.ROOM_ID;
    }

    @Override
    public Integer component1() {
        return getTeacherRoomId();
    }

    @Override
    public Integer component2() {
        return getTeacherId();
    }

    @Override
    public Integer component3() {
        return getRoomId();
    }

    @Override
    public Integer value1() {
        return getTeacherRoomId();
    }

    @Override
    public Integer value2() {
        return getTeacherId();
    }

    @Override
    public Integer value3() {
        return getRoomId();
    }

    @Override
    public TTeacherRoomRecord value1(Integer value) {
        setTeacherRoomId(value);
        return this;
    }

    @Override
    public TTeacherRoomRecord value2(Integer value) {
        setTeacherId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public TTeacherRoomRecord value3(Integer value) {
        setRoomId(value);
        return this;
    }

    @Override
    public TTeacherRoomRecord values(Integer value1, Integer value2, Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }
}
