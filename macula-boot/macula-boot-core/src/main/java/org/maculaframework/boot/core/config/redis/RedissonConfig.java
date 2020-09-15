/*
 * Copyright 2004-2020 the original author or authors.
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

package org.maculaframework.boot.core.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.redisson.codec.KryoCodec;
import org.redisson.config.Config;

/**
 * <p>
 * <b>RedissonConfig</b> 对Redisson Config的封装，加了一个名称
 * </p>
 *
 * @author Rain
 * @since 2020-04-02
 */

@Getter
@Setter
public class RedissonConfig extends Config {
    private String name;
}
