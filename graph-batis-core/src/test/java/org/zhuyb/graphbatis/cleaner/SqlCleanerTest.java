package org.zhuyb.graphbatis.cleaner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.zhuyb.graphbatis.entity.FetchingData;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author zhuyb
 * @date 2020/12/31
 */
@Slf4j
public class SqlCleanerTest {

    @Test
    public void cleanSql() {
        String originSql = "        SELECT st.student_name,\n" +
                "        st.student_id,\n" +
                "        su.subject_id,\n" +
                "        su.subject_name,\n" +
                "        t.teacher_id,\n" +
                "        t.teacher_name,\n" +
                "        r.room_id,\n" +
                "        r.room_name\n" +
                "        FROM t_room AS r\n" +
                "        JOIN t_teacher_room AS tr ON tr.room_id = r.room_id\n" +
                "        JOIN t_student AS st ON st.room_id = r.room_id\n" +
                "        JOIN t_teacher AS t ON t.teacher_id = tr.teacher_id\n" +
                "        JOIN t_subject AS su ON su.subject_id = t.subject_id";
        SqlCleaner sqlCleaner = buildSqlCleaner("org.zhuyb.graphbatis.cleaner.MybatisSqlCleanerImpl");
        FetchingData fetchingData = new FetchingData();
        Set<String> fieldNames = new HashSet<>();
        fieldNames.add("room_id");
        fieldNames.add("room_name");
        fieldNames.add("teacher_id");
        fieldNames.add("teacher_name");
        fetchingData.setFieldNames(fieldNames);
        boolean allPass = true;
        long startTime = System.currentTimeMillis();
        String expectCleanSql = "SELECT t.teacher_id, t.teacher_name, r.room_id, r.room_name FROM t_room AS r JOIN t_teacher_room AS tr ON tr.room_id = r.room_id JOIN t_teacher AS t ON t.teacher_id = tr.teacher_id";
        int executeTimes = 100000;
        for (int i = 0; i < executeTimes; i++) {
            String cleanSql = sqlCleaner.cleanSql(fetchingData, originSql);
            if (!cleanSql.equals(expectCleanSql)) {
                allPass = false;
                break;
            }
        }
        long costTime = System.currentTimeMillis() - startTime;
        log.info("execute {} \n result [{}] times {} cost {} ms", originSql, expectCleanSql, executeTimes, costTime);
        assertTrue(allPass && costTime < 1000);
    }

    @SneakyThrows
    @NotNull
    private SqlCleaner buildSqlCleaner(String cleanClassName) {
        SqlCleaner sqlCleaner = (SqlCleaner) Class.forName(cleanClassName).newInstance();
        Properties properties = new Properties();
        sqlCleaner.setUp(properties);
        return sqlCleaner;
    }
}