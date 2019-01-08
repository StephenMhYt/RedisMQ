package cn.com.maoh.handler.impl;

import cn.com.maoh.handler.IMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by maoh on 2017/12/4.
 */
public class DefaultMessageHandler implements IMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public void handleMessage(String message) {
        //do something
        LOGGER.info("handle message:{}",message);
    }
}
