package com.huangpan.systemlogfilter.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @desc：http响应信息
 * @title：MyHttpServletResponseWrapper
 * @author: zhaoyangyang
 * @date: 2017/10/16 0016 15:38
 * @version: v1.0
 */
public class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private CustomServletOutputStream customStream;
    /**
     * http状态码
     */
    private int httpStatus;

    public MyHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }


    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    public int getStatus() {
        return httpStatus;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            customStream = new CustomServletOutputStream(outputStream);
        }
        return customStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            customStream = new CustomServletOutputStream(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(customStream, getResponse().getCharacterEncoding()), true);
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            customStream.flush();
        }
    }

    public byte[] getCopy() {
        if (customStream != null) {
            return customStream.getCopy();
        } else {
            return new byte[0];
        }
    }

}
