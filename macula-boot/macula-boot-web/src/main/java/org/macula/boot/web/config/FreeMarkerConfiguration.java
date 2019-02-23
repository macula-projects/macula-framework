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

package org.macula.boot.web.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.macula.boot.core.config.core.CoreConfigProperties;
import org.macula.boot.web.mvc.view.FreeMarkerViewResolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;

/**
 * <p>
 * <b>FreeMarkerConfiguration</b> Freemarker的额外配置
 * </p>
 *
 * @author Rain
 * @since 2019-02-21
 */
public class FreeMarkerConfiguration {
    @Autowired
    private Configuration cfg;

    @Autowired
    private CoreConfigProperties coreConfigProperties;

    // Spring 初始化的时候加载配置
    @PostConstruct
    public void setConfigure() throws TemplateException {
        // 设置默认settings
        cfg.setSetting(Configuration.DATETIME_FORMAT_KEY, coreConfigProperties.getPattern().getDatetime());
        cfg.setSetting(Configuration.DATE_FORMAT_KEY, coreConfigProperties.getPattern().getDate());
        cfg.setSetting(Configuration.TIME_FORMAT_KEY, coreConfigProperties.getPattern().getTime());
        cfg.setSetting(Configuration.NUMBER_FORMAT_KEY, coreConfigProperties.getPattern().getNumber());

        // TODO 设置FreeMarker全局变量
        // cfg.setSharedVariable("ctx", ctx);
    }

    @Bean(name = "freeMarkerViewResolver")
    public FreeMarkerViewResolver freeMarkerViewResolver(FreeMarkerProperties properties) {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolverImpl();
        properties.applyToMvcViewResolver(resolver);
        return resolver;
    }
}
