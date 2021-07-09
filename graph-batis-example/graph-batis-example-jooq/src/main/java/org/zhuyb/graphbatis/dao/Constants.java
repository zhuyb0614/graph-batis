package org.zhuyb.graphbatis.dao;

import org.zhuyb.graphbatis.jooq.tables.*;

/**
 * @author zhuyb
 */
public interface Constants {
    TRoom r = TRoom.T_ROOM.as("r");
    TTeacherRoom tr = TTeacherRoom.T_TEACHER_ROOM.as("tr");
    TStudent st = TStudent.T_STUDENT.as("st");
    TTeacher t = TTeacher.T_TEACHER.as("t");
    TSubject su = TSubject.T_SUBJECT.as("su");
}
