package org.macula.boot.core.cache.setting;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 多级缓存配置项
 *
 * @author yuhao.wang
 */
public class LayeringCacheSetting implements Serializable {

    private static final long serialVersionUID = 3462254862999802337L;

    private static final String SPLIT = "-";
    /**
     * 是否使用一级缓存
     */
    boolean useFirstCache = true;
    /**
     * 内部缓存名，由[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]组成
     */
    private String internalKey;
    /**
     * 描述，数据监控页面使用
     */
    private String depict;
    /**
     * 一级缓存配置
     */
    private FirstCacheSetting firstCacheSetting;

    /**
     * 二级缓存配置
     */
    private SecondaryCacheSetting secondaryCacheSetting;

    public LayeringCacheSetting(FirstCacheSetting firstCacheSetting, SecondaryCacheSetting secondaryCacheSetting,
                                String depict) {
        this.firstCacheSetting = firstCacheSetting;
        this.secondaryCacheSetting = secondaryCacheSetting;
        this.depict = depict;
        internalKey();
    }

    public FirstCacheSetting getFirstCacheSetting() {
        return firstCacheSetting;
    }

    public void setFirstCacheSetting(FirstCacheSetting firstCacheSetting) {
        this.firstCacheSetting = firstCacheSetting;
    }

    public SecondaryCacheSetting getSecondaryCacheSetting() {
        return secondaryCacheSetting;
    }

    public void setSecondaryCacheSetting(SecondaryCacheSetting secondaryCacheSetting) {
        this.secondaryCacheSetting = secondaryCacheSetting;
    }

    public String getInternalKey() {
        return internalKey;
    }

    public void setInternalKey(String internalKey) {
        this.internalKey = internalKey;
    }

    public boolean isUseFirstCache() {
        return useFirstCache;
    }

    public void setUseFirstCache(boolean useFirstCache) {
        this.useFirstCache = useFirstCache;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    @JsonIgnore
    private void internalKey() {
        // 一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间
        StringBuilder sb = new StringBuilder();
        if (firstCacheSetting != null) {
            sb.append(firstCacheSetting.getTimeUnit().toMillis(firstCacheSetting.getExpireTime()));
        }
        sb.append(SPLIT);
        if (secondaryCacheSetting != null) {
            sb.append(secondaryCacheSetting.getTimeUnit().toMillis(secondaryCacheSetting.getExpiration()));
            sb.append(SPLIT);
            sb.append(secondaryCacheSetting.getTimeUnit().toMillis(secondaryCacheSetting.getPreloadTime()));
        }
        internalKey = sb.toString();
    }
}
