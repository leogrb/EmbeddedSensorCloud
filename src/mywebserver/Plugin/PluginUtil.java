package mywebserver.Plugin;

import BIF.SWE1.interfaces.Request;

import java.util.Map;

public class PluginUtil {
    public static float calcScore(Class pluginClass, Request req) {
        float score = 0.0f;
        String pluginName = pluginClass.getSimpleName().toLowerCase();
        pluginName = pluginName.substring(6);
        String segments[] = req.getUrl().getSegments();
        for (String seg : segments) {
            if (seg.contains(pluginName)) {
                score += 0.3f;
            }
        }
        if (req.getUrl().getParameterCount() != 0) {
            Map<String, String> params = req.getUrl().getParameter();
            if (params.get(pluginName + "_plugin") != null && params.get(pluginName + "_plugin").equals("true")) {
                score += 0.3;
            }
        }
        if (score > 1) {
            return 1f;
        }
        return score;
    }
}
