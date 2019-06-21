/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.maculaframework.boot.core.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 由于@SpringBootTest会扫描test目录下的XXXRepository类，所以必须要引入JPA的配置，否则会出现找不到EntityManager等问题
 *
 * @author Rain
 * @TestConfiguration 如果标识在顶层类上，这个配置是不会被@SpringBooTest注解扫描的，因为加了@SpringBootApplication上加了TypeExcludeFilter
 * 所以这里需要通过@Import引入
 * @since 2019-1-31
 */
@RunWith(SpringRunner.class)
// 避开其他目录的类被加载，这里只做CoreAutoConfiguration的配置检查
@SpringBootTest
@SpringBootApplication
public class CoreAutoConfigurationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testCacheRedisTemplate() {
        Assert.assertNotNull(applicationContext.getBean("cacheRedisTemplate"));
    }
}