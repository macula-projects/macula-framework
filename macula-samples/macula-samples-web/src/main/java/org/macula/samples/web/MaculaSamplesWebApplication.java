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

package org.macula.samples.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.macula.samples.api.EchoService;
import org.macula.samples.vo.User;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.ServiceException;
import org.maculaframework.boot.vo.ExecuteResponse;
import org.maculaframework.boot.vo.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * <b>MaculaSamplesWeb</b> 启动类
 * </p>
 *
 * @author Rain
 * @since 2020-04-28
 */

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@Tag(name = "user", description = "the User API")
public class MaculaSamplesWebApplication {

    // @DubboReference
    // private EchoService echoService;

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable("message") String message) {
       // return echoService.echo(message);
        return "hello world";
    }

    @GetMapping("/user/{name}")
    public Response getUser(@PathVariable("name") String name) {
        User user = new User();
        user.setAge(18);
        user.setId(1L);
        user.setName(name);
        //throw new ServiceException("101", "ERROR MSG", null);
        return new ExecuteResponse<User>(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(MaculaSamplesWebApplication.class);
    }
}
