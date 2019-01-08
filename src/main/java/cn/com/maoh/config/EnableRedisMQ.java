package cn.com.maoh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Author: Stephen
 * Create Date: 2019/1/8
 * Version: 1.0
 * Comments:
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Import(RedisQueueConfig.class)
@Configuration
public @interface EnableRedisMQ {
}
