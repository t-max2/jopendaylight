package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ApplicationStarter {
	public static void main(String[] args)
			throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient();
		//	oc.addUserLink("link4", "00:00:00:00:00:00:00:03", 2, "00:00:00:00:00:00:00:04", 1);
		//	oc.deleteUserLink("link2");
		//	oc.deleteUserLink("link3");
		System.out.println(oc.getUserLinks());
	}
}
