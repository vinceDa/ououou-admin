/*
package com.ou.common.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

*/
/**
 *  redis的String/Value的默认序列化方式
 *  解决问题: java代码中的key: secretKey设置到redis中查看变成\xac\xed\x00\x05t\x00\tsecretKey 类似的问题
 * @author vince
 * @date 2019/12/18 17:16
 *//*

public class DefaultStrSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    public DefaultStrSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public DefaultStrSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return o == null ? null : String.valueOf(o).getBytes(charset);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return bytes == null ? null : new String(bytes, charset);

    }
}
*/
