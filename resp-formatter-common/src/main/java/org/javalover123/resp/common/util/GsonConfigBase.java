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

package org.javalover123.resp.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * gson全局配置java8 LocalDateTime的序列化 全局配置时间返回格式
 *
 * @author javalover123
 * @date 2021/8/30
 */
public class GsonConfigBase {

    /**
     * 默认时区
     */
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static Gson gson(String dateFormat, boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        customGsonBuilderCustomizer(builder, dateFormat);
        if (pretty) {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }

    /**
     * description:适配自定义序列化和反序列化策略
     */
    public static void customGsonBuilderCustomizer(GsonBuilder builder, String dateFormat) {
        builder.registerTypeAdapter(BigDecimal.class, new BigDecimalSerializer());
        builder.registerTypeAdapter(Double.class, new DoubleSerializer());
        builder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        builder.registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer(dateFormat));
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer(dateFormat));
        builder.serializeNulls();
    }

    /**
     * description:序列化 BigDecimal序列化为字符串
     */
    public static class BigDecimalSerializer implements JsonSerializer<BigDecimal> {

        @Override
        public JsonElement serialize(BigDecimal value, Type type, JsonSerializationContext context) {
            if (value != null) {
                return new JsonPrimitive(value.setScale(6, RoundingMode.HALF_DOWN));
            }
            return null;
        }
    }

    /**
     * description:序列化 Double序列化为字符串
     */
    public static class DoubleSerializer implements JsonSerializer<Double> {

        @Override
        public JsonElement serialize(Double value, Type type, JsonSerializationContext context) {
            if (value != null) {
                return new JsonPrimitive(new BigDecimal(value.toString()));
            }
            return null;
        }
    }

    /**
     * description:序列化 LocalDate序列化为毫秒级时间戳
     */
    public static class LocalDateSerializer implements JsonSerializer<LocalDate> {

        @Override
        public JsonElement serialize(LocalDate value, Type type, JsonSerializationContext context) {
            if (value != null) {
                return new JsonPrimitive(value.toString());
            }
            return null;
        }
    }

    /**
     * description:反序列化 毫秒级时间戳序列化为LocalDate
     */
    public static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonElement p, Type type, JsonDeserializationContext deserializationContext) {
            String str = p.getAsString();
            if (str == null || str.isEmpty()) {
                return null;
            }
            long timestamp = Long.parseLong(str);
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_ID).toLocalDate();
            } else {
                return null;
            }
        }
    }


    /**
     * description:序列化 LocalTime序列化成字符串
     */
    public static class LocalTimeSerializer implements JsonSerializer<LocalTime> {

        @Override
        public JsonElement serialize(LocalTime value, Type type, JsonSerializationContext context) {
            if (value != null) {
                String str = value.format(DateTimeFormatter.ISO_LOCAL_TIME);
                return new JsonPrimitive(str);
            }
            return null;
        }
    }

    /**
     * description:反序列化 HH:mm:ss序列化为LocalTime
     */
    public static class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {

        @Override
        public LocalTime deserialize(JsonElement p, Type type, JsonDeserializationContext deserializationContext) {
            String str = p.getAsString();
            if (str == null || str.isEmpty()) {
                return null;
            }
            return LocalTime.parse(str, DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }


    /**
     * description:序列化 LocalDateTime序列化为毫秒级时间戳
     */
    public static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

        private String format;

        private DateTimeFormatter formatter;

        public LocalDateTimeSerializer() {
        }

        public LocalDateTimeSerializer(String format) {
            this.format = format;
            if (format != null && !format.trim().isEmpty()) {
                formatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(format));
            }
        }

        @Override
        public JsonElement serialize(LocalDateTime value, Type type, JsonSerializationContext context) {
            if (value == null) {
                return null;
            }

            if (formatter == null) {
                return new JsonPrimitive(value.toString());
            } else {
                String str = formatter.format(value);
                return new JsonPrimitive(str);
            }
        }
    }

    /**
     * description:反序列化 毫秒级时间戳序列化为LocalDateTime
     */
    public static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

        private String format;

        private DateTimeFormatter formatter;

        public LocalDateTimeDeserializer() {
        }

        public LocalDateTimeDeserializer(String format) {
            this.format = format;
            if (format != null && !format.trim().isEmpty()) {
                formatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(format));
            }
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type,
                                         JsonDeserializationContext deserializationContext) {
            String str = json.getAsString();
            if (str == null || str.isEmpty()) {
                return null;
            }

            if (formatter != null && format.length() == str.length()) {
                try {
                    return LocalDateTime.parse(str, formatter);
                } catch (Exception e) {
                    System.err.println(str + ",LocalDateTimeDeserializer error," + e);
                    return null;
                }
            }

            long timestamp = Long.parseLong(str);
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_ID);
            } else {
                return null;
            }
        }
    }

    static String quote(long timestamp) {
        String str = Long.toString(timestamp);
        return "\"" + str + "\"";
    }

}
