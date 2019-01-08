package cn.com.maoh.template;

import org.apache.log4j.Logger;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/16.
 */
public class JedisTemplate {
    public static final String SET_EXPIRE_IF_EXIST = "XX";
    public static final String SET_EXPIRE_IF_NOT_EXIST = "NX";
    public static final String EXPIRE_TIME_SECOND = "EX";
    public static final String EXPIRE_TIME_MILLSECOND = "PX";

    private static final Logger LOGGER = Logger.getLogger(JedisTemplate.class);

    private Pool<Jedis> pool;
    private JedisCluster cluster;

    public JedisTemplate(Pool<Jedis> pool) {
        this.pool = pool;
        this.cluster = null;
    }

    public JedisTemplate(JedisCluster cluster) {
        this.cluster = cluster;
        this.pool = null;
    }

    public JedisTemplate(JedisFactory jedisFactory){
        if( jedisFactory.getRedisNodes().split(JedisFactory.HOST_SPLITTER).length > 1){
            //集群模式
            this.cluster = jedisFactory.createJedisCluster();
            this.pool = null;
        } else {
            //单机模式
            this.cluster = null;
            this.pool = jedisFactory.createJedisPool();
        }
    }

    public String get(String key) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.get(key);
        } finally {
            returnConnection(jedis);
        }
    }

    public String set(String key, String value) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.set(key, value);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * 将 key 中储存的数字值增一
     * @param key
     * @return
     */
    public Long incr(String key){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.incr(key);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * 将 key 中储存的数字值减一
     * @param key
     * @return
     */
    public Long decr(String key){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.decr(key);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * 将 key 所储存的值加上给定的浮点增量值(increment)
     * @param key
     * @param increment
     * @return
     */
    public Long incrBy(String key,long increment){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.incrBy(key,increment);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * key 所储存的值减去给定的减量值（decrement）
     * @param key
     * @param decrement
     * @return
     */
    public Long decrBy(String key,long decrement){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.decrBy(key,decrement);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * redis队列，从左边插入一条消息
     * @param key 队列名
     * @param value 信息
     * @return
     */
    public Long lpush(String key,String value){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.lpush(key,value);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * redis队列，从右边弹出一条信息
     * @param key 队列名
     * @return
     */
    public String rpop(String key){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.rpop(key);
        }finally {
            returnConnection(jedis);
        }
    }

    public List<String> brpop(String key,int timeout){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.brpop(timeout,key);
        }finally {
            returnConnection(jedis);
        }
    }

    public Long llen(String key){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.llen(key);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 返回名称为key的list中start至end之间的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key,Long start,Long end){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.lrange(key,start,end);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 返回list中所有的value
     * @param key
     * @return
     */
    public List<String> lallrange(String key){
        JedisCommands jedis = borrowConnection();
        try{
            Long llen = jedis.llen(key);
            return jedis.lrange(key,0,llen - 1);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 返回名称为key的list中index位置的元素
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key,Long index){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.lindex(key,index);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 截取名称为key的list
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String ltrim(String key,Long start,Long end){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.ltrim(key,start,end);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 给名称为key的list中index位置的元素赋值
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String lset(String key,Long index,String value){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.lset(key,index,value);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * 删除count个key的list中值为value的元素
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(String key,Long count,String value){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.lrem(key,count,value);
        }finally {
            returnConnection(jedis);
        }
    }

    /**
     * @param key
     * @param value
     * @param nxxx  NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.<p>
     * @param expx  EX|PX, expire time units: EX = seconds; PX = milliseconds <p>
     * @param time  expire time in the units of
     * @return
     */
    public String set(String key, String value, String nxxx, String expx, Long time) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.set(key, value, nxxx, expx, time);
        } finally {
            returnConnection(jedis);
        }
    }

    public Long del(String key) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.del(key);
        } finally {
            returnConnection(jedis);
        }
    }

    public String hmset(String key, Map<String, String> values) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.hmset(key, values);

        } finally {
            returnConnection(jedis);
        }
    }

    public Long hset(String key, String skey, String svalue) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.hset(key, skey, svalue);
        } finally {
            returnConnection(jedis);
        }
    }

    public String hget(String key,String field){
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.hget(key,field);
        }finally {
            returnConnection(jedis);
        }
    }

    public boolean hexists(String key,String field){
        JedisCommands jedis = borrowConnection();
        try{
            return jedis.hexists(key,field);
        }finally {
            returnConnection(jedis);
        }
    }

    public Map<String, String> hgetAll(String key) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.hgetAll(key);
        } finally {
            returnConnection(jedis);
        }
    }

    public List<String> hmget(String key, String... k) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.hmget(key, k);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * @param key     rediskey
     * @param seconds 秒
     * @return 剩余过期时间（秒）
     */
    public Long expire(String key, Integer seconds) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.expire(key, seconds);
        } finally {
            returnConnection(jedis);
        }
    }

    public Long ttl(String key) {
        JedisCommands jedis = borrowConnection();
        try {
            return jedis.ttl(key);
        } finally {
            returnConnection(jedis);
        }
    }

    /**
     * 发布消息
     * @param channel
     * @param message
     */
    public void publish(String channel, String message){
        if (isClusterMode()){
            cluster.publish(channel,message);
        }else{
            pool.getResource().publish(channel,message);
        }
    }

    /**
     * 订阅频道
     * @param jedisPubSub
     * @param channel
     */
    public void subscribe(JedisPubSub jedisPubSub, String channel){
        if (isClusterMode()){
            cluster.subscribe(jedisPubSub,channel);
        }else{
            pool.getResource().subscribe(jedisPubSub,channel);
        }
    }

    /**
     * 执行Lua脚本命令
     * @param scripts
     * @param list
     * @param list1
     * @return
     */
    public Long eval(String scripts,List<String> list,List<String> list1){
        if (isClusterMode()){
            JedisClusterScriptingCommands commands = this.cluster;
            try {
                return (Long)commands.eval(scripts, list, list1);
            }finally {
            }
        }else{
            ScriptingCommands commands = this.pool.getResource();
            try {
                return (Long)commands.eval(scripts, list, list1);
            }finally {
                ((Jedis) commands).close();
            }
        }
    }

    private JedisCommands borrowConnection() {
        return isClusterMode() ? this.cluster : this.pool.getResource();
    }

    private void returnConnection(JedisCommands commands) {
        if (isPoolMode() && commands instanceof Jedis) {
            ((Jedis) commands).close();
        }
    }


    private boolean isClusterMode() {
        return this.cluster != null && this.pool == null;
    }

    private boolean isPoolMode() {
        return this.pool != null && this.cluster == null;
    }

    public void destroy() {
        if (isPoolMode()) {
            try {
                this.pool.destroy();
            } catch (Exception ex) {
                LOGGER.warn("Cannot properly close Jedis pool", ex);
            }
            pool = null;
        }
        if (isClusterMode()) {
            try {
                this.cluster.close();
            } catch (Exception ex) {
                LOGGER.warn("Cannot properly close Jedis cluster", ex);
            }
        }
    }


}
