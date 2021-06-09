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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.maculaframework.boot.ApplicationContext;
import org.maculaframework.boot.MaculaConstants;
import org.maculaframework.boot.core.exception.MaculaException;
import org.maculaframework.boot.core.exception.translator.MaculaExceptionTranslator;
import org.maculaframework.boot.core.utils.ExceptionUtils;
import org.maculaframework.boot.vo.Response;
import org.maculaframework.boot.web.utils.HttpRequestUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <b>OrderedExceptionNegotiateFilter</b> 异常内容协调器
 * </p>
 *
 * @author Rain
 * @since 2019-06-28
 */

@Slf4j
public class OrderedExceptionNegotiateFilter extends OncePerRequestFilter implements OrderedFilter {

    private int order = Ordered.HIGHEST_PRECEDENCE;

    @Autowired(required = false)
    private List<MaculaExceptionTranslator> exceptionTranslators;

    @Autowired
    private List<HttpMessageConverter<Object>> messageConverters;

    private final Map<Class<?>, Boolean> cachedLoggable = new ConcurrentHashMap<Class<?>, Boolean>();

    @Override
    public int getOrder() {
        return this.order;
    }

    protected boolean shouldLogException(Exception ex) {
        if (ex == null) {
            return false;
        }
        Class<?> className = ex.getClass();
        if (!cachedLoggable.containsKey(className)) {
            cachedLoggable.put(className, LoggerFactory.getLogger(className).isErrorEnabled());
        }
        Boolean loggable = cachedLoggable.get(className);
        return loggable == null ? false : loggable.booleanValue();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!HttpRequestUtils.isAjaxOrOpenAPIRequest(request)) {
            try {
                chain.doFilter(request, response);
            } catch (Exception ex) {
                if (shouldLogException(ex)) {
                    log.error(StringUtils.EMPTY, ex);
                }
                Response result = createResponse(request, ex, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false);
                request.setAttribute("errors", result);

                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                if (ex instanceof IOException) {
                    throw (IOException) ex;
                }
                if (ex instanceof ServletException) {
                    throw (ServletException) ex;
                }
            }
            return;
        }

        HttpRequestUtils.markAsAjaxRequest(request);
        ExceptionNegotiateResponseWrapper negotiateResponse = new ExceptionNegotiateResponseWrapper(response);
        Exception t = null;
        try {
            chain.doFilter(request, negotiateResponse);
        } catch (Exception e) {
            if (shouldLogException(e)) {
                log.error(StringUtils.EMPTY, e);
            }
            t = e;
            negotiateResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (negotiateResponse.isAbnormal() && !negotiateResponse.isCommitted()) {
                Response result = createResponse(request, t, negotiateResponse.getStatus(), negotiateResponse.isSuccess());
                result.setErrCode(MaculaConstants.ERROR_HTTP_CODE_PREFIX + "." +negotiateResponse.getStatus());
                result.setRedirection(negotiateResponse.getRedirection());
                handleResponseBody(result, request, response);
            }
        }
    }

    private Response createResponse(HttpServletRequest request, Exception throwable, int status, boolean success) {
        Response result = null;
        if (throwable != null) {
            MaculaException ex = translate(throwable);
            if (ex != null) {
                result = new Response(ex);
            }
        }
        if (result == null) {
            result = new Response();
        }
        if (throwable != null && result.getErrDesc() == null) {
            result.setErrDesc(throwable.getLocalizedMessage());
        }
        if (result.getErrCode() == null) {
            result.setErrCode(MaculaConstants.ERROR_HTTP_CODE_PREFIX + '.' + status);
        }
        if (result.getErrDesc() == null && result.getErrCode() != null) {
            result.setErrDesc(ApplicationContext.getMessage(result.getErrCode(), RequestContextUtils.getLocale(request)));
        }
        result.setSuccess(success && throwable == null);
        return result;
    }

    private MaculaException translate(Exception ex) {
        if (exceptionTranslators != null) {
            for (MaculaExceptionTranslator translator : exceptionTranslators) {
                MaculaException coreException = translator.translateExceptionIfPossible(ex);
                if (coreException != null) {
                    return coreException;
                }
            }
        }
        return ExceptionUtils.getRecursionCauseException(ex, MaculaException.class);
    }

    private ModelAndView handleResponseBody(Object returnValue, HttpServletRequest webRequest,
                                            HttpServletResponse webResponse) throws ServletException, IOException {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(webResponse);
        try {
            Class<?> returnValueType = returnValue.getClass();
            if (this.messageConverters != null) {
                for (MediaType acceptedMediaType : acceptedMediaTypes) {
                    for (HttpMessageConverter<Object> messageConverter : this.messageConverters) {
                        if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                            messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                            return new ModelAndView();
                        }
                    }
                }
            }
            if (logger.isWarnEnabled()) {
                logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType
                        + "] and " + acceptedMediaTypes);
            }
            return null;
        } finally {
            outputMessage.close();
        }
    }

    static class ExceptionNegotiateResponseWrapper extends HttpServletResponseWrapper {
        private boolean success = true;
        private boolean abnormal = false;
        private int status;
        private String message;
        private String redirection;

        public ExceptionNegotiateResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            this.message = msg;
            setStatus(sc);
        }

        @Override
        public void sendError(int sc) throws IOException {
            setStatus(sc);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            this.redirection = location;
            this.abnormal = true;
            this.status = SC_MOVED_TEMPORARILY;
            // Ajax只要不是重定向到登录页面，认为是正常返回
            this.success = !StringUtils.contains(location, "/login");;
            super.setStatus(success ? SC_OK : SC_INTERNAL_SERVER_ERROR);
        }

        @Override
        public void setStatus(int sc) {
            this.status = sc;
            this.abnormal = sc != SC_OK;
            this.success = sc == SC_OK;
            // IE不能正确解析404、302之类的ResponseText，但是500可以，那就统一返回500了
            super.setStatus(sc != SC_OK ? SC_INTERNAL_SERVER_ERROR : SC_OK);
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @return the redirection
         */
        public String getRedirection() {
            return redirection;
        }

        /**
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /**
         * @return the abnormal
         */
        public boolean isAbnormal() {
            return abnormal;
        }

        public boolean isSuccess() {
            return success;
        }

    }
}
