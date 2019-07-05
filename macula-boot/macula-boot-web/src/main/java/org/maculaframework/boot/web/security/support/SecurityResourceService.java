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

package org.maculaframework.boot.web.security.support;

import org.maculaframework.boot.web.security.support.vo.ActionVo;
import org.maculaframework.boot.web.security.support.vo.MenuVo;
import org.maculaframework.boot.web.security.support.vo.ResourceVo;

import java.util.List;

/**
 * <p>
 * <b>SecurityResourceService</b> 资源加载服务接口
 * </p>
 *
 * @author Rain
 * @since 2019-07-03
 */
public interface SecurityResourceService {

    /**
     * 根据资源
     * @param resourceType 资源类型
     * @return 返回满足条件的资源信息
     */
    List<ResourceVo> findResources(ResourceType resourceType);

    /**
     * 获取动作资源
     * @param actionType
     * @return 返回按照顺序从小到大排序的动作资源
     */
    List<ActionVo> findActions(ActionType actionType);

    /**
     * 获取菜单资源
     * @param root 根ID
     * @param level 层数
     * @return 满足条件的菜单信息
     */
    List<MenuVo> findMenus(int root, int level);
}
