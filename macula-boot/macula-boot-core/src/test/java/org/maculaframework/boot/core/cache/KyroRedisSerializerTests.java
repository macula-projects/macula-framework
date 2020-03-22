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

package org.maculaframework.boot.core.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maculaframework.boot.core.cache.support.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * <b>KyroRedisSerializerTests</b> KryoRedisSerializer测试
 * </p>
 *
 * @author Rain
 * @since 2020-03-15
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KyroRedisSerializerTests.class)
@SpringBootConfiguration
@ComponentScan
public class KyroRedisSerializerTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testPutGetUser() {
        User user = new User();
        String key = "user:123";
        redisTemplate.opsForValue().set(key, user);
        User userCache = (User)redisTemplate.opsForValue().get(key);
        Assert.assertEquals(user.getUserId(), userCache.getUserId());
        Assert.assertEquals(user.getAddress().getAddredd(), userCache.getAddress().getAddredd());
    }
}
