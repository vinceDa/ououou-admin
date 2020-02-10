package com.ou.gateway.util;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * @author vince
 * @date 2019/12/18 16:18
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     *  过期时间
     */
    private final static long EXPIRATION = 1000 * 60 * 5;

    /**
     *  过期前1分钟
     */
    private final static long LAST_TIME = 1000 * 60;

    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = new HashSet<>(keys);
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    /**
     *  设置值
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String create(String key){
        if(StrUtil.isBlank(key)){
            return null;
        }
        String secretKey;
        if(redisTemplate.hasKey(key)){
            if(redisTemplate.boundHashOps(key).getExpire() < LAST_TIME){
                redisTemplate.opsForValue().set(key, SecureUtil.md5(UUID.randomUUID().toString()), EXPIRATION, TimeUnit.SECONDS);
            }
            secretKey = (String) redisTemplate.opsForValue().get(key);
        }else{
            secretKey = SecureUtil.md5(UUID.randomUUID().toString());
            redisTemplate.opsForValue().set(key,secretKey, EXPIRATION,TimeUnit.SECONDS);
        }
        return secretKey;
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

}
