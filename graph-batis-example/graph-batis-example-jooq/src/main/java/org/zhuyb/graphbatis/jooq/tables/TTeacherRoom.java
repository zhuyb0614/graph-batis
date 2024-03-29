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
import org.zhuyb.graphbatis.jooq.tables.records.TTeacherRoomRecord;

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
public class TTeacherRoom extends TableImpl<TTeacherRoomRecord> {

    /**
     * The reference instance of <code>t_teacher_room</code>
     */
    public static final TTeacherRoom T_TEACHER_ROOM = new TTeacherRoom();
    private static final long serialVersionUID = -136048095;
    /**
     * The column <code>t_teacher_room.teacher_room_id</code>.
     */
    public final TableField<TTeacherRoomRecord, Integer> TEACHER_ROOM_ID = createField(DSL.name("teacher_room_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>t_teacher_room.teacher_id</code>.
     */
    public final TableField<TTeacherRoomRecord, Integer> TEACHER_ID = createField(DSL.name("teacher_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");
    /**
     * The column <code>t_teacher_room.room_id</code>.
     */
    public final TableField<TTeacherRoomRecord, Integer> ROOM_ID = createField(DSL.name("room_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>t_teacher_room</code> table reference
     */
    public TTeacherRoom() {
        this(DSL.name("t_teacher_room"), null);
    }

    /**
     * Create an aliased <code>t_teacher_room</code> table reference
     */
    public TTeacherRoom(String alias) {
        this(DSL.name(alias), T_TEACHER_ROOM);
    }

    /**
     * Create an aliased <code>t_teacher_room</code> table reference
     */
    public TTeacherRoom(Name alias) {
        this(alias, T_TEACHER_ROOM);
    }

    private TTeacherRoom(Name alias, Table<TTeacherRoomRecord> aliased) {
        this(alias, aliased, null);
    }

    private TTeacherRoom(Name alias, Table<TTeacherRoomRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TTeacherRoom(Table<O> child, ForeignKey<O, TTeacherRoomRecord> key) {
        super(child, key, T_TEACHER_ROOM);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TTeacherRoomRecord> getRecordType() {
        return TTeacherRoomRecord.class;
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.T_TEACHER_ROOM_PRIMARY);
    }

    @Override
    public Identity<TTeacherRoomRecord, Integer> getIdentity() {
        return Keys.IDENTITY_T_TEACHER_ROOM;
    }

    @Override
    public UniqueKey<TTeacherRoomRecord> getPrimaryKey() {
        return Keys.KEY_T_TEACHER_ROOM_PRIMARY;
    }

    @Override
    public List<UniqueKey<TTeacherRoomRecord>> getKeys() {
        return Arrays.<UniqueKey<TTeacherRoomRecord>>asList(Keys.KEY_T_TEACHER_ROOM_PRIMARY);
    }

    @Override
    public TTeacherRoom as(String alias) {
        return new TTeacherRoom(DSL.name(alias), this);
    }

    @Override
    public TTeacherRoom as(Name alias) {
        return new TTeacherRoom(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TTeacherRoom rename(String name) {
        return new TTeacherRoom(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TTeacherRoom rename(Name name) {
        return new TTeacherRoom(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, Integer, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
