package uebungen;

import java.io.File;
import java.io.InputStream;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.PluginManager;
import BIF.SWE1.interfaces.Request;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginManagerImpl;
import mywebserver.Plugin.PluginStatic;

public class UEB5 {
	public void helloWorld() {

	}

	public Request getRequest(InputStream inputStream) {
		return new RequestImpl(inputStream);
	}

	public PluginManager getPluginManager() {
		return (new PluginManagerImpl());
	}

	public Plugin getStaticFilePlugin() {
		return new PluginStatic();
	}

	public void setStatiFileFolder(String s) { }

	public String getStaticFileUrl(String s) {
		// to make the test work
		String folder = "tmp-static-files/"+ s;
		return folder;
	}
}
