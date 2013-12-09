package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

public class ApplicationStarter {
	public static void main(String[] args) throws MalformedURLException, JSONException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient("127.0.0.1");
		System.out.println(oc.getTopology());
	}
}
