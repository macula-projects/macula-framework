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

package org.maculaframework.boot.core.cache.annotation;

import org.maculaframework.boot.core.cache.support.ExpireMode;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 一级缓存配置项
 *
 * @author yuhao.wang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FirstCache {
    /**
     * 缓存初始Size
     *
     * @return int
     */
    int initialCapacity() default 10;

    /**
     * 缓存最大Size
     *
     * @return int
     */
    int maximumSize() default 5000;

    /**
     * 缓存有效时间
     *
     * @return int
     */
    int expireTime() default 9;

    /**
     * 缓存时间单位
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 缓存失效模式
     *
     * @return ExpireMode
     * @see ExpireMode
     */
    ExpireMode expireMode() default ExpireMode.WRITE;
}
