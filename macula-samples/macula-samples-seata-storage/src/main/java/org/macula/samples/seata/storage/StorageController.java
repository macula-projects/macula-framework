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

package org.macula.samples.seata.storage;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * <b>StorageController</b> Storage接口
 * </p>
 *
 * @author Rain
 * @since 2020-05-05
 */
@RestController
public class StorageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private final JdbcTemplate jdbcTemplate;

    public StorageController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.update("delete from storage_tbl where commodity_code = 'C00321'");
        jdbcTemplate.update(
            "insert into storage_tbl(commodity_code, count) values ('C00321', 100)");

    }

    @GetMapping(value = "/storage/{commodityCode}/{count}", produces = "application/json")
    public String echo(@PathVariable String commodityCode, @PathVariable int count) {
        LOGGER.info("Storage Service Begin ... xid: " + RootContext.getXID());
        int result = jdbcTemplate.update(
            "update storage_tbl set count = count - ? where commodity_code = ?",
            new Object[] { count, commodityCode });
        LOGGER.info("Storage Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

}