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

package org.macula.boot.doc;

import io.swagger.v3.oas.annotations.Operation;
import org.maculaframework.boot.vo.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * <b>MaculaBootDocApplication</b> 测试
 * </p>
 *
 * @author Rain
 * @since 2020-10-13
 */

@SpringBootApplication
@RestController
public class MaculaBootDocApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaculaBootDocApplication.class, args);
    }

    @Operation(summary = "测试接口", description = "测试中文\n第二版</br>测试")
    @GetMapping("/test/{id}")
    public Response test(@PathVariable("id") String id) {
        return new Response();
    }

    @Operation(summary = "测试接口2")
    @GetMapping("/test2/{id}")
    public Response test2(@PathVariable("id") String id) {
        return new Response();
    }

    @GetMapping("/swagger-resources")
    public String swagger() {
        return "[{\"name\":\"default\",\"url\":\"/v3/api-docs\",\"swaggerVersion\":\"3.0\",\"location\":\"/v3/api-docs\"}]";
    }
}
