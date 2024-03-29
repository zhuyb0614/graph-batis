/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq.tables;


import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.zhuyb.graphbatis.jooq.DefaultSchema;
import org.zhuyb.graphbatis.jooq.Indexes;
import org.zhuyb.graphbatis.jooq.Keys;
import org.zhuyb.graphbatis.jooq.tables.records.TTeacherRecord;

import javax.annotation.Generated;
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
public class TTeacher extends TableImpl<TTeacherRecord> {

    /**
     * The reference instance of <code>t_teacher</code>
     */
    public static final TTeacher T_TEACHER = new TTeacher();
    private static final long serialVersionUID = -212637272;
    /**
     * The column <code>t_teacher.teacher_id</code>.
     */
    public final TableField<TTeacherRecord, Integer> TEACHER_ID = createField(DSL.name("teacher_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>t_teacher.teacher_name</code>.
     */
    public final TableField<TTeacherRecord, String> TEACHER_NAME = createField(DSL.name("teacher_name"), org.jooq.impl.SQLDataType.VARCHAR(50), this, "");
    /**
     * The column <code>t_teacher.subject_id</code>.
     */
    public final TableField<TTeacherRecord, Integer> SUBJECT_ID = createField(DSL.name("subject_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>t_teacher</code> table reference
     */
    public TTeacher() {
        this(DSL.name("t_teacher"), null);
    }

    /**
     * Create an aliased <code>t_teacher</code> table reference
     */
    public TTeacher(String alias) {
        this(DSL.name(alias), T_TEACHER);
    }

    /**
     * Create an aliased <code>t_teacher</code> table reference
     */
    public TTeacher(Name alias) {
        this(alias, T_TEACHER);
    }

    private TTeacher(Name alias, Table<TTeacherRecord> aliased) {
        this(alias, aliased, null);
    }

    private TTeacher(Name alias, Table<TTeacherRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TTeacher(Table<O> child, ForeignKey<O, TTeacherRecord> key) {
        super(child, key, T_TEACHER);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TTeacherRecord> getRecordType() {
        return TTeacherRecord.class;
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.T_TEACHER_PRIMARY);
    }

    @Override
    public Identity<TTeacherRecord, Integer> getIdentity() {
        return Keys.IDENTITY_T_TEACHER;
    }

    @Override
    public UniqueKey<TTeacherRecord> getPrimaryKey() {
        return Keys.KEY_T_TEACHER_PRIMARY;
    }

    @Override
    public List<UniqueKey<TTeacherRecord>> getKeys() {
        return Arrays.<UniqueKey<TTeacherRecord>>asList(Keys.KEY_T_TEACHER_PRIMARY);
    }

    @Override
    public TTeacher as(String alias) {
        return new TTeacher(DSL.name(alias), this);
    }

    @Override
    public TTeacher as(Name alias) {
        return new TTeacher(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TTeacher rename(String name) {
        return new TTeacher(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TTeacher rename(Name name) {
        return new TTeacher(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
