package mywebserver.HttpResponse;

import BIF.SWE1.interfaces.Response;
import mywebserver.Plugin.MIMETypes;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class ResponseImpl implements Response {
    private Status status;
    private MIMETypes mimeType;
    private String standardHttp;
    private Map<String, String> responseHeaders = new HashMap<String, String>();
    private String content;
    private String statusLine;
    private String headersAsString;
    private String httpResponse;

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
    /*
    The Content-Length entity-header field indicates the size of the entity-body,
    in decimal number of OCTETs, sent to the recipient or, in the case of the HEAD method,
    the size of the entity-body that would have been sent had the request been a GET.
     */
    public int getContentLength() {
        byte len[] = new byte[0];
        if (this.content != null) {
            try {
                len = this.content.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.err.println("Encoding error: " + e.getMessage());
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
            System.err.println("Error setting content type: " + e.getMessage());
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
            System.err.println("UTF-8 not supported");
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
            System.err.println("Exception " + e.getMessage());
        }
        this.content = s.toString();
    }

    @Override
    public void send(OutputStream network) {
        setStatusLine();
        setHeaderString();
        try {
            setHttpResponse();
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
        try {
            if (httpResponse != null) {
                network.write(httpResponse.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            System.err.println("Error" + e.getMessage());
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

    public void setHttpResponse() throws Exception {
        StringBuilder s = new StringBuilder();
        s.append(statusLine);
        s.append(headersAsString);
        if (getContentType() != null && this.content == null) {
            throw new Exception("Content is null");
        }
        s.append(content);
        httpResponse = s.toString();
    }
}
