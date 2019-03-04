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

package org.macula.boot.core.klock.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.macula.boot.core.klock.annotation.Klock;
import org.macula.boot.core.klock.config.KlockConfig;
import org.macula.boot.core.klock.model.LockInfo;
import org.macula.boot.core.klock.model.LockType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2017/12/29.
 */
public class LockInfoProvider {

    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";


    @Autowired
    private KlockConfig klockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    public LockInfo get(ProceedingJoinPoint joinPoint, Klock klock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = klock.lockType();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, klock);
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(klock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(klock);
        long leaseTime = getLeaseTime(klock);
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(Klock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                klockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(Klock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                klockConfig.getLeaseTime() : lock.leaseTime();
    }
}
