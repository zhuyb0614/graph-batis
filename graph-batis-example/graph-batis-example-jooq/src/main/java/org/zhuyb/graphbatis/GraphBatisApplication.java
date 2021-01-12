package org.zhuyb.graphbatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zhuyb.graphbatis.interceptor.JooqCleanSqlVisitListener;

@SpringBootApplication
public class GraphBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphBatisApplication.class, args);
    }

    @Bean
    public JooqCleanSqlVisitListener jooqCleanSqlVisitListenerProvider() {
        return new JooqCleanSqlVisitListener();
    }

}
