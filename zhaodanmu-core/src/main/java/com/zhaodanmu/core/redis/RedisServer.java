
package com.zhaodanmu.core.redis;


import com.zhaodanmu.common.utils.Log;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisServer {

    private static RedisManager redisManager;
    private RedisServer() {
    }

    public static void connect(String ip, int port) {
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


    public static RedisManager getManager() {
        return redisManager;
    }


    public static void main(String[] args) {
        RedisServer.connect("127.0.0.1",6379);
        RedisManager manager = RedisServer.getManager();
        manager.zAdd("ml1",100.0,"k1");
    }


}
