package cn.com.maoh.topic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Author: Stephen
 * Create Date: 2019/1/8
 * Version: 1.0
 * Comments:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:SpringContext.xml")
public class RedisMQTopicMessageListenerContainerTest {


    @Test
    public void onSubscribe() throws Exception {
        System.out.println("test");
    }

    @Test
    public void onUnsubscribe() throws Exception {

    }

}