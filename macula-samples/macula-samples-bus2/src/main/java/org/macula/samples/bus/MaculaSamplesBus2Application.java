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

package org.macula.samples.bus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * <b>MaculaSamplesBusApplication</b> 启动类
 * </p>
 *
 * @author Rain
 * @since 2020-04-30
 */
@SpringBootApplication
@RestController
@RemoteApplicationEventScan(basePackages = "org.macula.samples.bus")
public class MaculaSamplesBus2Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MaculaSamplesBus2Application.class);
        System.out.println(context.getEnvironment().getProperty("spring.cloud.bus.id"));
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @Value("${spring.cloud.bus.id}")
    private String originService;

    @Value("${server.port}")
    private int localServerPort;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Publish the {@link UserRemoteApplicationEvent}.
     * @param name the user name
     * @param destination the destination
     * @return If published
     */
    @GetMapping("/bus/event/publish/user")
    public boolean publish(@RequestParam String name,
                           @RequestParam(required = false) String destination) {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        publisher.publishEvent(
            new UserRemoteApplicationEvent(this, user, originService, destination));
        return true;
    }

    /**
     * Listener on the {@link UserRemoteApplicationEvent}.
     * @param event {@link UserRemoteApplicationEvent}
     */
    @EventListener
    public void onUserEvent(RemoteApplicationEvent event)
        throws JsonProcessingException {
        if (event instanceof UserRemoteApplicationEvent) {
            System.out.printf("Server [port : %d] listeners on %s\n", localServerPort,
                objectMapper.writeValueAsString(event));
        }
    }

    @EventListener
    public void onAckEvent(AckRemoteApplicationEvent event)
        throws JsonProcessingException {
        System.out.printf("Server [port : %d] listeners on %s\n", localServerPort,
            objectMapper.writeValueAsString(event));
    }
}
