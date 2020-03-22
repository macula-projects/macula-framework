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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * <p>
 * <b>ResourceServerConfig</b> 资源服务器的安全配置
 * </p>
 *
 * @author Rain
 * @since 2020-03-19
 */

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret:macula_secret$terces_alucam$123456}") String secret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers("/message/**").hasAuthority("ROLE_USER")
                    .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer
                    .jwt()
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        .decoder(jwtDecoder())
            );
//            .oauth2ResourceServer(oauth2ResourceServer ->
//                oauth2ResourceServer
//                    .opaqueToken(opaqueToken ->
//                        opaqueToken
//                            .introspectionUri(this.introspectionUri)
//                            .introspectionClientCredentials(this.clientId, this.clientSecret)
//                    )
//            );
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
                jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
                jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
                authorities.addAll(jwtGrantedAuthoritiesConverter.convert(jwt));
                return authorities;
            }
        };
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        byte[] secrets = secret.getBytes();
        SecretKey secretKey = new SecretKeySpec(secrets, 0, secrets.length, "HMACSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
