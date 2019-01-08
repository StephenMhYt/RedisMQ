package cn.com.maoh.queue;

import cn.com.maoh.template.JedisTemplate;

/**
 * Author: Stephen
 * Create Date: 2019/1/7
 * Version: 1.0
 * Comments:
 */
public class RedisMQQueue {

    private String name;

    private JedisTemplate jedisTemplate;

    public RedisMQQueue(String name,JedisTemplate jedisTemplate){
        this.name = name;
        this.jedisTemplate = jedisTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JedisTemplate getJedisTemplate() {
        return jedisTemplate;
    }

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }
}
