package uebungen;

import BIF.SWE1.interfaces.Url;
import mywebserver.*;

public class UEB1 {

	public Url getUrl(String path) {
		return (new UrlImpl(path));
	}

	public void helloWorld() {
	}
}
