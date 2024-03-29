/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import org.zhuyb.graphbatis.jooq.tables.TSubject;

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
public class TSubjectRecord extends UpdatableRecordImpl<TSubjectRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1573547311;

    /**
     * Create a detached TSubjectRecord
     */
    public TSubjectRecord() {
        super(TSubject.T_SUBJECT);
    }

    /**
     * Create a detached, initialised TSubjectRecord
     */
    public TSubjectRecord(Integer subjectId, String subjectName) {
        super(TSubject.T_SUBJECT);

        set(0, subjectId);
        set(1, subjectName);
    }

    /**
     * Getter for <code>t_subject.subject_id</code>.
     */
    public Integer getSubjectId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>t_subject.subject_id</code>.
     */
    public TSubjectRecord setSubjectId(Integer value) {
        set(0, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>t_subject.subject_name</code>.
     */
    @Size(max = 50)
    public String getSubjectName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>t_subject.subject_name</code>.
     */
    public TSubjectRecord setSubjectName(String value) {
        set(1, value);
        return this;
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return TSubject.T_SUBJECT.SUBJECT_ID;
    }

    @Override
    public Field<String> field2() {
        return TSubject.T_SUBJECT.SUBJECT_NAME;
    }

    @Override
    public Integer component1() {
        return getSubjectId();
    }

    @Override
    public String component2() {
        return getSubjectName();
    }

    @Override
    public Integer value1() {
        return getSubjectId();
    }

    @Override
    public String value2() {
        return getSubjectName();
    }

    @Override
    public TSubjectRecord value1(Integer value) {
        setSubjectId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public TSubjectRecord value2(String value) {
        setSubjectName(value);
        return this;
    }

    @Override
    public TSubjectRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }
}
