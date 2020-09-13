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

package org.macula.samples.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.macula.samples.api.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * <b>TestController</b>
 * </p>
 *
 * @author Rain
 * @since 2020-09-11
 */

@RestController
public class TestController {

    private RestTemplate restTemplate = new RestTemplate();

    @DubboReference
    private EchoService echoService;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/hello")
    @SentinelResource("resource")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/aa")
    @SentinelResource("aa")
    public String aa(int b, int a) {
        return "Hello test";
    }

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable("message") String message) {
        return echoService.echo(message);
    }

    @GetMapping("/template")
    public String client() {
        return restTemplate.getForObject("https://www.taobao.com/test", String.class);
    }

    @GetMapping("/slow")
    public String slow() {
        return circuitBreakerFactory.create("slow").run(() -> {
            try {
                Thread.sleep(500L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "slow";
        }, throwable -> "fallback");
    }
}