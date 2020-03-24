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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.*;
import java.util.Date;

/**
 * <p>
 * <b>UserVo</b> 测试User
 * </p>
 *
 * @author Rain
 * @since 2020-03-22
 */

@Getter
@Setter
public class UserVo {
    private String username;

    // spring.jackson.date-format
    // spring.mvc.dateFormat
    // 会影响Date和LocalDate
    // 不会影响LocalDateTime，需要单独配置，所以不建议LocalDateTime与页面交互
    private LocalDate birthday;
    private LocalDateTime createTime;
    private Date date;

    @Override
    public String toString() {
        return "UserVo{" +
            "username='" + username + '\'' +
            ", birthday=" + birthday +
            ", createTime=" + createTime +
            ", date=" + date +
            '}';
    }
}
