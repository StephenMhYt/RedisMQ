package cn.com.maoh.topic;

import cn.com.maoh.template.JedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;


/**
 * 由于redis的订阅方法subscribe是线程阻塞的，故另启一个线程订阅消息
 * Created by maoh on 2017/12/3.
 */
public class RedisMQTopicSubscribeThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMQTopicSubscribeThread.class);

    private JedisPubSub subscriber;

    private String channel;

    private JedisTemplate jedisTemplate;

    public RedisMQTopicSubscribeThread(JedisPubSub subscriber, String channel, JedisTemplate jedisTemplate){
        this.subscriber = subscriber;
        this.channel = channel;
        this.jedisTemplate = jedisTemplate;
    }

    @Override
    public void run(){
        //订阅channel频道,
        // while(true)是因为,当subscribe方法报错（如网络问题连接超时），则一直重复连接，直到正常成功订阅
        while (true) {
            try {
                jedisTemplate.subscribe(subscriber, channel);
            }catch (Exception e){
                LOGGER.info("error"+e.getMessage()+",restart to subscribe");
            }
        }
    }
}
