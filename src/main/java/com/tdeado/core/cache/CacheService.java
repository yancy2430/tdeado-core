package com.tdeado.core.cache;


/**
 * 缓存接口
 */
public interface CacheService<K,V> {
    boolean set(K key, V value);

    boolean set(K key, V value, long timeout);

    V get(K var1);

    boolean remove(K key);

}
