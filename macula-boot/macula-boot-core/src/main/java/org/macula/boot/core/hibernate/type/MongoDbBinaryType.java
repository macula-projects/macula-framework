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
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Types;

import static com.mongodb.client.model.Filters.eq;

/**
 * <p> <b>MongoDbBinaryType</b> 是表示该类型的具体内容将会保存到MongoDB中，面向二进制类型 </p>
 *
 * @author Rain
 * @author Wilson Luo
 * @version $Id: MongoDbBinaryType.java 3807 2012-11-21 07:31:51Z wilson $
 * @since 2011-3-5
 */
public class MongoDbBinaryType extends AbstractMongoDbLobType<Binary> {

    public MongoDbBinaryType() {
        super(Binary.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    protected Binary createLob(final String id) {
        Binary result = new Binary();
        result.setId(id);
        return new BinaryProxy(result, () -> {
            // 根据ID查询，ID一定要是ObjectId
            Document doc = getMongoTemplate().execute(getMongoTable(),
                    (MongoCollection<Document> collection) -> collection.find(eq(KEY_ID, new ObjectId(id))).first());
            return (doc != null) ? (byte[]) doc.get(KEY_CONTENT) : null;
        });
    }

}
