/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.validation.constraints.Size;


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
public class TTeacher {

    private Integer teacherId;
    private String teacherName;
    private Integer subjectId;

    public TTeacher() {
    }

    public TTeacher(TTeacher value) {
        this.teacherId = value.teacherId;
        this.teacherName = value.teacherName;
        this.subjectId = value.subjectId;
    }

    public TTeacher(
            Integer teacherId,
            String teacherName,
            Integer subjectId
    ) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.subjectId = subjectId;
    }

    public Integer getTeacherId() {
        return this.teacherId;
    }

    public TTeacher setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
        return this;
    }

    @Size(max = 50)
    public String getTeacherName() {
        return this.teacherName;
    }

    public TTeacher setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        return this;
    }

    public Integer getSubjectId() {
        return this.subjectId;
    }

    public TTeacher setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TTeacher other = (TTeacher) obj;
        if (teacherId == null) {
            if (other.teacherId != null)
                return false;
        } else if (!teacherId.equals(other.teacherId))
            return false;
        if (teacherName == null) {
            if (other.teacherName != null)
                return false;
        } else if (!teacherName.equals(other.teacherName))
            return false;
        if (subjectId == null) {
            if (other.subjectId != null)
                return false;
        } else if (!subjectId.equals(other.subjectId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.teacherId == null) ? 0 : this.teacherId.hashCode());
        result = prime * result + ((this.teacherName == null) ? 0 : this.teacherName.hashCode());
        result = prime * result + ((this.subjectId == null) ? 0 : this.subjectId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TTeacher (");

        sb.append(teacherId);
        sb.append(", ").append(teacherName);
        sb.append(", ").append(subjectId);

        sb.append(")");
        return sb.toString();
    }
}
