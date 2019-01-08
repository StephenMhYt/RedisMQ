package cn.com.maoh.serialize.kryo;

import cn.com.maoh.constants.MsgStatusEnum;
import cn.com.maoh.serialize.ISerializer;
import cn.com.maoh.serialize.MsgSerializeVO;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author: Stephen
 * Create Date: 2018/12/3
 * Version: 1.0
 * Comments:
 */
public class KryoSerializerTest {

    @Test
    public void serialize() throws Exception {
        long start = System.currentTimeMillis();
        MsgSerializeVO vo = new MsgSerializeVO();
        vo.setMsgBody("this is test1");
        vo.setMsgId(UUID.randomUUID().toString());
        vo.setStatus(MsgStatusEnum.UNPROCESS.value());
        MsgSerializeVO vo1 = new MsgSerializeVO();
        vo1.setMsgBody("this is test2");
        vo1.setMsgId(UUID.randomUUID().toString());
        vo1.setStatus(MsgStatusEnum.UNPROCESS.value());
        ISerializer serializer = new KryoSerializer();
        serializer.serialize(vo);
        serializer.serialize(vo1);
        System.out.println("cost time:"+(System.currentTimeMillis() - start));
        MsgSerializeVO deserialize = serializer.deserialize(MsgSerializeVO.class);
        System.out.println(deserialize.getMsgId());
        System.out.println(deserialize.getMsgBody());
        System.out.println(deserialize.getStatus());
    }

    @Test
    public void deserialize() throws Exception {

    }

}