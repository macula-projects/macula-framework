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

package org.maculaframework.boot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * <b>TimeZoneController</b> 时区测试
 * </p>
 *
 * @author Rain
 * @since 2019-02-26
 */

@Controller
public class TimeZoneController {

    @RequestMapping(value = "/testTimeZone", method = RequestMethod.GET)
    @ResponseBody
    public String testTimeZone(Locale clientLocale, ZoneId clientZoneId) {

        ZoneOffset serverZoneOffset = ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);

        return String.format("client timeZone: %s" +
                        "<br/> " +
                        "server timeZone: %s" +
                        "<br/>" +
                        " locale: %s%n",
                clientZoneId.normalized().getId(),
                serverZoneOffset.getId(),
                clientLocale);
    }
}