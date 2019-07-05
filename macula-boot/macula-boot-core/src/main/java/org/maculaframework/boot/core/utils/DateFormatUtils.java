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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * <b>DateFormatUtil.java </b>是用来格式化时间的类
 * </p>
 *
 * @since 2011-5-19
 * @author Wilson Luo
 * @version $Id: DateFormatUtils.java 5584 2015-05-18 07:54:35Z wzp $
 */
public final class DateFormatUtils {

    private static final DateFormat dateFormat;

    private static final DateFormat dateTimeFormat;

    private static final DateFormat timeFormat;

    private static TimeZone timeZone = TimeZone.getDefault();

    static {
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
    }

    private DateFormatUtils() {
        // nothing
    }

    public synchronized static void setTimeZone(TimeZone timeZone) {
        dateTimeFormat.setTimeZone(timeZone);
        dateFormat.setTimeZone(timeZone);
        timeFormat.setTimeZone(timeZone);
        DateFormatUtils.timeZone = timeZone;
    }

    /**
     * 将string转化为日期
     *
     * @param dateString 时期字符串
     * @throws ParseException 解析异常
     */
    public synchronized static Date parseDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }

    /**
     * 将string转化为时间
     *
     * @param dateString 时间字符串
     * @throws ParseException 解析异常
     */
    public synchronized static Date parseTime(String dateString) throws ParseException {
        return timeFormat.parse(dateString);
    }

    /**
     * 将string转化为日期时间
     *
     * @param dateString 日期时间字符串
     * @throws ParseException 解析异常
     */
    public synchronized static Date parseDateTime(String dateString) throws ParseException {
        return dateTimeFormat.parse(dateString);
    }

    /**
     * 将ISO8601转化为日期时间
     *
     * @param dateString iso8601日期字符串
     */
    public static Date parseISO8601(String dateString) throws ParseException {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})(Z|([+|-])(\\d{2}):?(\\d{2}))";
        if (dateString.matches(regex)) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dateString);
            while (m.find()) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(m.group(1)));
                cal.set(Calendar.MONTH, Integer.parseInt(m.group(2)) - 1);
                cal.set(Calendar.DATE, Integer.parseInt(m.group(3)));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(4)));
                cal.set(Calendar.MINUTE, Integer.parseInt(m.group(5)));
                cal.set(Calendar.SECOND, Integer.parseInt(m.group(6)));
                cal.set(Calendar.MILLISECOND, Integer.parseInt(m.group(7)));
                if ("Z".equals(m.group(8))) {
                    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                } else {
                    cal.setTimeZone(TimeZone.getTimeZone("GMT" + m.group(9) + m.group(10) + ":" + m.group(11)));
                }
                return cal.getTime();
            }
        }
        throw new ParseException(dateString + "is not a iso 8601 format date", 0);
    }

    /**
     * 根据日期字符串是否含有时间决定转换为日期还是日期时间还是时间 可以支持ISO8601格式
     *
     * @param dateString
     */
    public synchronized static Date parseAll(String dateString) throws ParseException {
        try {
            return parseISO8601(dateString);
        } catch (ParseException ex) {
            try {
                return parseDateTime(dateString);
            } catch (ParseException ex1) {
                try {
                    return parseDate(dateString);
                } catch (ParseException ex2) {
                    return parseTime(dateString);
                }
            }
        }
    }

    /**
     * 按格式输出date到string
     *
     * @param date
     */
    public synchronized static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 按格式输出time到string
     *
     * @param date
     */
    public synchronized static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 按格式输出DateTime到string
     *
     * @param date
     */
    public synchronized static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 按格式输出DateTime到iso8601 string
     *
     * @param date
     * @return iso 8601 string
     */
    public synchronized static String formatISO8601(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    /**
     * 按格式输出date到string,按照日期类型自动判断
     *
     * @param date
     */
    public synchronized static String formatAll(Date date) {
        if (date instanceof java.sql.Timestamp) {
            return formatDateTime(date);
        } else if (date instanceof java.sql.Time) {
            return formatTime(date);
        } else if (date instanceof java.sql.Date) {
            return formatDate(date);
        }
        return formatDateTime(date);
    }

    /**
     * 按格式输出string到date
     *
     * @param dateString
     * @param style 格式化参数
     */
    public static Date parse(String dateString, String style) throws ParseException {
        DateFormat theDateFormat = new SimpleDateFormat(style);
        theDateFormat.setTimeZone(timeZone);
        return theDateFormat.parse(dateString);
    }

    /**
     * 格式化输出date到string
     *
     * @param date
     * @param style 转化参数
     */
    public static String format(Date date, String style) {
        DateFormat theDateFormat = new SimpleDateFormat(style);
        theDateFormat.setTimeZone(timeZone);
        return theDateFormat.format(date);
    }

    /**
     * 获取时间间隔字符串
     *
     * @param dateA
     * @param dateB
     * @param resolutionB 初始解析精度,比如resolutionB=1,表示只有间隔够一个月才会显示1月...，否则显示0月
     * @return 时间间隔字符串
     */
    public static final String dateDifference(final long dateA, final long dateB, final long resolutionB) {
        StringBuffer sb = new StringBuffer();
        long difference = Math.abs(dateB - dateA);
        long resolution = resolutionB;
        if (resolution > 0L) {
            resolution--;
            long months = difference / 0x9fa52400L;
            if (months > 0L) {
                difference %= 0x9fa52400L;
                sb.append(months + " 月, ");
            }
        }
        if (resolution <= 0L && sb.length() == 0) {
            return "0 月";
        }
        resolution--;
        long days = difference / 0x5265c00L;
        if (days > 0L) {
            difference %= 0x5265c00L;
            sb.append(days + " 天, ");
        }
        if (resolution <= 0L && sb.length() == 0) {
            return "0 天";
        }
        resolution--;
        long hours = difference / 0x36ee80L;
        if (hours > 0L) {
            difference %= 0x36ee80L;
            sb.append(hours + " 小时, ");
        }
        if (resolution <= 0L && sb.length() == 0) {
            return "0 小时";
        }
        resolution--;
        long minutes = difference / 60000L;
        if (minutes > 0L) {
            difference %= 60000L;
            sb.append(minutes + " 分钟, ");
        }
        if (resolution <= 0L && sb.length() == 0) {
            return "0 分钟";
        }
        resolution--;
        long seconds = difference / 1000L;
        if (seconds > 0L) {
            // difference %= 1000L;
            sb.append(seconds + " 秒, ");
        }
        if (resolution <= 0L && sb.length() == 0) {
            return "0 秒";
        }
        if (sb.length() > 2) {
            return sb.substring(0, sb.length() - 2);
        }

        return "";
    }

}