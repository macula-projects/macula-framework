/**
 * MessageApplicationContext.java 2017年4月22日
 */
package org.macula.boot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * <p>
 * <b>ApiApplicationContext</b> API包的Spring上下文
 * </p>
 *
 * @author Rain
 * @version $Id$
 * @since 2017年4月22日
 */
public class ApiApplicationContext implements ApplicationContextAware {

    /**
     * container
     */
    protected static org.springframework.context.ApplicationContext container;

    /**
     * 获取i18n字符串，如果不存在则原样返回，Locale是采用用户信息中的， 如果不存在，则使用系统默认
     *
     * @param code i18n的编码
     * @return i18n字符串
     */
    public static String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }

    /**
     * 获取i18n字符串，如果不存在则原样返回，Locale是由参数指定.
     *
     * @param code   i18n的编码
     * @param locale 指定的地区信息
     * @return i18n字符串
     */
    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    /**
     * 获取i18n字符串，如果不存在则原样返回，Locale是采用用户信息中的， 如果不存在，则使用系统默认
     *
     * @param code i18n的编码
     * @param args 参数值
     * @return i18n字符串
     */
    public static String getMessage(String code, Object[] args) {
        Locale locale = getCurrentUserLocale();
        if (null == locale) {
            // 获取操作系统默认的地区
            locale = Locale.getDefault();
        }
        return getMessage(code, args, code, locale);
    }

    /**
     * 获取i18n字符串，如果不存在则原样返回，Locale是由参数指定.
     *
     * @param code   i18n的编码
     * @param args   参数值
     * @param locale 指定的地区信息
     * @return i18n字符串
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, code, locale);
    }

    /**
     * 获取i18n字符串
     *
     * @param code           i18n的编码
     * @param args           参数值
     * @param defaultMessage 如果找不到code对应的i18n信息，则使用该默认信息
     * @param locale         地区编码
     * @return i18n字符串
     */
    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        if (getContainer() == null) {
            return code;
        }
        try {
            return getContainer().getMessage(code, args, defaultMessage, locale);
        } catch (NoSuchMessageException ex) {
            return code;
        }
    }

    /**
     * 获取i18n字符串
     *
     * @param messagesourceresolvable
     * @param locale                  地区编码
     * @return i18n字符串
     */
    public static String getMessage(MessageSourceResolvable messagesourceresolvable, Locale locale) {
        if (getContainer() == null) {
            return messagesourceresolvable.getDefaultMessage();
        }
        return getContainer().getMessage(messagesourceresolvable, locale);
    }

    public static String getMessage(MessageSourceResolvable messagesourceresolvable) {
        Locale locale = getCurrentUserLocale();
        if (null == locale) {
            // 获取操作系统默认的地区
            locale = Locale.getDefault();
        }
        return getMessage(messagesourceresolvable, locale);
    }

    public static Locale getCurrentUserLocale() {
        return Locale.getDefault();
    }

    public static synchronized org.springframework.context.ApplicationContext getContainer() {
        return container;
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        container = applicationContext; // NOSONAR
    }
}
