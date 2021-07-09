package org.zhuyb.graphbatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
public class GraphBatisJooqApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(GraphBatisJooqApplication.class, args);
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            log.info("Graphiql URL http://{}:{}{}/graphiql", hostAddress, env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
            log.info("Voyager URL http://{}:{}{}/voyager", hostAddress, env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
        } catch (UnknownHostException e) {
            //do noting
        }
    }

}
