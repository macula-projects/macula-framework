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

package org.maculaframework.boot.web.mvc.i18n;

import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * <p>
 * <b>TimeZoneController</b> 时区处理控制器
 * </p>
 *
 * @author Rain
 * @since 2019-02-26
 */
public class TimeZoneController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
        if ("GET".equals(req.getMethod())) {
            doGet(req, res);
        }
        if ("POST".equals(req.getMethod())) {
            doPost(req, res);
        }
        return null;
    }

    private void doGet(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Object requestedUrl = req.getAttribute("requestedUrl");
        if (requestedUrl != null) {
            String content = "<html>\n" +
                    "            <head>\n" +
                    "                <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "            </head>\n" +
                    "            <body>\n" +
                    "                <form method=\"post\" id=\"tzForm\" action=\"/timezone\">\n" +
                    "                    <input id=\"tzInput\" type=\"hidden\" name=\"timeZoneOffset\"><br>\n" +
                    "                    <input type=\"hidden\" name=\"requestedUrl\" value=\"" + requestedUrl + "\">\n" +
                    "                </form>\n" +
                    "                <script type=\"text/javascript\">\n" +
                    "                            var date = new Date();\n" +
                    "                    var offSet = date.getTimezoneOffset();\n" +
                    "                    document.getElementById(\"tzInput\").value = offSet;\n" +
                    "                    document.getElementById(\"tzForm\").submit();\n" +
                    "                </script>\n" +
                    "            </body>\n" +
                    "        </html>";
            res.getWriter().println(content);
        }
    }

    private void doPost(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String requestedUrl = req.getParameter("requestedUrl");
        int timeZoneOffset = 8;
        try {
            String str = req.getParameter("timeZoneOffset");
            timeZoneOffset = Integer.parseInt(str);
        } catch (Exception ex) {
        }

        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(-timeZoneOffset * 60);
        TimeZone timeZone = TimeZone.getTimeZone(zoneOffset);

        LocaleContextResolver localeResolver = (LocaleContextResolver) RequestContextUtils.getLocaleResolver(req);

        localeResolver.setLocaleContext(req, res, new SimpleTimeZoneAwareLocaleContext(RequestContextUtils.getLocale(req), timeZone));

        res.sendRedirect(requestedUrl);
    }
}