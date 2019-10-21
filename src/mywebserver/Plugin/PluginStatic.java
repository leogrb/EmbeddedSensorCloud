package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.HttpResponse.Status;

import java.io.File;

public class PluginStatic implements Plugin {
    private ResponseImpl resp;
    private String filePath;
    private String fileName;
    private String fileExt;
    private File requestedFile;
    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        String filePath = req.getUrl().getPath();
        long count = filePath.chars().filter(ch -> ch == '/').count();
        if(count == 1){
            score += 0.3f;
        }
        else {
            for (int i = 0; i < count; i++) {
                score += 0.1f;
            }
        }
        if(score > 1){
            return 1f;
        }
        return score;
    }

    @Override
    public Response handle(Request req) {
        resp = new ResponseImpl();
        // construct static file folder
        String folder = System.getProperty("user.dir") + System.getProperty("file.separator") + "tmp-static-files";
        final File staticFolder = new File(folder);
        if(!staticFolder.exists()){
            staticFolder.mkdirs();
        }
        Url url = req.getUrl();
        fileName = url.getFileName();
        fileExt = url.getExtension();
        filePath = folder + System.getProperty("file.separator") + fileName;
        requestedFile = new File(filePath);
        if(requestedFile.exists() && !requestedFile.isDirectory()){
            resp.setMimeType(fileExt);
            try{
                resp.setContentType(resp.getMimeType());
            } catch (IllegalArgumentException e){
                System.err.println(e.getStackTrace());
            }
            resp.setStatusCode(200);
            //resp.setContent();
        }
        else {
            resp.setStatusCode(404);
        }
        return resp;
    }
}
