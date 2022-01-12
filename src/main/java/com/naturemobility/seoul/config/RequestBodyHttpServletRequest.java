package com.naturemobility.seoul.config;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RequestBodyHttpServletRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedBytes;

    public RequestBodyHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) cacheInputStream();

        return new CachedServletInputStream(cachedBytes.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {

        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {

        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    public static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() {

            return buffer.read();
        }

        @Override
        public boolean isFinished() {

            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {

            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

            throw new RuntimeException("Not implemented");
        }
    }
}