package cn.com.maoh.producer;

import cn.com.maoh.constants.RedisMQConstant;
import cn.com.maoh.template.JedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by maoh on 2017/12/4.
 */
public class RedisSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSender.class);

    private JedisTemplate jedisTemplate;

    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);

    private Integer delay = 1;

    private static final ConcurrentMap<String,String> MESSAGE_MAP = new ConcurrentHashMap();

    private final Object monitor = new Object();

    /**
     * 判断当前订阅者数量是否超过1，若无订阅者，则先将消息放入队列中
     * @param channel
     * @param message
     */
    public void topicSend(String channel,String message){
        if (haveSubscriber(channel)){
            jedisTemplate.publish(channel,message);
        }else{
            MESSAGE_MAP.put(channel,message);
            ses.scheduleAtFixedRate(new Worker(),delay,delay,TimeUnit.SECONDS);
        }
    }

    /**
     * 发送queue消息，左进右出
     * @param queueName
     * @param message
     */
    public void queueSend(String queueName,String message){
        jedisTemplate.lpush(queueName,message);
    }

    private class Worker extends Thread {

        @Override
        public void run() {
            LOGGER.info("start...");
            if (CollectionUtils.isEmpty(MESSAGE_MAP)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("no message to publish");
                }
                return;
            }
            synchronized (monitor) {
                for (Map.Entry<String, String> entry : MESSAGE_MAP.entrySet()) {
                    if (haveSubscriber(entry.getKey())) {
                        String channel = entry.getKey();
                        String message = MESSAGE_MAP.remove(channel);
                        jedisTemplate.publish(channel, message);
                        LOGGER.info("publish channel:{},message:{}", entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }

    /**
     * 判断该频道是否已有订阅者
     * @return
     */
    private boolean haveSubscriber(String channel){
        String subscribers = jedisTemplate.get(RedisMQConstant.REDIS_SUB_NUM_REDIS_KEY_PREFIX + channel);
        return !StringUtils.isEmpty(subscribers) && Integer.parseInt(subscribers) > 0;
    }

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public ScheduledExecutorService getSes() {
        return ses;
    }

    public void setSes(ScheduledExecutorService ses) {
        this.ses = ses;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
