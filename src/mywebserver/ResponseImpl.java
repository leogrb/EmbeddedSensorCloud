package mywebserver;

import BIF.SWE1.interfaces.Response;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class ResponseImpl implements Response {
    private Status status;
    private Map<String, String> responseHeaders = new HashMap<String, String>();
    private String content;

    public ResponseImpl() {
        this.responseHeaders.put("Server", "BIF-SWE1-Server");
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.responseHeaders;
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
        return this.responseHeaders.get("Content-Type");
    }

    @Override
    public void setContentType(String contentType) {
        try {
            this.responseHeaders.put("Content-Type", contentType);
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

    @Override
    public void setStatusCode(int status) {
        this.status = Status.getStatusWithCode(status);
    }

    @Override
    public String getStatus() {
        if (this.status != null) {
            String statusAsString = Integer.toString(status.getCode()) + " " + status.getDescription();
            return statusAsString;
        }
        throw new IllegalArgumentException("no status was set");
    }

    @Override
    public void addHeader(String header, String value) {
        this.responseHeaders.put(header, value);
    }

    @Override
    public String getServerHeader() {
        return this.responseHeaders.get("Server");
    }

    @Override
    public void setServerHeader(String server) {
        this.responseHeaders.replace("Server", server);
    }

    @Override
    public void setContent(String content) {
        if(!content.isEmpty()) {
            this.content = content;
        }
    }

    @Override
    public void setContent(byte[] content) {
        try {
            this.content = new String(content, "UTF-8");
        }catch (UnsupportedEncodingException e){
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
        } catch(IOException e){
            System.err.println("Exception " + e.getMessage());
        }
        this.content = s.toString();
    }

    @Override
    public void send(OutputStream network) {

    }
}
