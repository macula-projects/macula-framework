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

package org.macula.boot.core.config.redis;

import lombok.Data;
import org.macula.boot.MaculaConstants;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * <b>MultiRedisProperties</b> 多Redis配置
 * </p>
 *
 * @author Rain
 * @since 2019-01-31
 */
@Data
@ConfigurationProperties(prefix = MaculaConstants.CONFIG_REDIS_PREFIX)
public class MultiRedisProperties {
    private RedisProperties cache = new RedisProperties();
    private RedisProperties data = new RedisProperties();
}
