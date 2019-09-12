package com.huangpan.systemlogfilter.filter;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @desc：请求参数获取
 * @title：RestUtil
 * @author: zhaoyangyang
 * @date: 2018/7/24 0024 14:24
 * @version: v1.0
 */
public class RestUtil {

    //获取请求参数
    public static String getRequestParameter(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String method = request.getMethod();
        String param = null;
        //url参数
        if (method.equalsIgnoreCase("GET")) {
            param = request.getQueryString();
            /*if (Base64.isBase64(param)) {
                param = new String(Base64.decodeBase64(param), StandardCharsets.UTF_8);
            }*/
        }
        //form参数
        else {
            param = getBodyData(request);
            /*if (Base64.isBase64(param)) {
                param = new String(Base64.decodeBase64(param), StandardCharsets.UTF_8);
            }*/
        }
        return param;
    }

    //获取请求体中的字符串(POST)
    private static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }
}
