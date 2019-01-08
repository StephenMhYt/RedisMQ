package cn.com.maoh.serialize.file;

import cn.com.maoh.constants.MsgStatusEnum;
import cn.com.maoh.constants.MsgTypeEnum;
import cn.com.maoh.serialize.ISerializer;
import cn.com.maoh.serialize.MsgSerializeVO;
import cn.com.maoh.template.JedisFactory;
import cn.com.maoh.template.JedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author: Stephen
 * Create Date: 2018/12/4
 * Version: 1.0
 * Comments:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:SpringContext.xml")
public class FileSerializerTest {

    @Autowired
    private JedisTemplate jedisTemplate;

    @Test
    public void serialize() throws Exception {
        long start = System.currentTimeMillis();
        MsgSerializeVO vo = new MsgSerializeVO();
        vo.setMsgBody("this is test1");
        vo.setMsgId(UUID.randomUUID().toString());
        vo.setStatus(MsgStatusEnum.UNPROCESS.value());
        vo.setMsgType(MsgTypeEnum.QUEUE.value());
        vo.setQueueName("thecover.queue");
        MsgSerializeVO vo1 = new MsgSerializeVO();
        vo1.setMsgBody("this is test2");
        vo1.setMsgId(UUID.randomUUID().toString());
        vo1.setStatus(MsgStatusEnum.UNPROCESS.value());
        vo1.setMsgType(MsgTypeEnum.TOPIC.value());
        vo1.setChannel("thecover.channel");
        ISerializer serializer = new FileSerializer(jedisTemplate);
        serializer.serialize(vo);
        serializer.serialize(vo1);
        System.out.println("cost time:"+(System.currentTimeMillis() - start));
        serializer.deserialize(MsgSerializeVO.class);
        Thread.sleep(10000);
    }

    @Test
    public void deserialize() throws Exception {

    }

}