package org.zhuyb.graphbatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan(value = "org.zhuyb.graphbatis.mapper")
@Slf4j
public class GraphBatisMybatisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GraphBatisMybatisApplication.class, args);
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            String port = env.getProperty("server.port");
            String contextPath = env.getProperty("server.servlet.context-path");
            if (contextPath == null) {
                contextPath = "";
            }
            log.info("Graphiql URL http://{}:{}{}/graphiql", hostAddress, port, contextPath);
            log.info("Voyager URL http://{}:{}{}/voyager", hostAddress, port, contextPath);
        } catch (UnknownHostException e) {
            //do noting
        }
    }

}
