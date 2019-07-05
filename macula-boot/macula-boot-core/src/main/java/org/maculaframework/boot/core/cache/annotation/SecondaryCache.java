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

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 二级缓存配置项
 *
 * @author yuhao.wang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SecondaryCache {
    /**
     * 缓存有效时间
     *
     * @return long
     */
    long expireTime() default 5;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 建议是： preloadTime = expireTime * 0.2
     *
     * @return long
     */
    long preloadTime() default 1;

    /**
     * 时间单位 {@link TimeUnit}
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.HOURS;

    /**
     * 是否强制刷新（直接执行被缓存方法），默认是false
     *
     * @return boolean
     */
    boolean forceRefresh() default false;

    /**
     * 是否允许缓存NULL值
     *
     * @return boolean
     */
    boolean isAllowNullValue() default false;

    /**
     * 非空值和null值之间的时间倍率，默认是1。isAllowNullValue=true才有效
     * <p>
     * 如配置缓存的有效时间是200秒，倍率这设置成10，
     * 那么当缓存value为null时，缓存的有效时间将是20秒，非空时为200秒
     * </p>
     *
     * @return int
     */
    int magnification() default 1;
}
