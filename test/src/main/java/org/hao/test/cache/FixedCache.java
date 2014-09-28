package org.hao.test.cache;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FixedCache<K, V extends Serializable> implements KVCache<K, V> {

    private final static int       BIT  = 1;

    private final static int       KB   = BIT * 1024;

    private final static int       KB_4 = 4 * KB;

    private ByteBuffer             buf;

    private final Map<K, Position> pos  = new HashMap<K, Position>();

    public FixedCache(int bytes){
        initBuffer(bytes);
    }

    @Override
    public void put(K key, V value) {
        int position = buf.position();
        byte[] vals = serialize(value);
        if (position + vals.length > buf.limit()) return;
        buf.put(vals);
        pos.put(key, new Position(position, vals.length));
    }

    @Override
    public V get(K key) {
        buf.flip();
        Position position = pos.get(key);
        V value = null;
        if (position != null) {
            byte[] vals = new byte[position.getLen()];
            buf.position(position.getPos());
            buf.get(vals);
            value = deserialize(vals);
        }
        buf.position(buf.limit());
        buf.limit(buf.capacity());
        return value;
    }

    @Override
    public V remove(K key) {
        V result = get(key);
        pos.remove(key);
        return result;
    }

    private byte[] serialize(V value) {
        if (value != null) {
            return JSON.toJSONBytes(value, SerializerFeature.NotWriteRootClassName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private V deserialize(byte[] buf) {
        if (buf != null) {
            Object o = JSON.parse(buf, Feature.SortFeidFastMatch);
            return (V) o;
        }
        return null;
    }

    private void initBuffer(int bytes) {
        int mod = bytes / KB_4;
        int size = (mod + 1) * KB_4;
        buf = ByteBuffer.allocateDirect(size);
    }

    @Data
    @AllArgsConstructor
    private class Position {

        int pos;
        int len;
    }

    public static void main(String[] args) {
        KVCache<Integer, Long> cache = new FixedCache<Integer, Long>(1);
        cache.put(1, 1L);
        cache.put(2, 2L);
        System.out.println(cache.get(2));
        System.out.println(cache.get(1));
        cache.put(3, 3L);
        System.out.println(cache.get(2));
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
    }
}
