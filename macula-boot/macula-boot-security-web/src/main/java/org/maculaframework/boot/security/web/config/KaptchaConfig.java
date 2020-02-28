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

package org.maculaframework.boot.security.web.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * <p>
 * <b>KaptchaConfig</b> 验证码配置
 * </p>
 *
 * @author Rain
 * @since 2019-07-07
 */
@Configuration
public class KaptchaConfig {
    @Bean(name="captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();

        // 验证码字符范围
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "123456789");
        // 文字间隔
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "2");
        // 长度
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "30");
        // 字体颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // 字体
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");
        // 图片边框颜色
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "245,248,249");
        // 图片宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "90");
        // 图片高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "35");
        // session的key
        properties.setProperty(Constants.KAPTCHA_SESSION_KEY, "kaptcha_code");

        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}