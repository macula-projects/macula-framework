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

package org.maculaframework.boot.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <b>DateTimeTestController</b> 时间日期测试
 * </p>
 *
 * @author Rain
 * @since 2020-03-22
 */

@Controller
public class DateTimeTestController {

    @PostMapping("/demo/date/json")
    public @ResponseBody ResponseEntity<Map<String, Object>> testJson(@RequestBody UserVo userVo) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(userVo);
        map.put("birthday", userVo.getBirthday());
        map.put("date", userVo.getDate());
        map.put("localDateTime", userVo.getCreateTime());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/demo/date/form")
    public String testForm(@ModelAttribute UserVo userVo, Model model) {
        System.out.println(userVo);
        model.addAttribute("userVo", userVo);
        return "/demo/date";
    }
}
