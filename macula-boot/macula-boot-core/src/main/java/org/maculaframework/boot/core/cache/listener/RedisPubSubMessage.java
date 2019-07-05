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

package org.maculaframework.boot.core.cache.listener;

import java.io.Serializable;

/**
 * redis pub/sub 消息
 *
 * @author yuhao.wang3
 */
public class RedisPubSubMessage implements Serializable {

    private static final long serialVersionUID = 8934698410016742383L;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存key
     */
    private Object key;

    /**
     * 消息类型
     */
    private RedisPubSubMessageType messageType;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public RedisPubSubMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(RedisPubSubMessageType messageType) {
        this.messageType = messageType;
    }
}