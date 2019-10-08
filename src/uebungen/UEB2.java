package uebungen;

import java.io.InputStream;

import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.RequestImpl;
import mywebserver.ResponseImpl;
import mywebserver.UrlImpl;

public class UEB2 {

	public void helloWorld() {

	}

	public Url getUrl(String s) {
		return (new UrlImpl(s));
	}

	public Request getRequest(InputStream inputStream) {
		return (new RequestImpl(inputStream));
	}

	public Response getResponse() {
		return (new ResponseImpl());
	}
}
