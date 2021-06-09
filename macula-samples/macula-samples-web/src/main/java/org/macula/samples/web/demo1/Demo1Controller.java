/*
 * Copyright 2004-2021 the original author or authors.
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

package org.macula.samples.web.demo1;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.macula.samples.vo.User;
import org.maculaframework.boot.vo.ExecuteResponse;
import org.maculaframework.boot.vo.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * <b>Demo1Controller</b> Demo1
 * </p>
 *
 * @author Rain
 * @since 2021-06-09
 */
@RestController
@Tag(name = "demo1", description = "the Demo API")
public class Demo1Controller {

    @GetMapping("/demo1/{name}")
    public Response getUser(@PathVariable("name") String name) {
        User user = new User();
        user.setAge(18);
        user.setId(1L);
        user.setName(name);
        //throw new ServiceException("101", "ERROR MSG", null);
        return new ExecuteResponse<User>(user);
    }
}
