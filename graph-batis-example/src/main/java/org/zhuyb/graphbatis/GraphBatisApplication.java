package org.zhuyb.graphbatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "org.zhuyb.graphbatis.mapper")
public class GraphBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphBatisApplication.class, args);
    }

}
