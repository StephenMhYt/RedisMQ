package cn.com.maoh.config;

import cn.com.maoh.template.JedisFactory;
import cn.com.maoh.template.JedisTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2017/12/13.
 */
@Configuration
public class RedisQueueConfig {

    @Value("${redis.nodes}")
    private String redisNodes;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.pool.size}")
    private int maxTotal;

    @Value("${redis.queue.channel")
    private String channel;

    @Value("${redis.keep.alive.time}")
    private int keepAliveTime;

    @Value("${redis.max.pool.size}")
    private int maximumPoolSize;

    @Bean(name = "jedisFactory")
    public JedisFactory jedisFactory(){
        JedisFactory jedisFactory = new JedisFactory();
        jedisFactory.setRedisNodes(redisNodes);
        jedisFactory.setPassword(password);
        jedisFactory.setMaxTotal(maxTotal);
        return jedisFactory;
    }

    @Bean
    public JedisTemplate jedisTemplate(@Qualifier("jedisFactory")JedisFactory jedisFactory){
        return new JedisTemplate(jedisFactory);
    }
}
