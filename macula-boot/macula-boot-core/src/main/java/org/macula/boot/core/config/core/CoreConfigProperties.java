package org.macula.boot.core.config.core;

import lombok.Data;
import org.macula.boot.MaculaConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * <b>CoreConfigProperties</b> 属性设置
 * </p>
 *
 * @author Rain
 * @since 2019-01-23
 */
@ConfigurationProperties(prefix = MaculaConstants.CONFIG_BOOT_CORE_PREFIX)
public class CoreConfigProperties {

    // XSS防护级别，默认是BASIC级别
    private static MaculaConstants.ESCAPE_XSS_LEVEL escapeXssLevel = MaculaConstants.ESCAPE_XSS_LEVEL.BASIC;

    // 是否开启XSS防护，默认开启
    private static boolean enableEscapeXss = true;

    public static MaculaConstants.ESCAPE_XSS_LEVEL getEscapeXssLevel() {
        return escapeXssLevel;
    }

    public void setEscapeXssLevel(MaculaConstants.ESCAPE_XSS_LEVEL escapeXssLevel) {
        CoreConfigProperties.escapeXssLevel = escapeXssLevel;
    }

    public static boolean isEnableEscapeXss() {
        return enableEscapeXss;
    }

    public void setEnableEscapeXss(boolean enableEscapeXss) {
        CoreConfigProperties.enableEscapeXss = enableEscapeXss;
    }

    private Pattern pattern;

    public Pattern getPattern() {
        return this.pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Data
    public static class Pattern {
        private String datetime = "yyyy-MM-dd HH:mm:ss";
        private String date = "yyyy-MM-dd";
        private String time = "HH:mm:ss";
        private String number = "#";
    }
}
