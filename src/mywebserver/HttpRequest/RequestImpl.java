package mywebserver.HttpRequest;

import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Url;
import mywebserver.UrlImpl;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestImpl implements Request {
    private final static Logger LOGGER = Logger.getLogger(RequestImpl.class.getName());

    private final String methods[] = {"GET", "HEAD", "PUT", "POST", "DELETE", "CONNECT", "OPTIONS", "TRACE"};
    private BufferedReader bufreader;
    private String requestparams[];
    private Map<String, String> header;
    private String body;

    public RequestImpl(InputStream in) {
        bufreader = new BufferedReader(new InputStreamReader(in));
        initParsing();
    }

    public void parseInitLine() throws IOException {
        String temp;
        // to avoid nullpointer exception
        while ((temp = bufreader.readLine()) == null) {
        }
        requestparams = temp.split("[ ]"); // get first line -> 3 params
    }

    public void parseHeader() throws IOException {
        header = new HashMap<String, String>();
        String templine;
        String pairs[];
        if (bufreader != null) {
            while ((templine = bufreader.readLine()) != null && !templine.isEmpty()) { // read until request body if existing
                pairs = templine.split(": ", 2);
                header.put(pairs[0].toLowerCase(), pairs[1]); // lowerCase for getHeaders()
            }
        }
    }

    public void parseBody() throws IOException {
        // post body not ending with '\n'
        StringBuilder s = new StringBuilder();
        char temp;
        while (bufreader.ready()) {
        temp = (char)bufreader.read();
            s.append(temp);
        }
        this.body = s.toString();
    }

    public void initParsing() {
        try {
            parseInitLine();
            parseHeader();
            if (requestparams != null && requestparams[0].toUpperCase().equals("POST")) {
                parseBody();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected error " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isValid() {
        if (requestparams != null && requestparams.length != 3) { // method path httpversion
            return false;
        }
        if (requestparams != null) {
            boolean contains = Arrays.stream(this.methods).anyMatch(requestparams[0].toUpperCase()::equals);
            return contains;
        }
        return false;
    }

    @Override
    public String getMethod() {
        if (isValid()) {
            return (requestparams[0].toUpperCase());
        }
        return null;
    }

    @Override
    public Url getUrl() {
        if (isValid()) {
            return (new UrlImpl(requestparams[1]));
        }
        return (new UrlImpl(""));
    }

    @Override
    public Map<String, String> getHeaders() {
        return header;
    }

    @Override
    public int getHeaderCount() {
        return header.size();
    }

    @Override
    public String getUserAgent() {
        return header.get("user-agent");
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(header.get("content-length"));
    }

    @Override
    public String getContentType() {
        return header.get("content-type");
    }

    @Override
    public InputStream getContentStream() {
        if (!body.equals(null)) {
            InputStream in = new ByteArrayInputStream(body.getBytes());
            return in;
        }
        return null;
    }

    @Override
    public String getContentString() {
        if (body != null) {
            return body;
        }
        return null;
    }

    @Override
    public byte[] getContentBytes() {
        if (body != null) {
            return body.getBytes();
        }
        return null;
    }
}
