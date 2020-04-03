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

package org.maculaframework.boot.core.klock.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.maculaframework.boot.core.klock.annotation.Klock;
import org.maculaframework.boot.core.klock.core.LockInfoProvider;
import org.maculaframework.boot.core.klock.model.LockInfo;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2017/12/29.
 * Content :
 * @author kl
 */
public class LockFactory {
    Logger logger = LoggerFactory.getLogger(getClass());

    private RedissonClient redissonClient;

    private LockInfoProvider lockInfoProvider;

    public LockFactory(RedissonClient redissonClient, LockInfoProvider lockInfoProvider) {
        this.redissonClient = redissonClient;
        this.lockInfoProvider = lockInfoProvider;
    }

    public Lock getLock(ProceedingJoinPoint joinPoint, Klock klock) {
        LockInfo lockInfo = lockInfoProvider.get(joinPoint, klock);
        switch (lockInfo.getType()) {
            // case Reentrant:
            //    return new ReentrantLock(redissonClient, lockInfo);
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }

}
