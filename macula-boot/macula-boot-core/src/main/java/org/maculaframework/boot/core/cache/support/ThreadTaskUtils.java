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

package org.maculaframework.boot.core.cache.support;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 *
 * @author yuhao.wang3
 */
public class ThreadTaskUtils {
    private static MdcThreadPoolTaskExecutor taskExecutor = null;

    static {
        taskExecutor = new MdcThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(8);
        // 最大线程数
        taskExecutor.setMaxPoolSize(64);
        // 队列最大长度
        taskExecutor.setQueueCapacity(1000);
        // 线程池维护线程所允许的空闲时间(单位秒)
        taskExecutor.setKeepAliveSeconds(120);
        /*
         * 线程池对拒绝任务(无限程可用)的处理策略
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务,如果执行器已关闭,则丢弃.
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        taskExecutor.initialize();
    }

    public static void run(Runnable runnable) {
        taskExecutor.execute(runnable);
    }
}
