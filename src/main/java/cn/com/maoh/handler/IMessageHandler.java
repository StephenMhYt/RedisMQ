package cn.com.maoh.handler;

/**
 * 消息处理接口，具体的消息处理类需实现此接口
 * Created by maoh on 2017/12/4.
 */
public interface IMessageHandler {

    void handleMessage(String message);
}
