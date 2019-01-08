package cn.com.maoh.thread;

import cn.com.maoh.template.JedisTemplate;
import cn.com.maoh.topic.RedisMQTopic;
import cn.com.maoh.topic.RedisMQTopicTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class RedisMQAbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMQAbortPolicyWithReport.class);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            if (r instanceof RedisMQTopicTask){
                RedisMQTopicTask task = (RedisMQTopicTask)r;
                RedisMQTopic redisMQTopic = task.getTopic();
                JedisTemplate jedisTemplate = redisMQTopic.getJedisTemplate();
                String channel = redisMQTopic.getChannel();
                String message = task.getMessage();
                jedisTemplate.publish(channel,message);
            }else {
                String msg = String.format("Thread pool is EXHAUSTED!" +
                                " Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                                " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)",
                        e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                        e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
                LOGGER.warn(msg);
                throw new RejectedExecutionException(msg);
            }
        }

    }