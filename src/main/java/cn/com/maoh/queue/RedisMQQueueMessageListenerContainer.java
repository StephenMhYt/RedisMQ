package cn.com.maoh.queue;

import cn.com.maoh.handler.IMessageHandler;
import cn.com.maoh.template.JedisTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Author: Stephen
 * Create Date: 2019/1/7
 * Version: 1.0
 * Comments:
 */
public class RedisMQQueueMessageListenerContainer implements InitializingBean{

    private RedisMQQueue destination;

    private IMessageHandler messageHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        checkParam();
        init();
    }

    private void init(){
        JedisTemplate jedisTemplate = destination.getJedisTemplate();
        String queueName = destination.getName();
        while (true){
            List<String> list = jedisTemplate.brpop(queueName, 10);
            if (!CollectionUtils.isEmpty(list)){
                if (list.size() != 2 || !queueName.equals(list.get(0))){
                    throw new RuntimeException("brpop result list is not valid");
                }
                String message = list.get(1);
                messageHandler.handleMessage(message);
            }
        }
    }

    private void checkParam(){
        if (destination == null){
            throw new RuntimeException("the redisMQQueue cannot be null");
        }
        if (StringUtils.isEmpty(destination.getName())){
            throw new RuntimeException("the subscribe queue name cannot be null");
        }
        if (destination.getJedisTemplate() == null){
            throw new RuntimeException("the redis template cannot be null");
        }
        if (messageHandler == null){
            throw new RuntimeException("the message handler cannot be null");
        }
    }

    public void setDestination(RedisMQQueue destination) {
        this.destination = destination;
    }

    public void setMessageHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
