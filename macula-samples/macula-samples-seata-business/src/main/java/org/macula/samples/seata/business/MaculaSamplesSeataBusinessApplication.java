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

package org.macula.samples.seata.business;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * <b>MaculaSamplesSeataBusinessApplication</b> Business服务
 * </p>
 *
 * @author Rain
 * @since 2020-05-05
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient(autoRegister = false)
public class MaculaSamplesSeataBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaculaSamplesSeataBusinessApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @FeignClient("macula-samples-seata-storage")
    @DubboTransported(protocol = "dubbo")
    public interface StorageService {

        @GetMapping(path = "/storage/{commodityCode}/{count}")
        String storage(@PathVariable("commodityCode") String commodityCode,
                       @PathVariable("count") int count);

    }

    @FeignClient("macula-samples-seata-order")
    @DubboTransported(protocol = "dubbo")
    public interface OrderService {

        @PostMapping(path = "/order")
        String order(@RequestParam("userId") String userId,
                     @RequestParam("commodityCode") String commodityCode,
                     @RequestParam("orderCount") int orderCount);

    }
}
