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

package org.maculaframework.boot.core.klock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kl on 2017/12/29.
 */
@ConfigurationProperties(prefix = "macula.redis.klock")
@Data
public class KlockProperties {

    //redisson
    private String address;
    private String password;
    private int database = 15;
    private ClusterServer clusterServer;
    private String codec = "org.redisson.codec.JsonJacksonCodec";
    //lock
    private long waitTime = 60;
    private long leaseTime = 60;

    @Data
    public static class ClusterServer {

        private String[] nodeAddresses;
    }
}
