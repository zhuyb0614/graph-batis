package org.zhuyb.graphbatis.entity;

import lombok.Data;
import org.zhuyb.graphbatis.anno.QueryName;

/**
 * @author yunbo.zhu
 * @version 1.0
 * @date 2021/7/9 5:28 下午
 */
@Data
@QueryName("findFlat")
public class Flat {
    /**
     *
     */
    private Integer roomId;
    /**
     *
     */
    private String roomName;
    /**
     *
     */
    private Integer studentId;
    /**
     *
     */
    private String studentName;
    /**
     *
     */
    private Integer subjectId;
    /**
     *
     */
    private String subjectName;
    /**
     *
     */
    private Integer teacherId;
    /**
     *
     */
    private String teacherName;
}
