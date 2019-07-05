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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.maculaframework.boot.web.security.access.vote.MaculaRoleVoter;
import org.maculaframework.boot.web.security.web.access.interceptor.ActionFilterInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

    @Value("${macula.web.ignoring-regex-pattern}")
    private String ignoringRegexPattern = "/|/index.*|/resources.*|/views.*|/.*public.*|/error/.*|/.*/blank.*|/.*/ajaxforward.*|.*loginphoto.*";

    protected WebSecurityConfig() {
        super(true);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .regexMatchers(ignoringRegexPattern);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(actionFilterInvocationSecurityMetadataSource());
                        o.setAccessDecisionManager(accessDecisionManager());
                        return o;
                    }
                })
                .and()
                .sessionManagement()
                .and()
                .securityContext()
                .and()
                .requestCache()
                .and()
                .servletApi()
                .and()
                .anonymous()
                .and()
                .csrf()
                .and()
                .cors()
                .and()
                .formLogin()
                .failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException e) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"status\":\"error\",\"msg\":\"");
                    if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                        sb.append("用户名或密码输入错误，登录失败!");
                    } else if (e instanceof DisabledException) {
                        sb.append("账户被禁用，登录失败，请联系管理员!");
                    } else {
                        sb.append("登录失败!");
                    }
                    sb.append("\"}");
                    out.write(sb.toString());
                    out.flush();
                    out.close();
                })
                .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication auth) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String s = "{\"status\":\"success\",\"msg\":" + " TODO }";
                    out.write(s);
                    out.flush();
                    out.close();
                })
                .and()
                .logout()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {

                });
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public FilterInvocationSecurityMetadataSource actionFilterInvocationSecurityMetadataSource() {
        return new ActionFilterInvocationSecurityMetadataSource();
    }

    private AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
        decisionVoters.add(new MaculaRoleVoter());
        return new AffirmativeBased(decisionVoters);
    }
}
