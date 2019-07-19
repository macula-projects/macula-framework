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

package org.macula.cloud.base.service.impl;

import org.maculaframework.boot.web.security.support.*;
import org.maculaframework.boot.web.security.CustomResourceService;
import org.maculaframework.boot.web.security.support.Action;

import java.util.List;

/**
 * <p>
 * <b>SecurityResourceServiceImpl</b> 资源获取服务
 * </p>
 *
 * @author Rain
 * @since 2019-07-17
 */
public class SecurityResourceServiceImpl implements CustomResourceService {

    @Override
    public List<Resource> findResources(ResourceType resourceType) {
        return null;
    }

    @Override
    public List<Action> findActions(ActionType actionType) {
        return null;
    }

    @Override
    public List<Menu> findMenus(int root, int level) {
        return null;
    }
}
