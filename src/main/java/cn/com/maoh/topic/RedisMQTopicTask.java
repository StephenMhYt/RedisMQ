package cn.com.maoh.topic;

import cn.com.maoh.handler.IMessageHandler;

/**
 * Created by maoh on 2017/12/4.
 */
public class RedisMQTopicTask implements Runnable {

    private IMessageHandler handler;

    private String message;

    private RedisMQTopic topic;

    public RedisMQTopicTask(IMessageHandler handler,RedisMQTopic topic, String message){
        this.handler = handler;
        this.topic = topic;
        this.message = message;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        execute(handler, message);
    }

    private void execute(IMessageHandler handler, String message){
        //消息处理
        handler.handleMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public RedisMQTopic getTopic() {
        return topic;
    }
}
