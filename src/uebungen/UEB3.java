package uebungen;

import java.io.InputStream;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.PluginTest;
import mywebserver.RequestImpl;
import mywebserver.ResponseImpl;

public class UEB3 {

	public void helloWorld() {

	}

	public Request getRequest(InputStream inputStream) {
		return new RequestImpl(inputStream);
	}

	public Response getResponse() {
		return new ResponseImpl();
	}

	public Plugin getTestPlugin() {
		return new PluginTest();
	}
}
