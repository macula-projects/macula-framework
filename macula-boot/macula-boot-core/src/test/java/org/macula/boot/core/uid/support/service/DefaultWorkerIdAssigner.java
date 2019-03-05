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

package org.macula.boot.core.uid.support.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.macula.boot.core.uid.support.domain.WorkerNode;
import org.macula.boot.core.uid.support.repository.WorkerNodeRepository;
import org.macula.boot.core.uid.utils.DockerUtils;
import org.macula.boot.core.uid.utils.NetUtils;
import org.macula.boot.core.uid.worker.WorkerIdAssigner;
import org.macula.boot.core.uid.worker.WorkerNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * <b>DefaultWorkerIdAssigner</b> WorkerId分配的实现
 * </p>
 *
 * @author Rain
 * @since 2019-03-05
 */

@Slf4j
@Component
public class DefaultWorkerIdAssigner implements WorkerIdAssigner {

    @Autowired
    private WorkerNodeRepository workerNodeRepository;

    @Override
    public long assignWorkerId() {
        // build worker node entity
        WorkerNode workerNode = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        workerNodeRepository.save(workerNode);
        log.info("Add worker node:" + workerNode);

        return workerNode.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNode buildWorkerNode() {
        WorkerNode workerNode = new WorkerNode();
        if (DockerUtils.isDocker()) {
            workerNode.setType(WorkerNodeType.CONTAINER.value());
            workerNode.setHostName(DockerUtils.getDockerHost());
            workerNode.setPort(DockerUtils.getDockerPort());
        } else {
            workerNode.setType(WorkerNodeType.ACTUAL.value());
            workerNode.setHostName(NetUtils.getLocalAddress());
            workerNode.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(0, 100000));
        }

        return workerNode;
    }
}
