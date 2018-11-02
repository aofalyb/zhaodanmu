
package com.zhaodanmu.core.redis;


import com.zhaodanmu.common.utils.Log;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisServer {

    private String ip;
    private int port;
    boolean isCluster;

    private static RedisManager redisManager;
    public RedisServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public void sync() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, ip, port);
        redisManager = new RedisManager(jedisPool);
        Log.sysLogger.info("checking redis active:[{}]",ip + ":" + port);
        try(Jedis resource = jedisPool.getResource()) {
            if(resource != null) {
                resource.ping();
                Log.sysLogger.info("redis server:[{}] connect success",ip + ":" + port);
            }
        } catch (Exception e) {
            Log.sysLogger.error("redis server:[{}] connect failed!",ip + ":" + port,e);
            throw new RuntimeException(e);
        }

    }


    public RedisManager getManager() {
        return redisManager;
    }


}
