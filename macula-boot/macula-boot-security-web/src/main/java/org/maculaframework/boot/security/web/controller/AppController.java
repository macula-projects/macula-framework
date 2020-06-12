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

package org.maculaframework.boot.security.web.controller;

import org.maculaframework.boot.security.web.CustomSecurityService;
import org.maculaframework.boot.security.web.SecurityUtils;
import org.maculaframework.boot.security.web.support.Menu;
import org.maculaframework.boot.security.web.support.User;
import org.maculaframework.boot.web.controller.BaseController;
import org.maculaframework.boot.web.mvc.annotation.OpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private CustomSecurityService securityService;

    @GetMapping("/**/menus/{root}/{level}")
    @OpenApi
    public List<Menu> listMenus(@PathVariable int root, @PathVariable int level) {
        if (securityService != null) {
            return securityService.findMenus(getAppName(), root, level);
        }
        return new ArrayList<>();
    }

    @GetMapping(value = {"/admin/user", "/front/user", "/mobile/user"})
    @OpenApi
    public User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }
}
