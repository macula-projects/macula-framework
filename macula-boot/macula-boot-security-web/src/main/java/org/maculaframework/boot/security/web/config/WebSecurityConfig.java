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

import org.maculaframework.boot.security.web.filter.OrderedExceptionNegotiateFilter;
import org.maculaframework.boot.web.config.WebConfigProperties;
import org.maculaframework.boot.security.web.filter.KaptchaAuthenticationFilter;
import org.maculaframework.boot.security.web.access.vote.MaculaRoleVoter;
import org.maculaframework.boot.security.web.interceptor.UrlRegexFilterInvocationSecurityMetadataSource;
import org.maculaframework.boot.security.web.interceptor.DelegatingFilterInvocationSecurityMetadataSource;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WebConfigProperties webConfigProperties;

    public WebSecurityConfig(@Autowired WebConfigProperties webConfigProperties) {
        this.webConfigProperties = webConfigProperties;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .regexMatchers(webConfigProperties.getIgnoringRegexPattern());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            authorizeRequests(authorizeRequests ->
                authorizeRequests
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
                            AbstractAccessDecisionManager accessDecisionManager = (AbstractAccessDecisionManager) o.getAccessDecisionManager();
                            accessDecisionManager.getDecisionVoters().add(0, new MaculaRoleVoter());

                            return o;
                        }
                    })
            )
            .csrf(csrf ->
                csrf.ignoringRequestMatchers(request -> "XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
            )
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .sessionAuthenticationFailureHandler(authenticationFailureHandler())
                    .maximumSessions(webConfigProperties.getMaximumSessions())
                    .sessionRegistry(getApplicationContext().getBean(SessionRegistry.class))
                    .expiredUrl(webConfigProperties.getExpiredUrl())
            )
            .logout(logout ->
                logout.permitAll()
            )
            .formLogin(formLogin ->
                formLogin.permitAll().failureHandler(authenticationFailureHandler())
            )
            .addFilterBefore(
                new KaptchaAuthenticationFilter("/login", authenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class
            );
    }

    @Bean
    public FilterInvocationSecurityMetadataSource actionFilterInvocationSecurityMetadataSource() {
        return new UrlRegexFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public SessionRegistry springSessionBackedSessionRegistry(RedisIndexedSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler(webConfigProperties.getFailureUrl());
    }

    @Bean
    public OrderedExceptionNegotiateFilter exceptionNegotiateFilter() {
        return new OrderedExceptionNegotiateFilter();
    }
}
