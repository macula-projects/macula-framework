/**
 * Copyright @ 2017-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.maculaframework.boot.core.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.maculaframework.boot.MaculaConstants;

/**
 * <p> <b>XssCleaner</b> 是XSS攻击清除器</p>
 *
 * @author Arron.Lin
 * @since 2017年8月23日
 */
public class XssCleaner {

    private XssCleaner() {
    }

    public static String clean(String value, MaculaConstants.ESCAPE_XSS_LEVEL escapeXssLevel) {
        if (value == null) {
            return null;
        }

        String result;

        switch (escapeXssLevel) {
            case BASICWITHIMAGES:
                result = Jsoup.clean(value, "", Whitelist.basicWithImages(), new Document.OutputSettings().prettyPrint(false));
                break;
            case SIMPLETEXT:
                result = Jsoup.clean(value, "", Whitelist.simpleText(), new Document.OutputSettings().prettyPrint(false));
                break;
            case RELAXED:
                result = Jsoup.clean(value, "", Whitelist.relaxed(), new Document.OutputSettings().prettyPrint(false));
                break;
            default:
                result = Jsoup.clean(value, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false));
                break;
        }

        return StringEscapeUtils.unescapeXml(result);
    }
}
