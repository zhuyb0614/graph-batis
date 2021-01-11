package org.zhuyb.graphbatis.entity;

import java.io.Serializable;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_teacher_room
 * <p>
 * If you want to modify
 * <p>
 * 1. checkout feature/gen
 * 2. mybatis-generator:generate
 * 3. commit -m 'your comment'
 * 4. push
 * 5. checkout your branch
 * 6. merge feature/gen
 */
public class TeacherRoom implements Serializable {
    /**
     * t_teacher_room
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Integer teacherRoomId;
    /**
     *
     */
    private Integer teacherId;
    /**
     *
     */
    private Integer roomId;

    /**
     * @return teacher_room_id
     */
    public Integer getTeacherRoomId() {
        return teacherRoomId;
    }

    /**
     * @param teacherRoomId
     */
    public void setTeacherRoomId(Integer teacherRoomId) {
        this.teacherRoomId = teacherRoomId;
    }

    /**
     * @return teacher_id
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return room_id
     */
    public Integer getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     */
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}