package cn.com.maoh.topic;

import cn.com.maoh.template.JedisTemplate;

/**
 * Author: Stephen
 * Create Date: 2018/11/13
 * Version: 1.0
 * Comments:
 */
public class RedisMQTopic {

    private String channel;

    private JedisTemplate jedisTemplate;
    
    public RedisMQTopic(){
    }

    public RedisMQTopic(String channel, JedisTemplate jedisTemplate){
        this.channel = channel;
        this.jedisTemplate = jedisTemplate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public JedisTemplate getJedisTemplate() {
        return jedisTemplate;
    }

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }
}
