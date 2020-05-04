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

package org.macula.samples.bus;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * <p>
 * <b>UserRemoteApplicationEvent</b> 用户远程事件
 * </p>
 *
 * @author Rain
 * @since 2020-04-30
 */
public class UserRemoteApplicationEvent extends RemoteApplicationEvent {

    private User user;

    public UserRemoteApplicationEvent() {
    }

    public UserRemoteApplicationEvent(Object source, User user, String originService,
                                      String destinationService) {
        super(source, originService, destinationService);
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
