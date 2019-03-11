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

package org.macula.boot.core.klock.support;

import org.macula.boot.core.klock.annotation.Klock;
import org.macula.boot.core.klock.annotation.KlockKey;
import org.springframework.stereotype.Service;

/**
 * <p>
 * <b>TestKlockService</b> 测试服务类
 * </p>
 *
 * @author Rain
 * @since 2019-03-05
 */
@Service
public class TestKlockService {

    @Klock(waitTime = 10,leaseTime = 60,keys = {"#param"})
    public String getValue(String param) throws Exception {
        //  if ("sleep".equals(param)) {//线程休眠或者断点阻塞，达到一直占用锁的测试效果
        Thread.sleep(1000*3);
        //}
        return "success";
    }

    @Klock(keys = {"#userId"})
    public String getValue(String userId,@KlockKey int id)throws Exception{
        Thread.sleep(5*1000);
        return "success";
    }

    @Klock(keys = {"#user.name","#user.id"})
    public String getValue(User user)throws Exception{
        Thread.sleep(5*1000);
        return "success";
    }

}
