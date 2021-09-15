package com.huangpan.systemlogfilter.filter;



import javax.servlet.http.HttpServletRequestWrapper;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Classname MyRequestWrapper
 * @Description MyRequestWrapper
 * @Date 2021/9/15 9:46
 * @Created by huangwencai
 */

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public static final Charset defaultCharset = StandardCharsets.UTF_8;

    private final Charset charset;

    private final byte[] bodyBytes;

    private final MultiValueMap<String, String> formParameters;

    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String charEncode = request.getCharacterEncoding();
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(charEncode)) {
            charset = Charset.forName(charEncode);
        } else if (StringUtils.isNotBlank(contentType)
                && contentType.contains("charset=")) {
            charset = Charset.forName(contentType.substring(contentType.indexOf("=") + 1));
        } else {
            charset = defaultCharset;
        }
        bodyBytes = handleInputStream(request.getInputStream());
        if (StringUtils.isNotBlank(contentType)
                && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            formParameters = handleFormParameters();
        } else {
            formParameters = new LinkedMultiValueMap(0);
        }
    }

    private byte[] handleInputStream(InputStream inputStream) throws IOException {
        /*BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int volume;
        byte[] target = new byte[0];
        while ((volume = bis.read(buffer)) != -1) {
            target = ArrayUtils.addAll(target, buffer);
        }
        bis.close();*/
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        byte[] target = sb.toString().getBytes(charset);
        return target;
    }

    public String getBody() {
        return new String(bodyBytes, this.charset);
    }

    /**
     * 参照HttpPutFormContentFilter
     */
    private MultiValueMap<String, String> handleFormParameters() throws UnsupportedEncodingException {
        String[] pairs = tokenizeToStringArray(getBody(), "&", true, true);
        MultiValueMap<String, String> result = new LinkedMultiValueMap(pairs.length);
        String[] var8 = pairs;
        int var9 = pairs.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            String pair = var8[var10];
            int idx = pair.indexOf(61);
            if (idx == -1) {
                result.add(URLDecoder.decode(pair, charset.name()), null);
            } else {
                String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
                String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
                result.add(name, value);
            }
        }

        return result;
    }

    private String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return new String[0];
        } else {
            StringTokenizer st = new StringTokenizer(str, delimiters);
            ArrayList<String> tokens = new ArrayList<>();

            while(true) {
                String token;
                do {
                    if (!st.hasMoreTokens()) {
                        return tokens.toArray(new String[0]);
                    }

                    token = st.nextToken();
                    if (trimTokens) {
                        token = token.trim();
                    }
                } while(ignoreEmptyTokens && token.length() <= 0);

                tokens.add(token);
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyBytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getParameter(String name) {
        String queryStringValue = super.getParameter(name);
        String formValue = this.formParameters.getFirst(name);
        return queryStringValue != null ? queryStringValue : formValue;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new LinkedHashMap();
        Enumeration names = this.getParameterNames();

        while(names.hasMoreElements()) {
            String name = (String)names.nextElement();
            result.put(name, this.getParameterValues(name));
        }

        return result;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Set<String> names = new LinkedHashSet();
        names.addAll(Collections.list(super.getParameterNames()));
        names.addAll(this.formParameters.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        List<String> formParam = this.formParameters.get(name);
        if (formParam == null) {
            return parameterValues;
        } else if (parameterValues != null && this.getQueryString() != null) {
            List<String> result = new ArrayList(parameterValues.length + formParam.size());
            result.addAll(Arrays.asList(parameterValues));
            result.addAll(formParam);
            return result.toArray(new String[0]);
        } else {
            return formParam.toArray(new String[0]);
        }
    }



}
