package com.gjleon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String userName;
    @Value("${database.password}")
    private String password;

    @Bean
    @Primary
    public Connection connectionMySql() {
        return new Connection(url, userName, password);
    }

    @Bean(name = "connectionMongoDB")
    public Connection connectionMongo() {
        return new Connection("localhost", "devdojoMongo", "goku");
    }
}
