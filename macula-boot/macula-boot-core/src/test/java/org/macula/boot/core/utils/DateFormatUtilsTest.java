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

import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtilsTest {

    public static void main(String[] args) {

        System.out.println("Instant=" + Instant.now());
        System.out.println("LocalDateTime=" + LocalDateTime.now());
        System.out.println("ZonedDateTime=" + ZonedDateTime.now());
        System.out.println("OffsetDateTime=" + OffsetDateTime.now());

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(dateFormat.format(date));

        DateFormatter formatter = new DateFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        System.out.println(formatter.print(date, Locale.getDefault()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("+08:00")).toInstant());

        DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withResolverStyle(ResolverStyle.SMART);

        System.out.println(df.format(LocalDateTime.now()));

        System.out.println(df.parse("2019-2-26"));
    }

}