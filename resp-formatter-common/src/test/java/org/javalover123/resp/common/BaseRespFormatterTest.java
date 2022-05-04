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

package org.javalover123.resp.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.javalover123.resp.common.dto.Info;
import org.junit.Assert;
import org.junit.Test;

/**
 * base formatter test
 *
 * @author javalover123
 * @date 2022/4/29
 */
public class BaseRespFormatterTest {

    private BaseRespFormatter respFormatter = new BaseRespFormatter() {
        @Override
        public Info info() {
            return null;
        }

        @Override
        public String decode(byte[] input) throws IOException {
            return null;
        }
    };

    @Test
    public void outputToStdout() {
        String outputData = "{name:\"中国\"}";
        Map<String, Object> map = new HashMap<>();
        map.put("output", outputData);

        String expected_value = "{\"output\":\"{name:\\\"\\u4e2d\\u56fd\\\"}\"}";
        String output = respFormatter.outputToStdout(map);
        Assert.assertEquals(expected_value, output);
    }

}