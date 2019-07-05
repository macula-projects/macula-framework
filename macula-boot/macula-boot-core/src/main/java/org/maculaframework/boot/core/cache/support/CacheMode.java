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

package org.maculaframework.boot.core.cache.support;

/**
 * 缓存模式
 *
 * @author yuhao.wang3
 */
public enum CacheMode {
    /**
     * 只开启一级缓存
     */
    ONLY_FIRST("只是用一级缓存"),

    /**
     * 只开启二级缓存
     */
    ONLY_SECOND("只是使用二级缓存"),

    /**
     * 同时开启一级缓存和二级缓存
     */
    ALL("同时开启一级缓存和二级缓存");

    private String label;

    CacheMode(String label) {
        this.label = label;
    }
}
