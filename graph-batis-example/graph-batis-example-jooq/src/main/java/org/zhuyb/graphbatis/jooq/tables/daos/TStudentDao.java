/*
 * This file is generated by jOOQ.
 */
package org.zhuyb.graphbatis.jooq.tables.daos;


import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.zhuyb.graphbatis.jooq.tables.TStudent;
import org.zhuyb.graphbatis.jooq.tables.records.TStudentRecord;

import javax.annotation.Generated;
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
@Repository
public class TStudentDao extends DAOImpl<TStudentRecord, org.zhuyb.graphbatis.jooq.tables.pojos.TStudent, Integer> {

    /**
     * Create a new TStudentDao without any configuration
     */
    public TStudentDao() {
        super(TStudent.T_STUDENT, org.zhuyb.graphbatis.jooq.tables.pojos.TStudent.class);
    }

    /**
     * Create a new TStudentDao with an attached configuration
     */
    @Autowired
    public TStudentDao(Configuration configuration) {
        super(TStudent.T_STUDENT, org.zhuyb.graphbatis.jooq.tables.pojos.TStudent.class, configuration);
    }

    @Override
    public Integer getId(org.zhuyb.graphbatis.jooq.tables.pojos.TStudent object) {
        return object.getStudentId();
    }

    /**
     * Fetch records that have <code>student_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchRangeOfStudentId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TStudent.T_STUDENT.STUDENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_id IN (values)</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchByStudentId(Integer... values) {
        return fetch(TStudent.T_STUDENT.STUDENT_ID, values);
    }

    /**
     * Fetch a unique record that has <code>student_id = value</code>
     */
    public org.zhuyb.graphbatis.jooq.tables.pojos.TStudent fetchOneByStudentId(Integer value) {
        return fetchOne(TStudent.T_STUDENT.STUDENT_ID, value);
    }

    /**
     * Fetch records that have <code>room_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchRangeOfRoomId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TStudent.T_STUDENT.ROOM_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>room_id IN (values)</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchByRoomId(Integer... values) {
        return fetch(TStudent.T_STUDENT.ROOM_ID, values);
    }

    /**
     * Fetch records that have <code>student_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchRangeOfStudentName(String lowerInclusive, String upperInclusive) {
        return fetchRange(TStudent.T_STUDENT.STUDENT_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_name IN (values)</code>
     */
    public List<org.zhuyb.graphbatis.jooq.tables.pojos.TStudent> fetchByStudentName(String... values) {
        return fetch(TStudent.T_STUDENT.STUDENT_NAME, values);
    }
}
