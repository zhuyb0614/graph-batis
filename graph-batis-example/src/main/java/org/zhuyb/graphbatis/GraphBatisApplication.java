package org.zhuyb.graphbatis;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zhuyb.graphbatis.interceptor.CleanSqlInterceptor;

@SpringBootApplication
@MapperScan(value = "org.zhuyb.graphbatis.mapper")
public class GraphBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphBatisApplication.class, args);
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new CleanSqlInterceptor());
    }

}
