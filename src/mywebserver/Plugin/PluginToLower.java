package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.HttpResponse.Status;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginToLower implements Plugin {
    private final static Logger LOGGER = Logger.getLogger(PluginToLower.class.getName());
    private String contentString;

    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        String[] segments = req.getUrl().getSegments();
        if (segments.length != 1) {
            return 0f;
        } else {
            return score;
        }
    }

    @Override
    public Response handle(Request req) {
        ResponseImpl resp = new ResponseImpl();
        contentString = req.getContentString();
        if (contentString != null) {
            try {
                contentString = URLDecoder.decode(contentString, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            }
            String[] bodyVal = contentString.split("=");
            if (bodyVal.length > 1) {
                resp.setContent(bodyVal[1].toLowerCase());
            } else {
                resp.setContent("Not found: Bitte geben Sie einen Text ein");
            }
        } else {
            resp.setContent("Not found: Bitte geben Sie einen Text ein");
        }
        resp.setContentType("text/plain");
        resp.setStatusCode(200);
        return resp;
    }
}
