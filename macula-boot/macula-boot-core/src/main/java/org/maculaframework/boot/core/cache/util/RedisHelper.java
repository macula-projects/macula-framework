/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.core.cache.util;

import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * redis扩展工具
 *
 * @author yuhao.wang3
 * @since 2020/2/21 23:35
 */
public abstract class RedisHelper {
    /**
     * scan 实现
     *
     * @param redisTemplate redisTemplate
     * @param pattern       表达式
     */
    public static Set<String> scan(RedisTemplate<String, Object> redisTemplate, String pattern) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            if (connection instanceof RedisClusterConnection) {
                //集群模式
                return redisTemplate.keys(pattern);
            }

            // 单机模式
            Set<String> keysTmp = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                .match(pattern)
                .count(10000).build())) {

                while (cursor.hasNext()) {
                    keysTmp.add(new String(cursor.next(), "Utf-8"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return keysTmp;
        });
    }
}
