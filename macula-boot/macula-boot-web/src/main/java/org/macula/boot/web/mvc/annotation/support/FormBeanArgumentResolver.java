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
package org.macula.boot.web.mvc.annotation.support;

import org.apache.commons.lang3.StringUtils;
import org.macula.boot.core.exception.MaculaArgumentException;
import org.macula.boot.web.mvc.annotation.FormBean;
import org.macula.boot.web.mvc.annotation.TokenValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <p>BeanArgumentResolver</p>是从WebRequest中提取FormBean指定前缀的数据到方法参数中
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: FormBeanArgumentResolver.java 5364 2014-09-09 06:06:28Z wzp $
 * @since 2011-3-3
 */
@SuppressWarnings("unchecked")
public class FormBeanArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String BINDING_RESULT_LIST_NAME = "_bindingResultList_";

    private static final String DEFAULT_SEPARATOR = ".";
    private String separator = DEFAULT_SEPARATOR;

    private WebBindingInitializer webBindingInitializer;

    private TokenValidation tokenValidation;

    public FormBeanArgumentResolver(WebBindingInitializer webBindingInitializer) {
        setWebBindingInitializer(webBindingInitializer);
    }

    /**
     * Setter to configure the separator between prefix and actual property value. Defaults to
     * {@link #DEFAULT_SEPARATOR}.
     *
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = null == separator ? DEFAULT_SEPARATOR : separator;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        FormBean formBeanAnnotation = this.getFormBeanAnnotation(parameter);
        if (formBeanAnnotation != null) {
            String prefix = formBeanAnnotation.value();
            return (StringUtils.isNotEmpty(prefix) && !Pageable.class.isAssignableFrom(parameter.getParameterType()));
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        StringEscapeEditor stringEscapeEditor = getStringEscapeEditor(parameter);

        tokenValidate(parameter, webRequest);

        Object bindObject = null;

        String prefix = getPrefix(parameter);

        Class<?> paramType = parameter.getParameterType();

        if (Collection.class.isAssignableFrom(paramType) || paramType.isArray()) {
            // 确定数组或者Collection里面元素的类型
            Class<?> genericClass = null;
            if (paramType.isArray()) {
                genericClass = paramType.getComponentType();
            } else {
                genericClass = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric();
            }
            // 根据指定类型获取对象
            if (genericClass != null) {
                Map<String, Object> mappedValues = createMappedValues(genericClass, webRequest, parameter, binderFactory, prefix, stringEscapeEditor);
                if (!mappedValues.isEmpty()) {
                    List<Object> targetObject = new ArrayList<Object>(mappedValues.values());
                    WebDataBinder binder = binderFactory.createBinder(webRequest, null, DataBinder.DEFAULT_OBJECT_NAME);
                    bindObject = binder.convertIfNecessary(targetObject, paramType);
                }
            }
        } else if (Map.class.isAssignableFrom(paramType)) {
            Class<?> genericClass = ResolvableType.forMethodParameter(parameter).asMap().resolveGeneric(1);

            if (genericClass != null) {
                Map<String, Object> mappedValues = createMappedValues(genericClass, webRequest, parameter, binderFactory, prefix, stringEscapeEditor);
                if (!mappedValues.isEmpty()) {
                    Map<String, Object> targetObject = new HashMap<String, Object>();
                    for (Map.Entry<String, Object> entry : mappedValues.entrySet()) {
                        String key = entry.getKey();
                        key = key.substring(key.indexOf("['") + 2, key.indexOf("']"));
                        targetObject.put(key, entry.getValue());
                    }
                    WebDataBinder binder = binderFactory.createBinder(webRequest, null, DataBinder.DEFAULT_OBJECT_NAME);
                    bindObject = binder.convertIfNecessary(targetObject, paramType);
                }
            }

        } else {
            // 标准对象绑定
            bindObject = bindValue(webRequest, parameter, binderFactory, paramType, prefix, separator, stringEscapeEditor);
        }

        return bindObject;
    }

    /*
     * 表单重复提交
     */
    private void tokenValidate(MethodParameter parameter, NativeWebRequest webRequest) {
        if (tokenValidation != null) {
            FormBean formBeanAnnotation = this.getFormBeanAnnotation(parameter);
            if (formBeanAnnotation != null && formBeanAnnotation.valid()) {
                if (!tokenValidation.validate(formBeanAnnotation, webRequest)) {
                    if (formBeanAnnotation.captcha()) {
                        throw new MaculaArgumentException("form.captcha.exception");
                    }
                    throw new MaculaArgumentException("form.token.exception");
                }
            }
        }
    }

    /*
     * 生成指定前缀的数据Map
     */
    private Map<String, Object> createMappedValues(Class<?> genericClass, NativeWebRequest webRequest,
                                                   MethodParameter parameter, WebDataBinderFactory binderFactory, String prefix, StringEscapeEditor stringEscapeEditor) throws Exception {
        ServletRequest servletRequest = (ServletRequest) webRequest.getNativeRequest();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 将数组提取为一个一个的KEY，这里是集合必须要有prefix + '['
        Set<String> keySet = getSortedKeySet(servletRequest, prefix + '[');
        for (String key : keySet) {
            Object genericObj = null;
            if (key.endsWith(separator)) {
                String realKey = key.substring(0, key.length() - 1);
                // 给定前缀绑定数据
                genericObj = bindValue(webRequest, parameter, binderFactory, genericClass, realKey, separator, stringEscapeEditor);

            } else {
                Map<String, Object> paramValues = WebUtils.getParametersStartingWith(servletRequest, key);
                if (!paramValues.isEmpty()) {
                    WebDataBinder binderHelper = binderFactory.createBinder(webRequest, null, DataBinder.DEFAULT_OBJECT_NAME);
                    if (Collection.class.isAssignableFrom(genericClass)) {
                        genericObj = binderHelper.convertIfNecessary(paramValues.values(), genericClass);
                    } else {
                        genericObj = binderHelper.convertIfNecessary(paramValues.values().iterator().next(),
                                genericClass);
                    }
                }
            }
            if (genericObj != null) {
                resultMap.put(key, genericObj);
            }
        }
        return resultMap;
    }

    /**
     * 提取前缀，绑定数据
     *
     * @throws Exception
     */
    protected Object bindValue(NativeWebRequest webRequest, MethodParameter parameter, WebDataBinderFactory binderFactory,
                               Class<?> requiredClass, String prefix, String separator, StringEscapeEditor stringEscapeEditor) throws Exception {

        ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest,
                prefix, separator);

        Object genericObj = convertIfDomainClass(webRequest, pvs, requiredClass, prefix);
        if (null == genericObj) {
            genericObj = BeanUtils.instantiateClass(requiredClass);
        }

        WebDataBinder objectBinder = binderFactory.createBinder(webRequest, genericObj, prefix);
        objectBinder.registerCustomEditor(String.class, stringEscapeEditor);
        objectBinder.bind(pvs);

        // 如果有校验注解@Valid，则校验绑定，通过Request传递校验结果
        if (isValid(parameter)) {
            objectBinder.validate();
            BindingResult bindingResult = objectBinder.getBindingResult();

            List<BindingResult> list = (List<BindingResult>) webRequest.getAttribute(BINDING_RESULT_LIST_NAME, 0);
            if (null == list) {
                list = new ArrayList<BindingResult>();
                webRequest.setAttribute(BINDING_RESULT_LIST_NAME, list, 0);
            }
            list.add(bindingResult);
        }

        return genericObj;
    }

    private StringEscapeEditor getStringEscapeEditor(MethodParameter parameter) {
        FormBean formBean = getFormBeanAnnotation(parameter);
        return new StringEscapeEditor(formBean.htmlEscape(), formBean.javascriptEscape(), formBean.sqlEscape());
    }

    /**
     * Resolves the prefix to use to bind properties from. Will prepend a possible {@link FormBean} if available or
     * return the configured prefix otherwise.
     *
     * @param parameter
     */
    private String getPrefix(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof FormBean) {
                return ((FormBean) annotation).value();
            }
        }
        return null;
    }

    private FormBean getFormBeanAnnotation(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof FormBean) {
                return ((FormBean) annotation);
            }
        }
        return null;
    }

    private boolean isValid(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof Valid) {
                return true;
            }
        }
        return false;
    }

    /*
     * 获取指定前缀的KEY值
     */
    private Set<String> getSortedKeySet(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(prefix, "Prefix must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Set<String> keySet = new TreeSet<String>(ComparatorImpl.INSTANCE);
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith(prefix)) {
                String key = paramName;
                int lastScopeIndex = paramName.indexOf(']');
                int firstSeparator = paramName.indexOf(separator);
                if (firstSeparator > lastScopeIndex) {
                    // 这里把separator也加上，用来判断是简单数据类型还是复杂类型
                    key = paramName.substring(0, firstSeparator + 1);
                }
                if (!keySet.contains(key)) {
                    keySet.add(key);
                }
            }
        }
        return keySet;
    }

    /*
     * 如果是Domain Class，则根据是否有ID属性来自动查询实体数据，再行绑定
     */
    private Object convertIfDomainClass(WebRequest webRequest, PropertyValues pvs, Class<?> paramType, String prefix) {
        // 如果参数是Domain Class，则看看是否有ID，有就根据ID读取数据
        if (Persistable.class.isAssignableFrom(paramType)) {
            PropertyValue idValue = pvs.getPropertyValue("id");
            if (null != idValue) {
                String idString = (String) idValue.getValue();
                if (StringUtils.isNotEmpty(idString)) {
                    WebDataBinder binder = new WebDataBinder(null, prefix + separator + "id");
                    if (webBindingInitializer != null) {
                        webBindingInitializer.initBinder(binder, webRequest);
                    }
                    return binder.convertIfNecessary(idString, paramType);
                }
            }
        }
        return null;
    }

    /**
     * @param tokenValidation the tokenValidation to set
     */
    public void setTokenValidation(TokenValidation tokenValidation) {
        this.tokenValidation = tokenValidation;
    }

    private static final class ComparatorImpl implements Comparator<String> {
        public static final ComparatorImpl INSTANCE = new ComparatorImpl();

        @Override
        public int compare(String left, String right) {
            int lengthCompare = left.length() - right.length();
            return lengthCompare != 0 ? lengthCompare : left.compareTo(right);
        }
    }
}
