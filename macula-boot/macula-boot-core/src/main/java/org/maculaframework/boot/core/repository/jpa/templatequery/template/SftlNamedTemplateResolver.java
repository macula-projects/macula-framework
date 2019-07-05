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

/**
 * SftlNamedTemplateResolver.java 2017年11月17日
 */
package org.maculaframework.boot.core.repository.jpa.templatequery.template;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * <b>SftlNamedTemplateResolver</b> SFTL模板加载
 * </p>
 *
 * @author Rain
 * @version $Id$
 * @since 2017年11月17日
 */
public class SftlNamedTemplateResolver implements NamedTemplateResolver {
    private String encoding = "UTF-8";
    private String suffix = "sftl";

    public SftlNamedTemplateResolver(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public void doInTemplateResource(Resource resource, final NamedTemplateCallback callback) throws Exception {
        InputStream inputStream = resource.getInputStream();
        final List<String> lines = IOUtils.readLines(inputStream, encoding);

        String name = null;
        StringBuilder content = new StringBuilder();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);

            if (isNameLine(line)) {
                name = StringUtils.trim(StringUtils.remove(line, "--"));
            } else {
                line = StringUtils.trimToNull(line);
                if (line != null) {
                    content.append(line).append(" ");
                }
            }

            // 碰到最后一行或者下一行是SQL Name时回调
            if (isNextNameLineOrEnd(lines, index)) {
                callback.process(name, content.toString());
                name = null;
                content = new StringBuilder();
            }
        }

    }

    private boolean isNameLine(String line) {
        return StringUtils.contains(line, "--");
    }

    // 判断是否是最后一行或者下一行是--sqlname
    private boolean isNextNameLineOrEnd(List<String> lines, int index) {
        return (index + 1) == lines.size() || isNameLine(lines.get(index + 1));
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
