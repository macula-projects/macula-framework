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

package org.macula.boot.core.utils;

import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * <b>DateTimeTest</b> 时间日期学习
 * </p>
 *
 * @author Rain
 * @since 2019-02-26
 */
public class DateTimeTest {

    @Test
    public void testDate() throws ParseException {
        System.out.println(OffsetDateTime.now(ZoneId.of("+07:00")).withOffsetSameInstant(ZoneOffset.ofHours(8)));
        System.out.println(OffsetDateTime.now(ZoneId.of("+07:00")).withOffsetSameLocal(ZoneOffset.ofHours(8)));
        System.out.println(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)));

        System.out.println("带时区日期字符串转UTC时间：" + OffsetDateTime.parse("2016-02-14T10:32:04.150+07:00").toInstant());
        System.out.println("带时区日期字符串转其他时区时间：" + OffsetDateTime.ofInstant(OffsetDateTime.parse("2016-02-14T10:32:04.150+07:00").toInstant(), ZoneId.of("+08:00")));
        System.out.println("带时区日期字符串转本地时间：" + LocalDateTime.ofInstant(Instant.parse("2016-02-14T10:32:04.150Z"), ZoneId.of("+08:00")));
        System.out.println("本地时间转UTC时间：" + LocalDateTime.now().atZone(ZoneOffset.of("+08:00")).toInstant());
        System.out.println("UTC当前时间：" + Instant.now());
        System.out.println("指定时区的当前时间：" + OffsetDateTime.now(ZoneId.of("+07:00")));
        System.out.println("时区当前时间：" + ZonedDateTime.now());
        System.out.println("Parse测试:" + LocalDate.parse("2019-12-12T23:44:32.000+09:00", DateTimeFormatter.ISO_DATE_TIME));
        System.out.println("OffsetDateTime to LocaleDateTime:" + OffsetDateTime.now(ZoneId.of("+07:00")).toLocalDateTime());

        DateFormatter formatter = new DateFormatter();
        //formatter.setIso(DateTimeFormat.ISO.DATE_TIME);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("ISO.DATE_TIME:" + formatter.print(new Date(), Locale.getDefault()));
        System.out.println("ISO.DATE_TIME:" + formatter.parse("2019-02-26", Locale.getDefault()));

        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_DATE_TIME;
        System.out.println("DATETIME:" + formatter1.format(OffsetDateTime.now()));
        //xx = formatter1.parse("2019-02-27T00:07:33.646+08:00");
        //System.out.println("DATETIME:" + xx);
    }

}