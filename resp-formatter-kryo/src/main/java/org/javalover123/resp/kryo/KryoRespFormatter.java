/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.javalover123.resp.kryo;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import org.javalover123.resp.common.dto.Info;
import org.javalover123.resp.common.BaseRespFormatter;
import org.javalover123.resp.common.util.JsonUtil;

/**
 * kryo to json formatter
 *
 * @author javalover123
 * @date 2022/4/29
 */
public class KryoRespFormatter extends BaseRespFormatter {

    private KryoSerializer kryoSerializer;

    public KryoRespFormatter(Class clazz) {
        // log = buildLog();
        this.kryoSerializer = new KryoSerializer(clazz);
    }

    @Override
    protected int typeIndex() {
        return 1;
    }

    @Override
    public Info info() {
        return new Info("0.0.1", "rdm kryo to json formatter");
    }

    @Override
    public String decode(byte[] input) throws IOException {
        try {
            log(Level.FINE, "kryo decode input|" + Arrays.toString(input));
            final Object result = kryoSerializer.deserialize(input);
            log(Level.FINE, "kryo decode result|" + result);
            return JsonUtil.toJson(result);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new IOException("kryo decode error|" + Arrays.toString(input), e);
        }
    }

}
