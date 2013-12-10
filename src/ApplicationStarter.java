package jOpendaylight;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ApplicationStarter {
	public static void main(String[] args)
			throws JsonProcessingException, MalformedURLException, IOException, RuntimeException {
		//	TODO
		OpendaylightClient oc = new OpendaylightClient("127.0.0.1");
		System.out.println(oc.getTopology());
	}
}
