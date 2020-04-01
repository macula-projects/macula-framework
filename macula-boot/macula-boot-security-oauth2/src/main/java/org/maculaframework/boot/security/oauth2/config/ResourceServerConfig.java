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

package org.maculaframework.boot.security.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * <b>ResourceServerConfig</b> 资源服务器的安全配置
 * 本配置适应JWT和普通Token，根据Token决定采用哪种Provider
 * </p>
 *
 * @author Rain
 * @since 2020-03-19
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret:macula_secret$terces_alucam$123456}")
    String secret;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionUri;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    String clientSecret;

    private final static String AUTHORITIES = "authorities";

    private final static String AUTHORITIES_PREFIX = "";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                .antMatchers("/public/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .authenticationManagerResolver(tokenAuthenticationManagerResolver())
            );
    }

    @Bean
    AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver() {
        BearerTokenResolver bearerToken = new DefaultBearerTokenResolver();
        JwtAuthenticationProvider jwt = jwt();
        OpaqueTokenAuthenticationProvider opaqueToken = opaqueToken();

        return request -> {
            String token = bearerToken.resolve(request);
            if (isAJwt(token)) {
                return jwt::authenticate;
            } else {
                return opaqueToken::authenticate;
            }
        };
    }

    @Bean
    public JwtAuthenticationProvider jwt() {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder());
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        // 从JWT转换为Authentication
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter
                = new JwtGrantedAuthoritiesConverter();

            @Override
            public final AbstractAuthenticationToken convert(Jwt jwt) {
                Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
                return new JwtAuthenticationToken(jwt, authorities);
            }

            // 从JWT的authorities属性中读取权限
            private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                // 先把SCOPE变成authorities
                Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
                // 再把authorities属性转换过来，相加
                jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AUTHORITIES);
                jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITIES_PREFIX);
                authorities.addAll(jwtGrantedAuthoritiesConverter.convert(jwt));
                return authorities;
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        byte[] secrets = secret.getBytes();
        SecretKey secretKey = new SecretKeySpec(secrets, 0, secrets.length, "HMACSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public OpaqueTokenAuthenticationProvider opaqueToken() {
        return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector());
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
        return new OpaqueTokenIntrospector() {
            private OpaqueTokenIntrospector delegate =
                new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);

            @Override
            public OAuth2AuthenticatedPrincipal introspect(String token) {
                OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
                return new DefaultOAuth2AuthenticatedPrincipal(
                    principal.getName(), principal.getAttributes(), extractAuthorities(principal));
            }

            // 自定义获取用户的authorities
            private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
                List<GrantedAuthority> result = new ArrayList<>();
                result.addAll(principal.getAuthorities());

                List<String> authorities = principal.getAttribute(AUTHORITIES);
                if (authorities != null) {
                    result.addAll(
                        authorities.stream()
                            .map(e -> new SimpleGrantedAuthority(AUTHORITIES_PREFIX + e))
                            .collect(Collectors.toList())
                    );
                }
                return result;
            }
        };
    }

    private boolean isAJwt(String token) {
        return token.contains(".");
    }
}
