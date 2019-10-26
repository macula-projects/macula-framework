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
package org.maculaframework.boot.web.config.security;

import org.maculaframework.boot.web.config.WebConfigProperties;
import org.maculaframework.boot.web.filter.KaptchaAuthenticationFilter;
import org.maculaframework.boot.web.security.access.vote.MaculaRoleVoter;
import org.maculaframework.boot.web.security.web.access.interceptor.ActionFilterInvocationSecurityMetadataSource;
import org.maculaframework.boot.web.security.web.access.interceptor.DelegatingFilterInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * <p>
 * <b>WebSecurityConfig</b> Web Security配置文件
 * </p>
 *
 * @author Rain
 * @since 2019-06-20
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebConfigProperties webConfigProperties;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .regexMatchers(webConfigProperties.getIgnoringRegexPattern());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // TODO 测试代码，未来需要删除
                .mvcMatchers("/admin/index").hasRole("ADMIN")
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {

                        // 保留原来基于表达式的MetadataSource
                        FilterInvocationSecurityMetadataSource expressionUrlMetadataSource = o.getSecurityMetadataSource();

                        o.setSecurityMetadataSource(new DelegatingFilterInvocationSecurityMetadataSource(
                                                            actionFilterInvocationSecurityMetadataSource(), expressionUrlMetadataSource));

                        // 添加动态URL Voter
                        AbstractAccessDecisionManager accessDecisionManager = (AbstractAccessDecisionManager)o.getAccessDecisionManager();
                        accessDecisionManager.getDecisionVoters().add(0, new MaculaRoleVoter());

                        return o;
                    }
                })
                .and()
                    .csrf()
                        .ignoringRequestMatchers(request -> "XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                .and()
                    .sessionManagement()
                        .sessionAuthenticationFailureHandler(authenticationFailureHandler())
                        .maximumSessions(webConfigProperties.getMaximumSessions())
                        .sessionRegistry(getApplicationContext().getBean(SessionRegistry.class))
                        .expiredUrl(webConfigProperties.getExpiredUrl()).and()
                .and()
                    .logout().permitAll()
                .and()
                    .formLogin().permitAll()
                        .failureHandler(authenticationFailureHandler())
                .and()
                    .addFilterBefore(new KaptchaAuthenticationFilter("/login", authenticationFailureHandler()),
                                            UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public FilterInvocationSecurityMetadataSource actionFilterInvocationSecurityMetadataSource() {
        return new ActionFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public SessionRegistry springSessionBackedSessionRegistry(RedisIndexedSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler(webConfigProperties.getFailureUrl());
    }
}
