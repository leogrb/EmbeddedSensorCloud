package mywebserver.HttpResponse;

import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.MIMETypes;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ResponseImpl implements Response {
    private final static Logger LOGGER = Logger.getLogger(ResponseImpl.class.getName());

    private Status status;
    private MIMETypes mimeType;
    private String standardHttp;
    private Map<String, String> responseHeaders = new HashMap<String, String>();
    private String content;
    private String statusLine;
    private String headersAsString;
    private String httpResponse;

    /**
     * Construct a ResponseImpl object
     */
    public ResponseImpl() {
        responseHeaders.put("Server", "BIF-SWE1-Server");
        responseHeaders.put("Date", LocalDateTime.now().toString());
        standardHttp = "HTTP/1.1";
    }

    @Override
    public Map<String, String> getHeaders() {
        return responseHeaders;
    }

    @Override
    public int getContentLength() {
        byte len[] = new byte[0];
        if (this.content != null) {
            try {
                len = this.content.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            }
        }
        return len.length;
    }

    @Override
    public String getContentType() {
        return responseHeaders.get("Content-Type");
    }

    @Override
    public void setContentType(String contentType) {
        try {
            responseHeaders.put("Content-Type", contentType);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
        }
    }

    @Override
    public int getStatusCode() {
        if (this.status != null) {
            return this.status.getCode();
        }
        throw new IllegalArgumentException("Status code not set");
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMimeType(String fileExt) {
        this.mimeType = MIMETypes.getMimeTypeWithExt(fileExt);
    }

    public String getMimeType() {
        if (mimeType != null) {
            return mimeType.getContentType();
        }
        throw new IllegalArgumentException("Mime Type not set");

    }

    @Override
    public void setStatusCode(int status) {
        this.status = Status.getStatusWithCode(status);
    }

    @Override
    public String getStatus() {
        if (status != null) {
            String statusAsString = Integer.toString(status.getCode()) + " " + status.getDescription();
            return statusAsString;
        }
        throw new IllegalArgumentException("no status was set");
    }

    @Override
    public void addHeader(String header, String value) {
        responseHeaders.put(header, value);
    }

    @Override
    public String getServerHeader() {
        return responseHeaders.get("Server");
    }

    @Override
    public void setServerHeader(String server) {
        responseHeaders.replace("Server", server);
    }

    @Override
    public void setContent(String content) {
        if (!content.isEmpty()) {
            this.content = content;
        }
    }

    @Override
    public void setContent(byte[] content) {
        try {
            this.content = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        }
    }

    @Override
    public void setContent(InputStream stream) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(stream));
        StringBuffer s = new StringBuffer(); //buffer is synchronized
        String line;
        try {
            while ((line = bf.readLine()) != null) {
                s.append(line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected error " + e.getMessage(), e);
        }
        this.content = s.toString();
    }

    @Override
    public void send(OutputStream network) {
        setStatusLine();
        setHeaderString();
        try {
            setHttpResponse();
        } catch (IllegalStateException e) {
            throw new UndeclaredThrowableException(e);
        }
        try {
            if (httpResponse != null) {
                network.write(httpResponse.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected error " + e.getMessage(), e);
        }
    }

    public void setStatusLine() {
        StringBuilder s = new StringBuilder();
        s.append(standardHttp);
        s.append(' ');
        s.append(getStatus());
        s.append('\n');
        statusLine = s.toString();
    }

    public void setHeaderString() {
        //content:
        StringBuilder s = new StringBuilder();
        int contentLen;
        if ((contentLen = getContentLength()) != 0) {
            addHeader("Content-Length", Integer.toString(contentLen));
        }
        for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
            String key = entry.getKey();
            s.append(entry.getKey());
            s.append(": ");
            s.append(entry.getValue());
            s.append('\n');
        }
        s.append('\n');
        headersAsString = s.toString();
    }

    public void setHttpResponse() throws IllegalStateException {
        StringBuilder s = new StringBuilder();
        s.append(statusLine);
        s.append(headersAsString);
        if (getContentType() != null && this.content == null) {
            throw new IllegalStateException("Content is null");
        }
        s.append(content);
        httpResponse = s.toString();
    }
}
