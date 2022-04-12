package com.tdeado.core.cache.impl;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import com.tdeado.core.cache.CacheService;

/**
 * 缓存接口
 */

public class MemoryCacheService implements CacheService<String,Object>{

    Cache<String,Object> fifoCache = CacheUtil.newFIFOCache(3);

    @Override
    public boolean set(String key, Object value) {
        fifoCache.put(key,value);
        return true;
    }

    @Override
    public boolean set(String key, Object value, long timeout) {
        fifoCache.put(key,value,timeout);
        return true;
    }

    @Override
    public Object get(String key) {
        return fifoCache.get(key);
    }

    @Override
    public boolean remove(String key) {
        fifoCache.remove(key);
        return true;
    }
}
