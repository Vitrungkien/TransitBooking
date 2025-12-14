package com.OneBpy.configs;

import com.OneBpy.models.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Bean
    public JedisPool jedisPool(RedisProperties props) {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(props.getPool().getMaxTotal());
        poolConfig.setMaxIdle(props.getPool().getMaxIdle());
        poolConfig.setMinIdle(props.getPool().getMinIdle());

        // ⭐ FIX LỖI
        poolConfig.setJmxEnabled(false);

        return new JedisPool(
                poolConfig,
                props.getHost(),
                props.getPort(),
                props.getTimeout()
        );
    }
}

