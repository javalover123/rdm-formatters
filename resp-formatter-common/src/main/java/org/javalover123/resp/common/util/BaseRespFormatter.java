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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
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

    protected Logger log;

    public void main(String[] args) {
        if (args.length <= typeIndex()) {
            STDOUT.println("usage:BaseFormatter [info|validate|decode]");
            return;
        }
        switch (args[typeIndex()]) {
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

    protected int typeIndex() {
        return 0;
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
            return error(e.getMessage());
        }
        outputToStdout(map);
        return outputData;
    }

    public void encode() {
        outputToStdout(Collections.singletonMap("error", "not impleted"));
    }

    public String error(String msg) {
        outputToStdout(Collections.singletonMap("error", msg));
        return null;
    }

    /**
     * decode byte array
     *
     * @param input byte array
     * @return
     */
    public abstract String decode(byte[] input) throws IOException;

    protected void outputToStdout(Object object) {
        String json = jsonify(object);
        // log(Level.FINE, "outputToStdout|" + json);
        STDOUT.print(json);
    }

    protected byte[] inputFromStdin() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(STDIN));
        return Base64.getDecoder().decode(br.readLine());
    }

    protected String jsonify(Object object) {
        return JsonUtil.toJson(object);
    }

    protected Logger buildLog() {
        InputStream inputStream = this.getClass().getResourceAsStream("/logging.properties");
        try {
            inputStream = new BufferedInputStream(inputStream);
            inputStream.mark(0);
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(inputStream);

            inputStream.reset();
            final Properties properties = new Properties();
            properties.load(inputStream);
            String pattern = properties.getProperty("java.util.logging.FileHandler.pattern", "");
            int lastIndexOf = pattern.lastIndexOf("/");
            if (lastIndexOf > 0) {
                String dir = pattern.substring(0, lastIndexOf);
                // System.out.println("create log dirs," + dir);
                new File(dir).mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Logger.getLogger(this.getClass().getName());
    }

    protected void log(Level level, String msg) {
        if (log != null) {
            log.log(level, msg);
        }
    }

}

