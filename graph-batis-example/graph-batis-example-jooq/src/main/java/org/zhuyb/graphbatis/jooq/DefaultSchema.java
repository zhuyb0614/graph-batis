/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq;


import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
import org.zhuyb.graphbatis.jooq.tables.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
public class DefaultSchema extends SchemaImpl {

    /**
     * The reference instance of <code></code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();
    private static final long serialVersionUID = 1230221950;
    /**
     * The table <code>t_room</code>.
     */
    public final TRoom T_ROOM = org.zhuyb.graphbatis.jooq.tables.TRoom.T_ROOM;

    /**
     * The table <code>t_student</code>.
     */
    public final TStudent T_STUDENT = org.zhuyb.graphbatis.jooq.tables.TStudent.T_STUDENT;

    /**
     * The table <code>t_subject</code>.
     */
    public final TSubject T_SUBJECT = org.zhuyb.graphbatis.jooq.tables.TSubject.T_SUBJECT;

    /**
     * The table <code>t_teacher</code>.
     */
    public final TTeacher T_TEACHER = org.zhuyb.graphbatis.jooq.tables.TTeacher.T_TEACHER;

    /**
     * The table <code>t_teacher_room</code>.
     */
    public final TTeacherRoom T_TEACHER_ROOM = org.zhuyb.graphbatis.jooq.tables.TTeacherRoom.T_TEACHER_ROOM;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
                TRoom.T_ROOM,
                TStudent.T_STUDENT,
                TSubject.T_SUBJECT,
                TTeacher.T_TEACHER,
                TTeacherRoom.T_TEACHER_ROOM);
    }
}
