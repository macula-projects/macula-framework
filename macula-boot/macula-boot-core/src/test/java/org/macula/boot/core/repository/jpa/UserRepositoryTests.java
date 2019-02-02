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
package org.macula.boot.core.repository.jpa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.macula.boot.core.repository.jpa.config.RepositoryConfig;
import org.macula.boot.core.repository.jpa.support.UserRepository;
import org.macula.boot.core.repository.jpa.support.domain.EmbbedContactInfo;
import org.macula.boot.core.repository.jpa.support.domain.User;
import org.macula.boot.core.repository.jpa.support.domain.UserVo;
import org.macula.boot.core.repository.jpa.support.domain.UserVo2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p> <b>UserRepositoryTests</b> 是用户存取测试. @DataJpaTest 等属于局部测试，只自动加载相关的自动配置项</p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: UserRepositoryTests.java 5354 2014-09-01 03:21:07Z wzp $
 * @since 2010-12-31
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(RepositoryConfig.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void init() {
        org.macula.boot.ApplicationContext.setContainer(applicationContext);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setFirstName("测试用户");
        User entity = userRepository.saveUser(user);

        Optional<User> findo = userRepository.findById(entity.getId());
        Assert.assertTrue(findo.isPresent());

        User find = findo.get();
        Assert.assertNotNull(find.getCreatedBy());


        find.setFirstName("测试用");
        userRepository.save(find);

        User find2 = userRepository.loadElementById(entity.getId());
        Assert.assertNotNull(find2);
        Assert.assertNotNull(find2.getCreatedBy());

        find2.setLastName("Changed");
        userRepository.save(find2);

        User find3 = userRepository.loadElementById(entity.getId());

        userRepository.customDelete(find3);
    }

    @Test
    public void testQueryAnnotation() {
        User user = new User();
        user.setFirstName("测试用户");
        user.setLastName("Rain");
        user.setEmail("rain@xxx.com");
        userRepository.saveUser(user);

        User user2 = new User();
        user2.setFirstName("测试用户2");
        user2.setLastName("Rain");
        user2.setEmail("rain2@xxx.com");
        userRepository.saveUser(user2);

        Pageable page = PageRequest.of(0, 20);
        Page<UserVo> vos = userRepository.findByLastName("Rain", page);
        System.out.println("==========size==========:" + vos.getTotalElements());
        Assert.assertEquals("测试用户", vos.getContent().get(0).getFirstName());
        Assert.assertEquals("测试用户2", vos.getContent().get(1).getFirstName());

        List<UserVo2> vo2s = userRepository.findByXXX("Rain");
        System.out.println("==========size==========:" + vo2s.size());
        Assert.assertEquals("测试用户", vo2s.get(0).getFirstName());
        Assert.assertEquals("测试用户2", vo2s.get(1).getFirstName());

        // 测试TemplateQuery注解
        Page<UserVo> vos2 = userRepository.findByLastNameVo("Rain", page);
        System.out.println("==========size==========:" + vos2.getTotalElements());
        Assert.assertEquals("测试用户", vos2.getContent().get(0).getFirstName());
        Assert.assertEquals("测试用户2", vos2.getContent().get(1).getFirstName());

        Map<String, Object> data = new HashMap<>();
        data.put("lastName", "Rain");
        Page<User> userMap = userRepository.findByLastNameMap(data, page);
        System.out.println("==========size==========:" + userMap.getTotalElements());
        Assert.assertEquals("测试用户", userMap.getContent().get(0).getFirstName());
        Assert.assertEquals("测试用户2", userMap.getContent().get(1).getFirstName());

        List<String> firstNames = new ArrayList<>();
        firstNames.add("测试用户2");
        List<User> mapAndList = userRepository.findByLastNameMapAndList(data, firstNames);
        Assert.assertEquals(1, mapAndList.size());
        Assert.assertEquals("rain2@xxx.com", mapAndList.get(0).getEmail());

        firstNames.add("测试用户");
        mapAndList = userRepository.findByLastNameMapAndList(data, firstNames);
        Assert.assertEquals(2, mapAndList.size());

        userRepository.updateFirstName("Rain", "测试用户update");
        Page<UserVo> vos21 = userRepository.findByLastNameVo("Rain", page);
        System.out.println("==========size==========:" + vos21.getTotalElements());
        System.out.println(vos21.getContent().get(0).getFirstName());
        Assert.assertEquals("测试用户update", vos21.getContent().get(0).getFirstName());
        Assert.assertEquals("测试用户update", vos21.getContent().get(1).getFirstName());
    }

    @Test
    @Transactional
    public void testQueryEmbbedContactInfo() {
        User user = new User();
        user.setFirstName("测试用户");
        user.setLastName("Rainxx");
        user.setEmail("rain@xxx.com");

        EmbbedContactInfo contactInfo = new EmbbedContactInfo();
        contactInfo.setHomeTel("123456");
        contactInfo.setOfficeTel("654321");
        contactInfo.setMobile("1222222");
        user.setContactInfo(contactInfo);

        user = userRepository.saveUser(user);

        List<EmbbedContactInfo> page = userRepository.findContactByLastName("Rainxx");
        EmbbedContactInfo u = page.get(0);
        System.out.println(u.getHomeTel() + "  before=======================");
        Assert.assertEquals("123456", u.getHomeTel());

        u.setHomeTel("xxxxxx");
        user.setEmail("xxxxemail@xx.com");

        userRepository.findContactByLastName("Rainxx");

        Optional<User> xo = userRepository.findById(user.getId());
        Assert.assertTrue(xo.isPresent());

        User xx = xo.get();

        System.out.println(xx.getEmail() + "  after================");
        Assert.assertEquals("xxxxemail@xx.com", xx.getEmail());
        System.out.println(xx.getContactInfo().getHomeTel() + "  after=======================");
        Assert.assertEquals("123456", xx.getContactInfo().getHomeTel());
    }
}
