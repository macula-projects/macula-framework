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
package org.macula.boot.web.mvc.view;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.IncludePage;
import org.macula.boot.web.mvc.annotation.TokenValidation;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p> <b>FreeMarkerViewImpl</b> 扩展Spring的FreeMarkerView，添加一些扩展属性 </p>
 * <p>
 * 通过实现CustomFreeMarkerViewModel接口，可以给FreeMarker注入一些默认的变量，可以实现多个CustomFreeMarkerViewModel接口<BR/>
 * 按照order的顺序加载。
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: FreeMarkerViewImpl.java 5627 2015-06-15 01:16:13Z wzp $
 * @since 2011-5-24
 */
public class FreeMarkerViewImpl extends org.springframework.web.servlet.view.freemarker.FreeMarkerView {
    private static List<CustomFreeMarkerViewModel> customList = null;

    @Autowired
    private TokenValidation tokenValidation;

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        model.put(FreemarkerServlet.KEY_INCLUDE, new IncludePage(request, response));

        // 设置DATETIME的日期格式
        model.put("dateTimePattern", getConfiguration().getDateTimeFormat());

        // 设置DATE的日期格式
        model.put("datePattern", getConfiguration().getDateFormat());

        // 设置TIME的日期格式
        model.put("timePattern", getConfiguration().getTimeFormat());

        // 设置数字格式
        model.put("numberPattern", getConfiguration().getNumberFormat());

        TimeZone timeZone = RequestContextUtils.getTimeZone(request);

        // 设置时区
        model.put("timeZone", timeZone.getID());
        model.put("timeZoneOffset", -(timeZone.getRawOffset() + timeZone.getDSTSavings()) / 60 / 1000);

        // 设置FreeMarker的时区
        getConfiguration().setTimeZone(timeZone);

        model.put("request", request);

        HttpSession session = request.getSession(false);
        if (session != null) {
            model.put("sessionId", session.getId());
        }

        model.put("tokenValidation", tokenValidation);

        super.renderMergedTemplateModel(model, request, response);
    }

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        if (customList == null) {
            Map<String, CustomFreeMarkerViewModel> customMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.getApplicationContext(), CustomFreeMarkerViewModel.class);
            if (customMap != null) {
                customList = new ArrayList<>();
                customList.addAll(customMap.values());
                OrderComparator.sort(customList);
            }
        }

        if (customList != null) {
            for (CustomFreeMarkerViewModel free : customList) {
                free.exposeHelpers(model, request);
            }
        }

        super.exposeHelpers(model, request);
    }
}
