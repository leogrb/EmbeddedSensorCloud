package mywebserver;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import BIF.SWE1.interfaces.Url;

public class UrlImpl implements Url {
    private String rawurl;

    public UrlImpl() {

    }

    public UrlImpl(String raw) {
        this.rawurl = raw;
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        if (rawurl != null) {
            if (rawurl.contains("?")) { // query
                return (rawurl.split("[?]")[0]);
            } else if (rawurl.contains("#")) { // fragment
                return (rawurl.split("[#]")[0]);
            } else return rawurl;
        }
        return "";
    }

    @Override
    public String getRawUrl() {
        // TODO Auto-generated method stub
        return rawurl;
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        String segments[] = this.getSegments();
        if (segments.length > 0) {
            if (segments[(segments.length) - 1].contains(".")) {
                Pattern pattern = Pattern.compile("^([a-zA-Z0-9\\s\\-_\\(\\)])+.([a-zA-Z0-9\\s\\-_\\(\\)])+");
                Matcher m = pattern.matcher(segments[(segments.length) - 1]);
                if (m.matches()) {
                    return segments[(segments.length) - 1];
                }
            }
        }
        return "";
    }

    @Override
    public String getExtension() {
        String extension = this.getFileName();
        if (extension != "") {
            String split[] = extension.split("[.]");
            extension = "." + split[split.length - 1];
        }
        return extension;
    }

    @Override
    public String getFragment() {
        if (rawurl != null) {
            if (rawurl.contains("#")) { // fragment
                return (rawurl.split("[#]")[1]);
            }
        }
        return "";
    }

    @Override
    public Map<String, String> getParameter() {
        // TODO Auto-generated method stub
        // return map after '?'
        Map<String, String> params = new HashMap<String, String>();
        if (rawurl != null) {
            if (rawurl.contains("?")) {
                String temp = rawurl.split("[?]")[1];
                String split[] = temp.split("[&]");
                for (int i = 0; i < split.length; i++) {
                    String pair[] = split[i].split("[=]");
                    params.put(pair[0], pair[1]);
                }
            }
        }
        return params;
    }

    @Override
    public int getParameterCount() {
        if (getParameter().isEmpty()) {
            return 0;
        } else return getParameter().size();
    }

    @Override
    public String[] getSegments() {
        // TODO Auto-generated method stub
        String path = getPath();
        String segments[] = new String[0];
        String t[];
        if (path == "") {
            return segments;
        } else {
            segments = rawurl.substring(1).split("[/]");
            return segments;
        }
    }

}
