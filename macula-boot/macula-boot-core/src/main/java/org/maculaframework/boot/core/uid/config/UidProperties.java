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

package org.maculaframework.boot.core.uid.config;

import org.apache.commons.lang3.StringUtils;
import org.maculaframework.boot.core.uid.utils.DateUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <b>UidProperties</b> UID配置信息
 * </p>
 *
 * @author Rain
 * @since 2019-03-05
 */
@ConfigurationProperties(prefix = "macula.uid")
public class UidProperties {

    /** 时间增量值占用位数。当前时间相对于时间基点的增量值，单位为秒 */
    private int timeBits = 28;

    /** 工作机器ID占用的位数 */
    private int workerBits = 22;

    /** 序列号占用的位数 */
    private int seqBits = 13;

    /** 时间基点. 例如 2018-11-26 (毫秒: 1543161600000) */
    private String epochStr = "2018-11-26";

    /** 时间基点对应的毫秒数 */
    private long epochSeconds = TimeUnit.MILLISECONDS.toSeconds(1543161600000L);

    public int getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(int timeBits) {
        if (timeBits > 0) {
            this.timeBits = timeBits;
        }
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        if (workerBits > 0) {
            this.workerBits = workerBits;
        }
    }

    public int getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(int seqBits) {
        if (seqBits > 0) {
            this.seqBits = seqBits;
        }
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        if (StringUtils.isNotBlank(epochStr)) {
            this.epochStr = epochStr;
            this.epochSeconds = TimeUnit.MILLISECONDS.toSeconds(DateUtils.parseByDayPattern(epochStr).getTime());
        }
    }

    public long getEpochSeconds() {
        return epochSeconds;
    }
}
