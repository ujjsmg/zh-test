package com.myfirstspring.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisAdapter implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;

    public static void print(int index, Object obj){
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }
    /*
    public static void main(String[] args){
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));

        jedis.set("pv", "100");
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));

        String list = "list";
        for(int i = 0; i < 10; i++){
            jedis.lpush(list, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(list, 0, 10));
        print(4, jedis.llen(list));
        print(5, jedis.linsert(list, BinaryClient.LIST_POSITION.AFTER, "a7", "xx"));
        print(6, jedis.linsert(list, BinaryClient.LIST_POSITION.BEFORE, "a7", "bb"));
        print(7, jedis.lrange(list, 0, 12));

        String userKey = "user";
        jedis.hset(userKey, "name", "Jim");
        jedis.hset(userKey, "age", "20");
        jedis.hset(userKey, "phone", "18717171717");
        print(8, jedis.hget(userKey, "name"));
        print(9, jedis.hgetAll(userKey));
        print(10, jedis.hexists(userKey, "school"));
        print(11, jedis.hkeys(userKey));
        print(12, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "xdu");
        jedis.hsetnx(userKey, "name", "zh");
        print(13, jedis.hgetAll(userKey));

        String like1 = "like1";
        String like2 = "like2";
        for(int i = 0; i < 10; i++){
            jedis.sadd(like1, String.valueOf(i));
            jedis.sadd(like2, String.valueOf(i*2));
        }
        print(14, jedis.smembers(like1));
        print(15, jedis.smembers(like2));
        print(16, jedis.sinter(like1, like2));
        print(17, jedis.sunion(like1, like2));
        print(18, jedis.sdiff(like1, like2));
        print(19 , jedis.sismember(like1, "7"));
        jedis.srem(like1, "5");
        print(20, jedis.smembers(like1));
        jedis.smove(like2, like1, "16");
        print(21, jedis.smembers(like1));
        print(22, jedis.scard(like1));

        String rank = "rankKey";
        jedis.zadd(rank, 70, "Jim");
        jedis.zadd(rank, 80, "Jack");
        jedis.zadd(rank, 90, "Jay");
        jedis.zadd(rank, 60, "Jane");
        jedis.zadd(rank, 50, "Jimmy");
        print(23, jedis.zcard(rank));
        print(24, jedis.zcount(rank, 60, 100));
        print(25, jedis.zscore(rank, "Jimmy"));
        jedis.zincrby(rank, 3, "Jimy");
        print(26, jedis.zscore(rank, "Jimy"));
        print(27, jedis.zcount(rank, 0, 100));
        //低到高前3名
        print(28, jedis.zrange(rank, 0, 2));
        //高到低前3名
        print(29, jedis.zrevrange(rank, 0, 2));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rank, "60", "100")) {
            print(30, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(31, jedis.zrank(rank, "Jay"));
        print(32, jedis.zrevrank(rank, "Jay"));
    }
    */
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        }
        catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }
        finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }
        catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }
        finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        }
        catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return false;
        }
        finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }
        catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            return 0;
        }
        finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object obj){
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> tClass){
        String value = get(key);
        if(value != null)
            return JSON.parseObject(value, tClass);
        return null;
    }
}
