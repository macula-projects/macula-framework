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

/**
 * SftlNamedTemplateResolver.java 2017年11月17日
 */
package org.macula.boot.core.repository.templatequery.template;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Iterator;
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

    public SftlNamedTemplateResolver(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 模板后缀
     *
     * @return String
     */
    @Override
    public String getSuffix() {
        return "sftl";
    }

    @Override
    public Iterator<Void> doInTemplateResource(Resource resource, final NamedTemplateCallback callback)
            throws Exception {
        InputStream inputStream = resource.getInputStream();
        final List<String> lines = IOUtils.readLines(inputStream, encoding);
        return new Iterator<Void>() {
            String name;

            StringBuilder content = new StringBuilder();

            int index = 0;

            int total = lines.size();

            @Override
            public boolean hasNext() {
                return index < total;
            }

            @Override
            public Void next() {
                do {
                    String line = lines.get(index);
                    if (isNameLine(line)) {
                        name = StringUtils.trim(StringUtils.remove(line, "--"));
                    } else {
                        line = StringUtils.trimToNull(line);
                        if (line != null) {
                            content.append(line).append(" ");
                        }
                    }
                    index++;
                } while (!isLastLine() && !isNextNameLine());

                //next template
                callback.process(name, content.toString());
                name = null;
                content = new StringBuilder();
                return null;
            }

            @Override
            public void remove() {
                //ignore
            }

            private boolean isNameLine(String line) {
                return StringUtils.contains(line, "--");
            }

            private boolean isNextNameLine() {
                String line = lines.get(index);
                return isNameLine(line);
            }

            private boolean isLastLine() {
                return index == total;
            }
        };
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
