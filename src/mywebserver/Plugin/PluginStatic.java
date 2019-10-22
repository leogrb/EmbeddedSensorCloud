package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.HttpResponse.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PluginStatic implements Plugin {
    private ResponseImpl resp;
    private String filePath;
    private String fileName;
    private String fileExt;
    private File requestedFile;
    private InputStream fileStream;
    private String folderRelPath = "deploy" + System.getProperty("file.separator") + "tmp-static-files" + System.getProperty("file.separator");

    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        String filePath = req.getUrl().getPath();
        long count = filePath.chars().filter(ch -> ch == '/').count();
        if (count == 1) {
            score += 0.3f;
        } else {
            for (int i = 0; i < count; i++) {
                score += 0.1f;
            }
        }
        if (score > 1) {
            return 1f;
        }
        return score;
    }

    @Override
    public Response handle(Request req) {
        resp = new ResponseImpl();
        // construct static file folder
        String folderAbsPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "tmp-static-files";
        final File staticFolder = new File(folderAbsPath);
        if (!staticFolder.exists()) {
            staticFolder.mkdirs();
        }
        Url url = req.getUrl();
        try {
            if (url.getRawUrl().equals("/")) {
                requestedFile = new File(folderRelPath + "index.html");
                fileExt = "html";
            } else {
                fileName = url.getFileName();
                fileExt = url.getExtension();
                fileExt = fileExt.substring(1);
                filePath = folderRelPath + fileName;
                requestedFile = new File(filePath);
            }
            if (requestedFile.exists() && !requestedFile.isDirectory()) {
                resp.setMimeType(fileExt);
                resp.setContentType(resp.getMimeType());
                fileStream = new FileInputStream(requestedFile);
                resp.setContent(fileStream);
                resp.setStatusCode(200);
            }
            //resp.setContent();
            else {
                requestedFile = new File(folderRelPath + "404err.html");
                resp.setMimeType("html");
                resp.setContentType(resp.getMimeType());
                fileStream = new FileInputStream(requestedFile);
                resp.setContent(fileStream);
                resp.setStatusCode(404);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getStackTrace());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resp;
    }
}