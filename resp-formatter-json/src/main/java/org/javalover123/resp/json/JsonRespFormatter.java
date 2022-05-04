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

package org.javalover123.resp.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.javalover123.resp.common.BaseRespFormatter;
import org.javalover123.resp.common.dto.Info;
import org.javalover123.resp.common.util.JsonUtil;

/**
 * json milliseconds to date time
 *
 * @author javalover123
 * @date 2022/4/29
 */
public class JsonRespFormatter extends BaseRespFormatter {

    private static final Pattern PATTERN_TIMESTAMP = Pattern.compile("^\\d{13}$");

    {
        // log = buildLog();
    }

    @Override
    public Info info() {
        return new Info("0.0.1", "rdm timestamp formatter");
    }

    @Override
    public String decode(byte[] input) throws IOException {
        String str = new String(input, StandardCharsets.UTF_8).trim();
        log(Level.FINE, "decode input|" + str);
        if (str.startsWith("{")) {
            Map<String, Object> map = JsonUtil.toObject(str, Map.class);
            for (Entry<String, Object> entry : map.entrySet()) {
                final String value = Objects.toString(entry.getValue(), "");
                entry.setValue(handleValue(value));
            }
            return JsonUtil.toJson(map);
        } else if (str.startsWith("[")) {
            List<Object> list = JsonUtil.toObject(str, List.class);
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                final String value = Objects.toString(obj, "");
                list.set(i, handleValue(value));
            }
            return JsonUtil.toJson(list);
        }
        throw new IOException("not timestamp");
    }

    Object handleValue(String value) {
        String str = value.replace("\"", "");
        final Matcher matcher = PATTERN_TIMESTAMP.matcher(str);
        if (matcher.matches()) {
            final long mills = Long.parseLong(str);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault());
            return dateTime;
        }
        return value;
    }

}
