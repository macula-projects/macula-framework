package org.macula.boot.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.macula.boot.core.utils.support.User;

import java.time.*;
import java.time.format.DateTimeFormatter;
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

        String json = "{\"username\":\"Rain\",\"password\":\"password\",\"instant\":\"2018-12-20T18:20:05.888Z\",\"localDateTime\":\"2018-12-12T08:00:05.333Z\",\"offsetDateTime\":\"2018-12-20T18:20:05.888+07:00\",\"localDate\":\"2019-02-26T18:00:05.000Z\",\"date\":\"2019-02-26T13:50:01.442+09:00\"}\n";
        User user2 = JSONUtils.jsonToPojo(json, User.class);
        Assert.assertEquals(user2.getUsername(), "Rain");
        System.out.println(user2.getDate());
        System.out.println(user2.getOffsetDateTime());
    }
}