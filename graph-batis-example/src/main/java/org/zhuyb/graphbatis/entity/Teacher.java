package org.zhuyb.graphbatis.entity;

import java.io.Serializable;

/**
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_teacher
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
public class Teacher implements Serializable {
    /**
     * t_teacher
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Integer teacherId;
    /**
     *
     */
    private String teacherName;
    /**
     *
     */
    private Integer subjectId;

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
     * @return teacher_name
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName == null ? null : teacherName.trim();
    }

    /**
     * @return subject_id
     */
    public Integer getSubjectId() {
        return subjectId;
    }

    /**
     * @param subjectId
     */
    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }
}