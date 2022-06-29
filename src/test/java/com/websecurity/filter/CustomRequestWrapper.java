package com.websecurity.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;

public class CustomRequestWrapper extends HttpServletRequestWrapper {
    private final String body;
    public CustomRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                char[] charBuffer = new char[512];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            } else {
                sb.append("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        body = sb.toString();
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes("UTF-8"));
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
            public int read() {
                return bais.read();
            }
        };
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }
    public String getBody() {
        return this.body;
    }
    @Override
    public String getParameter(String name) {
        return super.getParameter(name);
    }
    @Override
    public Map<String, String[]> getParameterMap() {
        return super.getParameterMap();
    }
    @Override
    public Enumeration<String> getParameterNames() {
        return super.getParameterNames();
    }
    @Override
    public String[] getParameterValues(String name) {
        return super.getParameterValues(name);
    }
}