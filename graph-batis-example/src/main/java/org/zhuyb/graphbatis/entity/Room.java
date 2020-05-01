package org.zhuyb.graphbatis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Room implements Serializable {
    private static final long serialVersionUID = 20200501L;

    private Integer roomId;

    private String roomName;


}