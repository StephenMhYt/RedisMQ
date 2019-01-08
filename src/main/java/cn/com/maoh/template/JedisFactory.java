package cn.com.maoh.template;

import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于创建jedis连接池<p>
 * 连接地址格式：x.x.x.x:port<p>
 * Created by maoh on 2017/12/04.
 */
public class JedisFactory {

    public static final String HOST_SPLITTER = ",";
    public static final String PORT_SPLITTER = ":";


    private String database = "0";
    private String password = null;
    private String redisMaster = "";
    private String redisSentinels = "";
    private String redisClusterNodes = "";



    private String redisNodes = "";
    private Boolean testWhileIdle = true;
    private Integer maxTotal = 30;
    private Integer maxIdle = 20;
    private Integer minIdle = 10;
    private Integer connectionTimeout = 2000;
    private Integer minEvictableIdleTimeMillis = 60000;
    private Integer timeBetweenEvictionRunsMillis = 30000;
    private Integer clusterMaxRedirects = 5;
    private JedisPoolConfig jedisPoolConfig;

    /**
     * 需要配置redisMaster，无需配置redisSentinels 和 redisClusterNodes，其他选配<p>
     *
     * @return 单机jedis连接池
     */
    public JedisPool createJedisPool() {
        redisMaster = redisNodes.split(HOST_SPLITTER)[0];
        String[] hostAndPort = redisMaster.split(PORT_SPLITTER);
        return new JedisPool(buildJedisPoolConfig(), hostAndPort[0], Integer.parseInt(hostAndPort[1]), connectionTimeout, password, 0, null);
    }

    /**
     * 需要配置redisMaster和redisSentinels，无需配置redisClusterNodes。<p>
     *
     * @return
     */
    public JedisSentinelPool createJedisSentinelPool() {
        String[] sentinels = redisSentinels.split(HOST_SPLITTER);
        Set<String> sentinelSet = new HashSet<String>();
        for (String sentinel : sentinels) {
            sentinelSet.add(sentinel);
        }
        return new JedisSentinelPool(redisMaster, sentinelSet, buildJedisPoolConfig(), connectionTimeout, password);
    }

    /**
     * 需要配置redisClusterNodes，无需配置redisMaster和redisSentinels。<p>
     *
     * @return
     */
    public JedisCluster createJedisCluster() {
        redisClusterNodes = redisNodes;
        Set<HostAndPort> hostAndPort = new HashSet<HostAndPort>();
        String[] hostAndPorts = redisClusterNodes.split(HOST_SPLITTER);
        for (String hp : hostAndPorts) {
            String[] hpArr = hp.split(PORT_SPLITTER);
            hostAndPort.add(new HostAndPort(hpArr[0], Integer.parseInt(hpArr[1])));
        }

        int redirects = clusterMaxRedirects;

        return new JedisCluster(hostAndPort, connectionTimeout, redirects, buildJedisPoolConfig());
    }


    public JedisPoolConfig buildJedisPoolConfig() {
        if (jedisPoolConfig == null) {
            jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        return jedisPoolConfig;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public String getRedisMaster() {
        return redisMaster;
    }

    public String getRedisSentinels() {
        return redisSentinels;
    }

    public void setRedisSentinels(String redisSentinels) {
        this.redisSentinels = redisSentinels;
    }

    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Integer getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getRedisClusterNodes() {
        return redisClusterNodes;
    }

    public Integer getClusterMaxRedirects() {
        return clusterMaxRedirects;
    }

    public void setClusterMaxRedirects(Integer clusterMaxRedirects) {
        this.clusterMaxRedirects = clusterMaxRedirects;
    }

    public String getRedisNodes() {
        return redisNodes;
    }

    public void setRedisNodes(String redisNodes) {
        this.redisNodes = redisNodes;
    }
}
