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

package org.maculaframework.boot.web.controller;

import org.maculaframework.boot.web.mvc.annotation.OpenApi;
import org.maculaframework.boot.web.security.CustomResourceService;
import org.maculaframework.boot.web.security.SecurityUtils;
import org.maculaframework.boot.web.security.support.Menu;
import org.maculaframework.boot.web.security.support.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <b>HomeController</b> HOME主页
 * </p>
 *
 * @author Rain
 * @since 2019-07-08
 */
@Controller
public class AppController extends BaseController {

    @Autowired(required = false)
    CustomResourceService resourceService;

    @GetMapping("/**/menus/{root}/{level}")
    @OpenApi
    public List<Menu> listMenus(@PathVariable int root, @PathVariable int level) {
        if (resourceService != null) {
            return resourceService.findMenus(root, level);
        }
        return new ArrayList<>();
    }

    @GetMapping(value = {"/admin/user", "/front/user", "/mobile/user"})
    @OpenApi
    public User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }
}
