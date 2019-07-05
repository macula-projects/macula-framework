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

package org.maculaframework.boot.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.maculaframework.boot.core.utils.support.User;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONUtilsTest {

    @Test
    public void testJson() {
        User user = new User();
        user.setUsername("Rain");
        user.setPassword("password");
        user.setLocalDateTime(LocalDateTime.parse("2018-12-12T08:00:05.333"));
        user.setInstant(Instant.parse("2018-12-20T18:20:05.888Z"));
        user.setLocalDate(LocalDate.now());
        user.setDate(new Date());
        user.setOffsetDateTime(OffsetDateTime.parse("2018-12-20T18:20:05.888+00:00"));
        String userJson = JSONUtils.objectToJson(user);
        System.out.println(userJson);

        User usernew = JSONUtils.jsonToPojo(userJson, User.class);
        User usernew2 = JSONUtils.jsonToObject(userJson, new TypeReference<User>() {});

        Assert.assertEquals(user.getUsername(), usernew.getUsername());
        Assert.assertEquals(user.getUsername(), usernew2.getUsername());

        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user);

        String userListJson = JSONUtils.objectToJson(list);
        System.out.println(userListJson);

        List<User> listnew = JSONUtils.jsonToList(userListJson, User.class);
        List<User> listnew2 = JSONUtils.jsonToObject(userListJson, new TypeReference<List<User>>() {});

        Assert.assertEquals(list.size(), listnew.size());
        Assert.assertEquals(list.size(), listnew2.size());

        String json = "{\"username\":\"Rain\",\"password\":\"password\",\"instant\":\"2018-12-20T18:20:05.888Z\",\"localDateTime\":\"2018-12-12T08:00:05.333Z\",\"offsetDateTime\":\"2018-12-20T18:20:05.888+07:00\",\"localDate\":\"2019-02-26T18:00:05.000Z\",\"date\":\"2019-02-26T13:50:01.442+09:00\"}\n";
        User user2 = JSONUtils.jsonToPojo(json, User.class);
        Assert.assertEquals(user2.getUsername(), "Rain");

        System.out.println(user2.getDate());
        System.out.println(user2.getOffsetDateTime());
    }
}