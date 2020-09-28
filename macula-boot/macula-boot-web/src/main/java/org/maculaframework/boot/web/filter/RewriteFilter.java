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

package org.maculaframework.boot.web.filter;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 过滤后端请求，不属于后端的请求，交由前端路由处理
 */
public class RewriteFilter implements Filter {
    /**
     * 需要rewrite到的目的地址
     */
    public static final String REWRITE_TO = "rewriteUrl";

    /**
     * 拦截的url,url通配符之前用英文分号隔开
     */
    public static final String ROUTER_PATTERNS = "routerPatterns";

    public static final String STATIC_PATTERNS = "staticPatterns";

    /** 配置路由通配符 */
    private Set<String> routerPatterns = null;

    /** 配置静态文件通配符 */
    private Set<String> staticPatterns = null;

    private String rewriteTo = null;

    @Override
    public void init(FilterConfig cfg) throws ServletException {
        //初始化拦截配置
        rewriteTo = cfg.getInitParameter(REWRITE_TO);
        String exceptUrlString = cfg.getInitParameter(ROUTER_PATTERNS);
        if (!StringUtils.isEmpty(exceptUrlString)) {
            routerPatterns = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList(exceptUrlString.split(";", 0))));
        } else {
            routerPatterns = Collections.emptySet();
        }
        exceptUrlString = cfg.getInitParameter(STATIC_PATTERNS);
        if (!StringUtils.isEmpty(exceptUrlString)) {
            staticPatterns = Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList(exceptUrlString.split(";", 0))));
        } else {
            staticPatterns = Collections.emptySet();
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String servletPath = request.getServletPath();
        //匹配到后端路径标识，放行，否则，交给前端路由
        if (isMatches(routerPatterns, servletPath) && !isMatches(staticPatterns, servletPath)) {
            req.getRequestDispatcher(rewriteTo).forward(req, resp);
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 匹配返回true，不匹配返回false
     * @param patterns 正则表达式或通配符
     * @param url 请求的url
     * @return
     */
    private boolean isMatches(Set<String> patterns, String url) {
        if(null == patterns){
            return false;
        }
        for (String str : patterns) {
            if (str.endsWith("/*")) {
                String name = str.substring(0, str.length() - 2);
                if (url.contains(name)) {
                    return true;
                }
            } else {
                Pattern pattern = Pattern.compile(str);
                if (pattern.matcher(url).matches()) {
                    return true;
                }
            }
        }
        return false;
    }
}
