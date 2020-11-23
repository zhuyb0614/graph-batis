package org.zhuyb.graphbatis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhuyb
 */
@Data
@Accessors(chain = true)
public class RoomVo {
    private String studentName;
    private String roomName;
    private String subjectName;
    private String teacherName;
    private Integer studentId;
    private Integer teacherId;
}
