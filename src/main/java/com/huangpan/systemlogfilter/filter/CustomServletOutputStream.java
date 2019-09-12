package com.huangpan.systemlogfilter.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @desc：自定义输出类
 * @title：CustomServletOutputStream
 * @author: zhaoyangyang
 * @date: 2017/10/17 0017 12:40
 * @version: v1.0
 */
public class CustomServletOutputStream extends ServletOutputStream {

    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    public CustomServletOutputStream(OutputStream outputStream){
        this.outputStream=outputStream;
        byteArrayOutputStream=new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        byteArrayOutputStream.write(b);
    }

    public byte[] getCopy(){
        return byteArrayOutputStream.toByteArray();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {

    }
}
