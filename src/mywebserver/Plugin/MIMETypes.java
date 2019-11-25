package mywebserver.Plugin;

public enum MIMETypes {
    // MIME Type Sample
    bin("application/octet-stream"),
    exe("application/octet-stream"),
    oda("application/oda"),
    pdf("application/pdf"),
    eps("application/postscript"),
    ai("application/postscript"),
    ps("application/postscript"),
    rtf("application/rtf"),
    mif("application/x-mif"),
    fm("application/x-mif"),
    gtar("application/x-gtar"),
    shar("application/x-shar"),
    tar("application/x-tar"),
    hqx("application/mac-binhex40"),
    snd("audio/basic"),
    au("audio/basic"),
    wav("audio/x-wav"),
    gif("image/gif"),
    ief("image/ief"),
    jpeg("image/jpeg"),
    jpg("image/jpeg"),
    jpe("image/jpeg"),
    tiff("image/tiff"),
    tif("image/tiff"),
    rgb("image/x-rgb"),
    xbm("image/x-xbitmap"),
    xpm("image/x-xpixmap"),
    xwd("image/x-xwindowdump"),
    html("text/html"),
    htm("text/html"),
    shtml("text/html"),
    txt("text/plain"),
    rtx("text/richtext"),
    tsv("text/tab-separated-values"),
    etx("text/x-setext"),
    mpeg("video/mpeg"),
    mpg("video/mpeg"),
    mpe("video/mpeg"),
    qt("video/quicktime"),
    mov("video/quicktime"),
    avi("video/x-msvideo"),
    bat("application/x-msdos-program"),
    css("text/css"),
    js("text/javascript"),
    xml("application/xml");
    private String contentType;

    MIMETypes(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static MIMETypes getMimeTypeWithExt(String ext) {
        for (MIMETypes mt : values()) {
            if (mt.toString().equals(ext)) {
                return mt;
            }
        }
        return null;
    }
}
