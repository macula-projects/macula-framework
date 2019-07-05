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

package org.maculaframework.boot.core.cache.support;

/**
 * 对象类型
 *
 * @author yuhao.wang3
 */
public enum Type {
    /**
     * null
     */
    NULL("null"),

    /**
     * string
     */
    STRING("string"),

    /**
     * object
     */
    OBJECT("Object 对象"),

    /**
     * List集合
     */
    LIST("List集合"),

    /**
     * Set集合
     */
    SET("Set集合"),

    /**
     * 数组
     */
    ARRAY("数组"),

    /**
     * 枚举
     */
    ENUM("枚举"),

    /**
     * 其他类型
     */
    OTHER("其他类型");

    private String label;

    Type(String label) {
        this.label = label;
    }

    public static Type parse(String name) {
        for (Type type : Type.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return OTHER;
    }
}