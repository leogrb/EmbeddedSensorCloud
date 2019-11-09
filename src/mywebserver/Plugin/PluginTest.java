package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;

public class PluginTest implements Plugin {
    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        if (req.getUrl().getPath().equals("/")) {
            score += 0.05;
        }
        return score;
    }

    @Override
    public Response handle(Request req) {
        Url url = req.getUrl();
        Response response = new ResponseImpl();
        response.setStatusCode(200);
        response.setContent("test");
        return response;
    }
}
