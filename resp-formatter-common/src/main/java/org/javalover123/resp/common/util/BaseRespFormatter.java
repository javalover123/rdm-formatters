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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.javalover123.resp.common.dto.Info;

/**
 * redis desktop manager base formatter
 *
 * @author javalover123
 * @date 2022/4/29
 */
public abstract class BaseRespFormatter {

    private static final PrintStream STDOUT = System.out;

    private static final InputStream STDIN = System.in;

    public void main(String[] args) {
        if (args.length == 0) {
            STDOUT.println("usage:BaseFormatter [info|validate|decode]");
            return;
        }
        switch (args[0]) {
            case "info":
                outputToStdout(this.info());
                break;
            case "validate":
                this.validate();
                break;
            case "decode":
                this.decode();
                break;
            case "encode":
                this.encode();
                break;
        }
    }

    /**
     * Info, version and description
     *
     * @return
     */
    public abstract Info info();

    public boolean validate() {
        Map<String, Object> map = new HashMap<>(4);
        boolean flag = true;
        String msg = "";
        try {
            decode(inputFromStdin());
        } catch (IOException e) {
            flag = false;
            msg = e.getLocalizedMessage();
        }
        map.put("valid", Boolean.valueOf(flag));
        map.put("error", msg);
        outputToStdout(map);
        return flag;
    }

    public String decode() {
        Map<String, Object> map = new HashMap<>(4);
        String outputData = null;
        try {
            outputData = decode(inputFromStdin());
            map.put("output", outputData);
            map.put("read-only", Boolean.valueOf(true));
            map.put("format", "json");
        } catch (IOException e) {
            map.put("error", e.getMessage());
        }
        outputToStdout(map);
        return outputData;
    }

    public void encode() {
        outputToStdout(Collections.singletonMap("error", "not impleted"));
    }

    /**
     * decode byte array
     *
     * @param input byte array
     * @return
     */
    public abstract String decode(byte[] input);

    protected void outputToStdout(Object object) {
        STDOUT.print(jsonify(object));
    }

    protected byte[] inputFromStdin() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(STDIN));
        return Base64.getDecoder().decode(br.readLine());
    }

    protected String jsonify(Object object) {
        return JsonUtil.toJson(object);
    }
}

