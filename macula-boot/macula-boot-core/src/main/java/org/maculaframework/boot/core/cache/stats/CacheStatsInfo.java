/*
 * Copyright 2004-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maculaframework.boot.core.cache.stats;

import org.maculaframework.boot.core.cache.setting.LayeringCacheSetting;

import java.io.Serializable;

/**
 * 缓存命中率统计实体类
 *
 * @author yuhao.wang3
 */
public class CacheStatsInfo implements Serializable {

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 内部缓存名，由[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]组成
     */
    private String internalKey;

    /**
     * 描述,数据监控页面使用
     */
    private String depict;

    /**
     * 总请求总数
     */
    private long requestCount;

    /**
     * 总未命中总数
     */
    private long missCount;

    /**
     * 命中率
     */
    private double hitRate;

    /**
     * 一级缓存命中总数
     */
    private long firstCacheRequestCount;

    /**
     * 一级缓存未命中总数
     */
    private long firstCacheMissCount;

    /**
     * 二级缓存命中总数
     */
    private long secondCacheRequestCount;

    /**
     * 二级缓存未命中总数
     */
    private long secondCacheMissCount;

    /**
     * 总的请求时间
     */
    private long totalLoadTime;

    /**
     * 缓存配置
     */
    private LayeringCacheSetting layeringCacheSetting;


    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public long getMissCount() {
        return missCount;
    }

    public void setMissCount(long missCount) {
        this.missCount = missCount;
    }

    public long getFirstCacheRequestCount() {
        return firstCacheRequestCount;
    }

    public void setFirstCacheRequestCount(long firstCacheRequestCount) {
        this.firstCacheRequestCount = firstCacheRequestCount;
    }

    public long getFirstCacheMissCount() {
        return firstCacheMissCount;
    }

    public void setFirstCacheMissCount(long firstCacheMissCount) {
        this.firstCacheMissCount = firstCacheMissCount;
    }

    public long getSecondCacheRequestCount() {
        return secondCacheRequestCount;
    }

    public void setSecondCacheRequestCount(long secondCacheRequestCount) {
        this.secondCacheRequestCount = secondCacheRequestCount;
    }

    public long getSecondCacheMissCount() {
        return secondCacheMissCount;
    }

    public void setSecondCacheMissCount(long secondCacheMissCount) {
        this.secondCacheMissCount = secondCacheMissCount;
    }

    public long getTotalLoadTime() {
        return totalLoadTime;
    }

    public void setTotalLoadTime(long totalLoadTime) {
        this.totalLoadTime = totalLoadTime;
    }

    public String getInternalKey() {
        return internalKey;
    }

    public void setInternalKey(String internalKey) {
        this.internalKey = internalKey;
    }

    public LayeringCacheSetting getLayeringCacheSetting() {
        return layeringCacheSetting;
    }

    public void setLayeringCacheSetting(LayeringCacheSetting layeringCacheSetting) {
        this.layeringCacheSetting = layeringCacheSetting;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public double getHitRate() {
        return hitRate;
    }

    public void setHitRate(double hitRate) {
        this.hitRate = hitRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheStatsInfo that = (CacheStatsInfo) o;

        if (cacheName != null ? !cacheName.equals(that.cacheName) : that.cacheName != null) {
            return false;
        }
        return internalKey != null ? internalKey.equals(that.internalKey) : that.internalKey == null;
    }

    @Override
    public int hashCode() {
        int result = cacheName != null ? cacheName.hashCode() : 0;
        result = 31 * result + (internalKey != null ? internalKey.hashCode() : 0);
        return result;
    }

    /**
     * 清空统计信息
     */
    public void clearStatsInfo() {
        this.setRequestCount(0);
        this.setMissCount(0);
        this.setTotalLoadTime(0);
        this.setHitRate(0);

        this.setFirstCacheRequestCount(0);
        this.setFirstCacheMissCount(0);

        this.setSecondCacheRequestCount(0);
        this.setSecondCacheMissCount(0);
    }
}