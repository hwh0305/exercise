package org.hao.test.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> implements KVCache<K, V> {

    private final Map<K, V>  map;

    private final int        capacity;

    private final Lock       lock  = new ReentrantLock();

    private final AtomicLong hits  = new AtomicLong();

    private final AtomicLong total = new AtomicLong();

    public LRUCache(final int capacity){
        this.capacity = capacity;
        map = new LinkedHashMap<K, V>(capacity, 0.75f, true) {

            private static final long serialVersionUID = 5771523884708637378L;

            @Override
            protected boolean removeEldestEntry(Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        try {
            lock.lock();
            map.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        V value = null;
        try {
            lock.lock();
            value = map.get(key);
        } finally {
            lock.unlock();
        }
        total.incrementAndGet();
        if (value != null) hits.incrementAndGet();
        return value;
    }

    @Override
    public V remove(K key) {
        V value = null;
        try {
            lock.lock();
            value = map.remove(key);
        } finally {
            lock.unlock();
        }
        return value;
    }

    public float getHitRate() {
        return (float) hits.get() / (float) total.get();
    }

    public int getCapacity() {
        return capacity;
    }

    public static void main(String[] args) {
        final KVCache<Long, Long> cache = new LRUCache<Long, Long>(1000);
        // ExecutorService pool = Executors.newFixedThreadPool(200);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; ++i) {
            Random rand = new Random();
            long seed = Math.abs(rand.nextLong());
            long kv = seed & 1023;
            cache.put(kv, kv);
        }
        System.out.println(System.currentTimeMillis() - start);
        for (int i = 0; i < 10000000; ++i) {
            Random rand = new Random();
            long seed = Math.abs(rand.nextLong());
            long key = seed & 1023;
            cache.get(key);
        }
        System.out.println(((LRUCache<Long, Long>) cache).getHitRate());
        System.out.println(System.currentTimeMillis() - start);
    }
}
