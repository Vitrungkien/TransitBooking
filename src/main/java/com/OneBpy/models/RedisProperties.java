package com.OneBpy.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private String host;
    private int port;
    private int timeout;
    private Pool pool = new Pool();

    @Setter
    @Getter
    public static class Pool {
        private int maxTotal;
        private int maxIdle;
        private int minIdle;

    }

}
