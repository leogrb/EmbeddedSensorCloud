package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.HttpResponse.Status;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginStatic implements Plugin {
    private final static Logger LOGGER = Logger.getLogger(PluginStatic.class.getName());
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
        boolean validFile = true;
        // construct static file folder
        String folderAbsPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "deploy" + System.getProperty("file.separator")+ "tmp-static-files";
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
                if (fileName == "") {  // fileName with no extension returns ""
                    validFile = false;
                }
                if (validFile) {
                    fileExt = url.getExtension();
                    fileExt = fileExt.substring(1);
                    filePath = folderRelPath + fileName;
                    requestedFile = new File(filePath);
                }
            }
            if (validFile && requestedFile.exists() && !requestedFile.isDirectory()) {
                resp.setMimeType(fileExt);
                resp.setContentType(resp.getMimeType());
                fileStream = new FileInputStream(requestedFile);
                resp.setContent(fileStream);
                resp.setStatusCode(200);
            }
            else {
                requestedFile = new File(folderRelPath + "404err.html");
                resp.setMimeType("html");
                resp.setContentType(resp.getMimeType());
                fileStream = new FileInputStream(requestedFile);
                resp.setContent(fileStream);
                resp.setStatusCode(404);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "File " + requestedFile.getName() + " not found\n");
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } finally{
            if(fileStream != null){
                try {
                    fileStream.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error closing filestream: " + e.getMessage(), e);
                }
            }
        }
        return resp;
    }

}