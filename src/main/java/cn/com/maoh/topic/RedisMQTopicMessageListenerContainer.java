package cn.com.maoh.topic;

import cn.com.maoh.constants.RedisMQConstant;
import cn.com.maoh.handler.IMessageHandler;
import cn.com.maoh.template.JedisTemplate;
import cn.com.maoh.thread.RedisMQAbortPolicyWithReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * 由于在分布式项目中，多台订阅者都会收到发布者发布的消息，故利用redis的list数据结构，只会有一台rpop到相应的具体消息。
 * 使用时需在spring配置文件中配置订阅者bean，并填充相应属性
 *
 * Created by maoh on 2017/12/4.
 */
public class RedisMQTopicMessageListenerContainer extends JedisPubSub implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMQTopicMessageListenerContainer.class);

    private IMessageHandler messageHandler;

    private int corePoolSize=3;

    private int maximumPoolSize=5;

    private long keepAliveTime=100L;

    private ThreadPoolExecutor pool;

    private ExecutorService executor;

    private RedisMQTopic redisMQTopic;

    @Override
    public final void onMessage(String channel, String message) {
        //订阅者接收到发布消息
        pool.execute(new RedisMQTopicTask(messageHandler,redisMQTopic, message));
    }

    public void onSubscribe(String channel, int subscribedChannels) {
        LOGGER.info("subscribe redis channel success, channel:{}, subscribedChannels:{}", channel, subscribedChannels);
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        LOGGER.info("unsubscribe redis channel, channel:{}, subscribedChannels:{}", channel, subscribedChannels);
    }

    @PostConstruct
    public void init(){
        //创建线程池对象
        pool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new RedisMQAbortPolicyWithReport());

        executor = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    public void destroy(){
        executor.shutdown();
        pool.shutdown();//shutdown()是已经提交的任务执行完毕后关闭，shutdownNow()是尚未执行的任务全部取消，正在执行的发出interrupt()
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkParam();
        JedisTemplate jedisTemplate = redisMQTopic.getJedisTemplate();
        String channel = redisMQTopic.getChannel();
        //记录订阅者+1
        jedisTemplate.incr(RedisMQConstant.REDIS_SUB_NUM_REDIS_KEY_PREFIX+channel);
        //由于redis的订阅方法subscribe是线程阻塞的，故另启一个线程订阅消息
        RedisMQTopicSubscribeThread thread = new RedisMQTopicSubscribeThread(this,channel,jedisTemplate);
        executor.execute(thread);
    }

    private void checkParam(){
        if (redisMQTopic == null){
            throw new RuntimeException("the redisMQTopic cannot be null");
        }
        if (StringUtils.isEmpty(redisMQTopic.getChannel())){
            throw new RuntimeException("the subscribe channel cannot be null");
        }
        if (redisMQTopic.getJedisTemplate() == null){
            throw new RuntimeException("the redis template cannot be null");
        }
        if (messageHandler == null){
            throw new RuntimeException("the message handler cannot be null");
        }
    }

    public IMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public RedisMQTopic getRedisMQTopic() {
        return redisMQTopic;
    }

    public void setRedisMQTopic(RedisMQTopic redisMQTopic) {
        this.redisMQTopic = redisMQTopic;
    }

    public ThreadPoolExecutor getPool() {
        return pool;
    }

    public void setPool(ThreadPoolExecutor pool) {
        this.pool = pool;
    }

}