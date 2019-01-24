/**
 * Copyright 2010-2012 the original author or authors.
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
package org.macula.boot.core.hibernate.type;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.macula.boot.ApplicationContext;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.mongodb.client.model.Filters.eq;

/**
 * <p> <b>AbstractMongoDbLobType</b> 是抽象的大字段存储到MongoDb的实现 </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: AbstractMongoDbLobType.java 5584 2015-05-18 07:54:35Z wzp $
 * @since 2011-3-7
 */
public abstract class AbstractMongoDbLobType<T extends Lob> extends AbstractLobType<T> {

    public static final String KEY_ID = "_id";
    public static final String KEY_CLASS = "_class";
    public static final String KEY_CONTENT = "content";
    private static final String TEMPLATE_NAME_ATTR = "mongoTemplateName";
    private static final String DEFAULT_TEMPLATE_NAME_VALUE = "mongoTemplate";
    private static final String COLLECTION_NAME_ATTR = "mongoCollectionName";
    private String mongoTemplateName;
    private MongoTemplate mongoTemplate;
    private String mongoCollectionName;

    protected AbstractMongoDbLobType(Class<T> clazz) {
        super(clazz);
    }

    @Override
    protected void initParameterValues(Properties properties) {
        if (properties != null) {
            mongoTemplateName = properties.getProperty(TEMPLATE_NAME_ATTR);
        }
        if (StringUtils.isEmpty(mongoTemplateName)) {
            mongoTemplateName = DEFAULT_TEMPLATE_NAME_VALUE;
        }
        if (properties != null && StringUtils.isEmpty(mongoCollectionName)) {
            mongoCollectionName = properties.getProperty(COLLECTION_NAME_ATTR);
        }
    }

    @Override
    protected T getInternal(ResultSet rs, String[] names, Persistable<Long> owner, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        String id = StandardBasicTypes.STRING.nullSafeGet(rs, names[0], session);
        if (!StringUtils.isEmpty(id) && null != getMongoTemplate()) {
            return createLob(id);
        }
        return null;
    }

    /**
     * 创建所需的Lob对象，可能需要处理延迟加载等问题，所以交由子类完成构建.
     *
     * @param id
     */
    protected abstract T createLob(String id);

    @Override
    protected void setInternal(PreparedStatement st, T value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value != null && getMongoTemplate() != null) {
            final Document doc = new Document();
            if (null != value.getId()) {
                // 这里一定要保存ObjectId类型
                doc.put(KEY_ID, new ObjectId(value.getId()));
            }
            if (value instanceof Binary) {
                doc.put(KEY_CLASS, Binary.class.getName());
                doc.put(KEY_CONTENT, ((Binary) value).getContent());
            } else if (value instanceof Text) {
                doc.put(KEY_CLASS, Text.class.getName());
                doc.put(KEY_CONTENT, ((Text) value).getContent());
            }

            // 保存DBObject
            Object id = getMongoTemplate().execute(getMongoTable(), (MongoCollection<Document> mongoCollection) -> {
                Object _id = doc.get("_id");
                if (_id == null) {
                    mongoCollection.insertOne(doc);
                } else {
                    mongoCollection.replaceOne(eq("_id", _id), doc, new ReplaceOptions().upsert(true));
                }
                return doc.get(KEY_ID);
            });

            value.setId(id.toString());

            StandardBasicTypes.STRING.nullSafeSet(st, value.getId(), index, session);
        } else {
            StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
        }
    }

    /**
     * 延迟初始化mongoTemplate。
     */
    protected synchronized MongoTemplate getMongoTemplate() {
        if (mongoTemplate == null && !StringUtils.isEmpty(mongoTemplateName)) {
            mongoTemplate = ApplicationContext.getBean(mongoTemplateName);
        }
        return mongoTemplate;
    }

    /**
     * 获取指定的集合.
     */
    protected String getMongoTable() {
        if (!StringUtils.isEmpty(mongoCollectionName)) {
            return mongoCollectionName;
        }
        return getMongoTemplate().getCollectionName(returnedClass());
    }
}
