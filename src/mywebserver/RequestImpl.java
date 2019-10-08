package mywebserver;

import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Url;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class RequestImpl implements Request {
    private final String methods[] = {"GET", "HEAD", "PUT", "POST", "DELETE", "CONNECT", "OPTIONS", "TRACE"};
    private BufferedReader bufreader;
    private String requestparams[];
    private Map<String, String> header;
    private String body;

    public RequestImpl(InputStream in){
        this.bufreader = new BufferedReader(new InputStreamReader(in));
        this.initParsing();
    }
    public void parseInitLine() throws IOException {
        if(this.bufreader.ready()) {
            String temp = this.bufreader.readLine();
            this.requestparams = temp.split("[ ]"); // get first line -> 3 params
        }
    }
    public void parseHeader() throws IOException{
        this.header = new HashMap<String, String>();
        String templine;
        String pairs[];
        while((templine = this.bufreader.readLine()) != null && !templine.isEmpty()){ // read until request body if existing
            pairs = templine.split("[: ]", 2);
            this.header.put(pairs[0].toLowerCase(), pairs[1]); // lowerCase for getHeaders()
        }
    }
    public void parseBody() throws IOException{
        StringBuilder s = new StringBuilder();
        String line;
        while((line = this.bufreader.readLine()) != null){
            s.append(line);
        }
        this.body = s.toString();
    }
    public void initParsing(){
        try {
            this.parseInitLine();
            this.parseHeader();
            this.parseBody();
        } catch (IOException e){
            System.err.println("ERROR request stream " + e.getMessage());
        }
    }
    @Override
    public boolean isValid() {
        if(this.requestparams.length != 3){ // method path httpversion
            return false;
        }
        boolean contains = Arrays.stream(this.methods).anyMatch(this.requestparams[0].toUpperCase()::equals);
        return contains;
    }

    @Override
    public String getMethod() {
        if(isValid()) {
            return (this.requestparams[0].toUpperCase());
        }
        return null;
    }

    @Override
    public Url getUrl() {
        if(isValid()){
            return (new UrlImpl(this.requestparams[1]));
        }
        return (new UrlImpl(""));
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.header;
    }

    @Override
    public int getHeaderCount() {
        return this.header.size();
    }

    @Override
    public String getUserAgent() {
        return this.header.get("user-agent");
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(this.header.get("content-length"));
    }

    @Override
    public String getContentType() {
        return this.header.get("content-type");
    }

    @Override
    public InputStream getContentStream() {
        if(!this.body.equals(null)){
            InputStream in = new ByteArrayInputStream(this.body.getBytes());
            return in;
        }
        return null;
    }

    @Override
    public String getContentString() {
        if(this.body != null){
            return body;
        }
        return null;
    }

    @Override
    public byte[] getContentBytes() {
        if(this.body != null){
            return this.body.getBytes();
        }
        return null;
    }
}
