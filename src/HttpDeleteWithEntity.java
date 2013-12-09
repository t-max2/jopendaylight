package jOpendaylight;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpDeleteWithEntity extends HttpEntityEnclosingRequestBase{
	//	constructor
	HttpDeleteWithEntity(String uri){
		super();
		this.setURI(URI.create(uri));
	}
	
	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return "DELETE";
	}
}
