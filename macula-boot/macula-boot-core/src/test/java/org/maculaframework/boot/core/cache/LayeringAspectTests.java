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

package org.maculaframework.boot.core.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maculaframework.boot.core.cache.manager.CacheManager;
import org.maculaframework.boot.core.cache.manager.LayeringCacheManager;
import org.maculaframework.boot.core.cache.support.CacheMode;
import org.maculaframework.boot.core.cache.support.domain.User;
import org.maculaframework.boot.core.cache.support.test.TestCacheService;
import org.maculaframework.boot.core.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LayeringAspectTests.class)
@SpringBootConfiguration
@ComponentScan
public class LayeringAspectTests {
    private Logger logger = LoggerFactory.getLogger(LayeringAspectTests.class);

    @Autowired
    private TestCacheService testCacheService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testGetUserNameLongParam() {
        long userId = 111;

        User user = testCacheService.getUserById(userId);
        user = testCacheService.getUserById(userId);
        sleep(5);
        user = testCacheService.getUserById(userId);
        sleep(4);
        user = testCacheService.getUserById(userId);
        sleep(10);

        Object result = redisTemplate.opsForValue().get("user:info:113:113");
        Assert.assertNull(result);

        user = testCacheService.getUserById(userId);
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetUserNameArrayAndLongParam() {
        String[] lastName = {"w", "y", "h"};
        long userId = 122;

        User user = testCacheService.getUserNoKey(userId, lastName);
        user = testCacheService.getUserNoKey(userId, lastName);
        sleep(5);
        user = testCacheService.getUserNoKey(userId, lastName);
        sleep(4);
        user = testCacheService.getUserNoKey(userId, lastName);
        sleep(10);
        Object result = redisTemplate.opsForValue().get("user:info:113:113");
        Assert.assertNull(result);

        user = testCacheService.getUserNoKey(userId, lastName);
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetUserNameObjectParam() {
        User user = new User();
        user.setUserId(113);
        user.setAge(31);
        user.setLastName(new String[]{"w", "y", "h"});

        user = testCacheService.getUserObjectPram(user);
        user = testCacheService.getUserObjectPram(user);
        sleep(5);
        user = testCacheService.getUserObjectPram(user);
        sleep(4);
        user = testCacheService.getUserObjectPram(user);
        sleep(11);
        Object result = redisTemplate.opsForValue().get("user:info:113:113");
        Assert.assertNull(result);

        user = testCacheService.getUserObjectPram(user);
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetUserNameObjectAndIntegerParam() {
        User user = new User();
        user.setUserId(114);
        user.setAge(31);
        user.setLastName(new String[]{"w", "y", "h"});

        testCacheService.getUser(user, user.getAge());
        user = testCacheService.getUser(user, user.getAge());
        Assert.assertNotNull(user);
        sleep(5);
        user = testCacheService.getUser(user, user.getAge());
        sleep(4);
        user = testCacheService.getUser(user, user.getAge());
        sleep(11);
        Object result = redisTemplate.opsForValue().get("user:info:114:114");
        Assert.assertNull(result);

        user = testCacheService.getUser(user, user.getAge());
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetNullUser() {
        long userId = 115;

        testCacheService.getNullUser(userId);
        User user = testCacheService.getNullUser(userId);
        Assert.assertNull(user);

        sleep(5);
        user = testCacheService.getNullUser(userId);
        sleep(4);
        user = testCacheService.getNullUser(userId);
        sleep(11);
        Object result = redisTemplate.opsForValue().get("user:info:115:115");
        Assert.assertNull(result);

        user = testCacheService.getNullUser(userId);
        Assert.assertNull(user);
    }

    @Test
    public void testGetUserNoParam() {
        User user = testCacheService.getUserNoParam();
        Assert.assertNotNull(user);
        user = testCacheService.getUserNoParam();
        Assert.assertNotNull(user);

        sleep(5);
        testCacheService.getUserNoParam();
        sleep(4);
        testCacheService.getUserNoParam();
        sleep(11);
        Object result = redisTemplate.opsForValue().get("user:info:{params:[]}");
        Assert.assertNull(result);

        user = testCacheService.getUserNoParam();
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetString() {
        String string = testCacheService.getString(211);
        Assert.assertNotNull(string);
        string = testCacheService.getString(211);
        Assert.assertNotNull(string);
        sleep(5);
        string = testCacheService.getString(211);
        Assert.assertNotNull(string);
    }

    @Test
    public void testGetInt() {
        Integer anInt = testCacheService.getInt(212);
        Assert.assertNotNull(anInt);
        anInt = testCacheService.getInt(212);
        Assert.assertNotNull(anInt);
        sleep(5);
        anInt = testCacheService.getInt(212);
        Assert.assertNotNull(anInt);
    }

    @Test
    public void testGetLong() {
        Long aLong = testCacheService.getLong(213);
        Assert.assertNotNull(aLong);
        aLong = testCacheService.getLong(213);
        Assert.assertNotNull(aLong);
        sleep(5);
        testCacheService.getLong(213);
    }

    @Test
    public void testGetDouble() {
        double aDouble = testCacheService.getDouble(223);
        Assert.assertNotNull(aDouble);
        aDouble = testCacheService.getDouble(223);
        Assert.assertNotNull(aDouble);
        sleep(5);
        testCacheService.getDouble(223);
    }

    @Test
    public void testGetFloat() {
        float aFloat = testCacheService.getFloat(224);
        Assert.assertNotNull(aFloat);
        aFloat = testCacheService.getFloat(224);
        Assert.assertNotNull(aFloat);
        sleep(5);
        testCacheService.getFloat(224);
    }

    @Test
    public void testGetBigDecimal() {
        BigDecimal bigDecimal = testCacheService.getBigDecimal(225);
        Assert.assertNotNull(bigDecimal);
        bigDecimal = testCacheService.getBigDecimal(225);
        Assert.assertNotNull(bigDecimal);
        sleep(5);
        testCacheService.getBigDecimal(225);
    }

    @Test
    public void testGetEnum() {
        CacheMode cacheMode = testCacheService.getEnum(214);
        Assert.assertNotNull(cacheMode);
        cacheMode = testCacheService.getEnum(214);
        Assert.assertEquals(cacheMode, CacheMode.ONLY_FIRST);
        sleep(5);
        cacheMode = testCacheService.getEnum(214);
        Assert.assertEquals(cacheMode, CacheMode.ONLY_FIRST);
    }

    @Test
    public void testGetDate() {
        Date date = testCacheService.getDate(244);
        Assert.assertNotNull(date);
        date = testCacheService.getDate(244);
        Assert.assertTrue(date.getTime() <= System.currentTimeMillis());
        sleep(5);
        date = testCacheService.getDate(244);
        Assert.assertTrue(date.getTime() <= System.currentTimeMillis());
    }


    @Test
    public void testGetArray() {
        long[] array = testCacheService.getArray(215);
        Assert.assertNotNull(array);
        array = testCacheService.getArray(215);
        Assert.assertEquals(array.length, 3);
        sleep(5);
        array = testCacheService.getArray(215);
        Assert.assertEquals(array.length, 3);
    }

    @Test
    public void testGetObjectArray() {
        User[] array = testCacheService.getObjectArray(216);
        Assert.assertNotNull(array);
        array = testCacheService.getObjectArray(216);
        Assert.assertEquals(array.length, 3);
        sleep(5);
        array = testCacheService.getObjectArray(216);
        Assert.assertEquals(array.length, 3);
    }

    @Test
    public void testGetList() {
        List<String> list = testCacheService.getList(217);
        Assert.assertNotNull(list);
        list = testCacheService.getList(217);
        Assert.assertEquals(list.size(), 3);
        sleep(5);
        list = testCacheService.getList(217);
        Assert.assertEquals(list.size(), 3);

    }

    @Test
    public void testGetLinkList() {
        LinkedList<String> list = testCacheService.getLinkedList(235);
        Assert.assertNotNull(list);
        list = testCacheService.getLinkedList(235);
        Assert.assertEquals(list.size(), 3);
        sleep(5);
        list = testCacheService.getLinkedList(235);
        Assert.assertEquals(list.size(), 3);

    }

    @Test
    public void testGetListObject() {
        List<User> list = testCacheService.getListObject(236);
        Assert.assertNotNull(list);
        list = testCacheService.getListObject(236);
        Assert.assertEquals(list.size(), 3);
        sleep(5);
        list = testCacheService.getListObject(236);
        Assert.assertEquals(list.size(), 3);

    }

    @Test
    public void testGetSet() {
        Set<String> set = testCacheService.getSet(237);
        Assert.assertNotNull(set);
        set = testCacheService.getSet(237);
        Assert.assertEquals(set.size(), 3);
        sleep(5);
        set = testCacheService.getSet(237);
        Assert.assertEquals(set.size(), 3);

    }

    @Test
    public void testGetSetObject() {
        Set<User> set = testCacheService.getSetObject(238);
        Assert.assertNotNull(set);
        set = testCacheService.getSetObject(238);
        Assert.assertEquals(set.size(), 1);
        sleep(5);
        set = testCacheService.getSetObject(238);
        Assert.assertEquals(set.size(), 1);

    }

    @Test
    public void testGetException() {
        List<User> list = null;
        try {
            list = testCacheService.getException(219);
        } catch (Exception e) {
            Assert.assertNotNull(e);
            return;
        }
        Assert.assertTrue(false);

    }

    @Test
    public void testGetNullPram() {
        User user = testCacheService.getNullUser(null);
        user = testCacheService.getNullUser(null);
        sleep(5);
        user = testCacheService.getNullUser(null);

        Assert.assertNull(user);
    }

    @Test
    public void testGetNullUserAllowNullValueTrueMagnification() {
        User user = testCacheService.getNullUserAllowNullValueTrueMagnification(1181L);
        user = testCacheService.getNullUserAllowNullValueTrueMagnification(1181L);
        sleep(5);
        user = testCacheService.getNullUserAllowNullValueTrueMagnification(1181L);

        Assert.assertNull(user);
    }

    @Test
    public void testGetNullUserAllowNullValueFalse() {
        User user = testCacheService.getNullUserAllowNullValueFalse(1182L);
        user = testCacheService.getNullUserAllowNullValueFalse(1182L);
        sleep(5);
        user = testCacheService.getNullUserAllowNullValueFalse(1182L);

        Assert.assertNull(user);
    }


    @Test
    public void testGetNullObjectPram() {
        try {
            User user = testCacheService.getNullObjectPram(null);
        } catch (Exception e) {
            Assert.assertNotNull(e);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testGetNullObjectPramIgnoreException() {
        User user = testCacheService.getNullObjectPramIgnoreException(null);
        Assert.assertNull(user);
    }

    @Test
    public void testPutUser() {
        long userId = 116;
        testCacheService.putUser(userId);
        User user = testCacheService.getUserById(userId);
        logger.debug(JSONUtils.objectToJson(user));
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUserId(), 11L);
    }

    @Test
    public void testPutUserNoParam() {
        User user = testCacheService.putUserNoParam();
        logger.debug(JSONUtils.objectToJson(user));
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUserId(), 11L);
    }

    @Test
    public void testPutNullUser() {
        long userId = 118_1117_10_1;
        testCacheService.putNullUser1118(userId);
        sleep(1);
        User user = testCacheService.getUserById118(userId);
        logger.debug(JSONUtils.objectToJson(user));
        Assert.assertNull(user);
    }


    @Test
    public void testPutNullUserAllowNullValueTrueMagnification() {
        long userId = 118_1117_1;
        testCacheService.putNullUserAllowNullValueTrueMagnification(userId);
        User user = testCacheService.getUserById(userId);
        logger.debug(JSONUtils.objectToJson(user));
        Assert.assertNull(user);
        sleep(3);
        user = testCacheService.getUserById(userId);
        Assert.assertNull(user);
        sleep(2);
        user = testCacheService.getUserById(userId);
        Assert.assertNotNull(user);
    }

    @Test
    public void testPutNullUserAllowNullValueFalse() {
        long userId = 118_1117_6;
        testCacheService.putNullUserAllowNullValueFalse(userId);
        User user = testCacheService.getUserById(userId);
        logger.debug(JSONUtils.objectToJson(user));
        Assert.assertNotNull(user);
    }

    @Test
    public void testEvictUser() {
        long userId = 118;
        User user = testCacheService.putUser(userId);
        sleep(3);
        testCacheService.evictUser(userId);
        sleep(3);
        Object result = redisTemplate.opsForValue().get("user:info:118:118");
        Assert.assertNull(result);


    }

    @Test
    public void testEvictAllUser() {
        testCacheService.putUserById(119);
        testCacheService.putUserById(120);
        testCacheService.putUserById(121);
        sleep(5);
        testCacheService.evictAllUser();
        sleep(3);
        Object result1 = redisTemplate.opsForValue().get("user:info:119");
        Object result2 = redisTemplate.opsForValue().get("user:info:121");
        Assert.assertNull(result1);
        Assert.assertNull(result2);
    }

    @Test
    public void testEvictAllUserNoCacheMannger() {
        testCacheService.putUserById(119_119);
        testCacheService.putUserById(119_120);
        testCacheService.putUserById(119_121);
        sleep(2);
        ((LayeringCacheManager) cacheManager).getCacheContainer().clear();
        Assert.assertTrue(((LayeringCacheManager) cacheManager).getCacheContainer().size() == 0);
        testCacheService.evictUser(119_119);
        sleep(2);
        Object result1 = redisTemplate.opsForValue().get("user:info:119119");
        Object result2 = redisTemplate.opsForValue().get("user:info:119121");
        Assert.assertNull(result1);
        Assert.assertNotNull(result2);

        ((LayeringCacheManager) cacheManager).getCacheContainer().clear();
        Assert.assertTrue(((LayeringCacheManager) cacheManager).getCacheContainer().size() == 0);
        testCacheService.evictAllUser();
        sleep(2);
        result2 = redisTemplate.opsForValue().get("user:info:119121");
        Object result3 = redisTemplate.opsForValue().get("user:info:119122");
        Assert.assertNull(result2);
        Assert.assertNull(result3);
    }


    private void sleep(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

