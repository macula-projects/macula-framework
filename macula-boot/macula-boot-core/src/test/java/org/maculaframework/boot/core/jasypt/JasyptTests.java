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

package org.maculaframework.boot.core.jasypt;

import com.ulisesbocchio.jasyptspringboot.JasyptSpringBootAutoConfiguration;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * <b>JasyptTests</b> Jasypt加解密测试
 * </p>
 *
 * @author Rain
 * @since 2019-03-22
 */

@RunWith(SpringRunner.class)
@SpringBootConfiguration
@ImportAutoConfiguration(JasyptSpringBootAutoConfiguration.class)
@SpringBootTest
public class JasyptTests {

    @Autowired
    ConfigurableEnvironment environment;

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("G0CvDz7oJn6");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("mysql");
        System.out.println("username:" + username);
        System.out.println("password:" + password);
    }

    @Test
    public void test() {
        Assert.assertEquals("root", environment.getProperty("macula.datasource.druid[0].username"));
        Assert.assertEquals("mysql", environment.getProperty("macula.datasource.druid[0].password"));
    }
}
