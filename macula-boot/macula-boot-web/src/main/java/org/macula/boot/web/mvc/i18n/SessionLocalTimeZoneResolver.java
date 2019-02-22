/*
 *  Copyright (c) 2010-2019   the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.macula.boot.web.mvc.i18n;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * <b>SessionLocalTimeZoneResolver</b> 扩展SessionLocalResolver，实现默认获取用户的时区
 * </p>
 *
 * @author Rain
 * @since 2019-02-22
 */
public class SessionLocalTimeZoneResolver extends SessionLocaleResolver {

    @Override
    protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {

        TimeZone defaultTimeZone = getDefaultTimeZone();
        if (defaultTimeZone == null) {
            // TODO 获取用户端的时区
            // defaultTimeZone = request.getTimeZone();
        }
        return defaultTimeZone;
    }
}
