package Test;

import mywebserver.Plugin.MIMETypes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestMIMETypes {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void MIMETypes_should_return_MimeType(){
       MIMETypes ext = MIMETypes.getMimeTypeWithExt("xml");
       assertTrue(ext.getContentType().equals("application/xml"));
    }
}
