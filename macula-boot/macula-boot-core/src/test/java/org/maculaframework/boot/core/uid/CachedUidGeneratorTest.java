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

package org.maculaframework.boot.core.uid;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maculaframework.boot.core.uid.config.UidConfiguration;
import org.maculaframework.boot.core.uid.config.UidTestConfiguration;
import org.maculaframework.boot.core.uid.impl.CachedUidGenerator;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@DataJpaTest // DataJpaTest不会加载普通的@Component组件
@Import({UidTestConfiguration.class, UidConfiguration.class})
public class CachedUidGeneratorTest {

    private static final int SIZE = 7000000; // 700w
    private static final boolean VERBOSE = false;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

    @Resource
    private CachedUidGenerator cachedUidGenerator;

    /**
     * Test for serially generate
     *
     * @throws IOException
     */
    @Test
    public void testSerialGenerate() throws IOException {
        // Generate UID serially
        Set<Long> uidSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(uidSet, i);
        }

        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /**
     * Test for parallel generate
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testParallelGenerate() throws InterruptedException, IOException {
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();

        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRun(uidSet, control));
            thread.setName("UID-generator-" + i);

            threadList.add(thread);
            thread.start();
        }

        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }

        // Check generate 700w times
        Assert.assertEquals(SIZE, control.get());

        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /**
     * Woker run
     */
    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (; ; ) {
            int myPosition = control.updateAndGet(old -> (old == SIZE ? SIZE : old + 1));
            if (myPosition == SIZE) {
                return;
            }

            doGenerate(uidSet, myPosition);
        }
    }

    /**
     * Do generating
     */
    private void doGenerate(Set<Long> uidSet, int index) {
        long uid = cachedUidGenerator.getUID();
        String parsedInfo = cachedUidGenerator.parseUID(uid);
        boolean existed = !uidSet.add(uid);
        if (existed) {
            System.out.println("Found duplicate UID " + uid);
        }

        // Check UID is positive, and can be parsed
        Assert.assertTrue(uid > 0L);
        Assert.assertTrue(StringUtils.isNotBlank(parsedInfo));

        if (VERBOSE) {
            System.out.println(Thread.currentThread().getName() + " No." + index + " >>> " + parsedInfo);
        }
    }

    /**
     * Check UIDs are all unique
     */
    private void checkUniqueID(Set<Long> uidSet) throws IOException {
        System.out.println(uidSet.size());
        Assert.assertEquals(SIZE, uidSet.size());
    }

}