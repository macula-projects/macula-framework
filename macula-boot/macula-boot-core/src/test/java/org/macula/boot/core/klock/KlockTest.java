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

package org.macula.boot.core.klock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.macula.boot.core.klock.support.TestKlockService;
import org.macula.boot.core.klock.support.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * <b>KlockTest</b> Klock测试类
 * </p>
 *
 * @author Rain
 * @since 2019-03-05
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan
public class KlockTest {

    @Autowired
    TestKlockService testKlockService;

    /**
     * 同一进程内多线程获取锁测试
     *
     * @throws Exception
     */
    @Test
    public void multithreadingTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        int i = 0;
        while (i < 10) {
            final int num = i;
            executorService.submit(() -> {
                try {
                    String result = testKlockService.getValue("sleep" + num);
                    System.err.println("线程:[" + Thread.currentThread().getName() + "]拿到结果=》" + result + new Date().toLocaleString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            i++;
        }
        System.in.read();
    }


    /**
     * 线程休眠50秒
     *
     * @throws Exception
     */
    @Test
    public void jvm1() throws Exception {
        String result = testKlockService.getValue("sleep");
        Assert.assertEquals(result, "success");
    }

    /**
     * 不休眠
     *
     * @throws Exception
     */
    @Test
    public void jvm2() throws Exception {
        String result = testKlockService.getValue("noSleep");
        Assert.assertEquals(result, "success");
    }

    /**
     * 不休眠
     *
     * @throws Exception
     */
    @Test
    public void jvm3() throws Exception {
        String result = testKlockService.getValue("noSleep");
        Assert.assertEquals(result, "success");
    }
    //先后启动jvm1 和 jvm 2两个测试用例，会发现虽然 jvm2没休眠,因为getValue加锁了，
    // 所以只要jvm1拿到锁就基本同时完成

    /**
     * 测试业务key
     */
    @Test
    public void businessKeyJvm1() throws Exception {
        String result = testKlockService.getValue("user1", 1);
        Assert.assertEquals(result, "success");
    }

    /**
     * 测试业务key
     */
    @Test
    public void businessKeyJvm2() throws Exception {
        String result = testKlockService.getValue("user1", 1);
        Assert.assertEquals(result, "success");
    }

    /**
     * 测试业务key
     */
    @Test
    public void businessKeyJvm3() throws Exception {
        String result = testKlockService.getValue("user1", 2);
        Assert.assertEquals(result, "success");
    }

    /**
     * 测试业务key
     */
    @Test
    public void businessKeyJvm4() throws Exception {
        String result = testKlockService.getValue(new User(3, "kl"));
        Assert.assertEquals(result, "success");
    }
}
