package jOpendaylight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestUtils {
	//	-------------------
	//		GET Method
	//	-------------------
	
	//	base GET method
	public static String doGet(String urlString, String account, String password) throws MalformedURLException, IOException, RuntimeException{
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		
		//	handle HTTP Basic authentication if any
		if(account != null && password != null){
			String encoding = Base64.encodeBase64String((account + ":" + password).getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encoding);
		}
		
		if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
			String temp;
			String result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			while( (temp = br.readLine())!= null ){
				result = result + temp;
			}
			
			conn.disconnect();
			return result;
		}
		else if(conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT){
			//	no content but OK, just no response entity
			//	for clear static flow entries
			return "";
		}
		else{
			//	NOT OK
			throw new RuntimeException("Failed: HTTP error code : "+ conn.getResponseCode());
		}
	}
	
	public static String doGet(String urlString) throws MalformedURLException, IOException, RuntimeException{
		return doGet(urlString, null, null);
	}
	
	public static String doGet(String urlString, Map<String, String> paraMap) throws MalformedURLException, IOException, RuntimeException{
		return doGet(urlString + "?" + prepareGetParameterString(paraMap));
	}
	
	private static String prepareGetParameterString(Map<String, String> paraMap){
		String result = "";
		boolean head = true;
		
		for(Map.Entry<String, String> entry: paraMap.entrySet()){
			if(entry.getValue() != null){
				//	prefix
				if(head){
					head = false;
				}
				else{
					result += "&";
				}
				
				//	content append
				result += entry.getKey() + "=" + entry.getValue();
			}
		}
		
		return result;
	}
	
	//	--------------------
	//		POST method
	//	--------------------
	
	public static String doPost(String urlString, String parameterString) throws MalformedURLException, IOException, RuntimeException{
		return doPost(urlString, parameterString, null, null);
	}
	
	//	base Post method
	public static String doPost(String urlString, String paraString, String account, String password) throws MalformedURLException, IOException, RuntimeException{
		URL url;
		OutputStream os;
		//	String parameterString;
		HttpURLConnection conn;
		
		url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json");
		
		//	handle HTTP Basic authentication if any
		if(account != null && password != null){
			String encoding = Base64.encodeBase64String((account + ":" + password).getBytes());
			conn.setRequestProperty("Authorization", "Basic " + encoding);
		}

		if(paraString != null && !paraString.equals("")){
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			
			os = conn.getOutputStream();
			os.write(paraString.getBytes());
			os.flush();
		}
		
		//	FIXME: maybe more status codes?
		if(conn.getResponseCode() == HttpURLConnection.HTTP_OK || 
				conn.getResponseCode() == HttpURLConnection.HTTP_CREATED){
			String temp;
			String result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			while( (temp = br.readLine())!= null ){
				result = result + temp;
			}
			
			conn.disconnect();
			return result;
		}
		else{
			//	NOT OK
			throw new RuntimeException("Failed: HTTP error code : "+ conn.getResponseCode());
		}
	}
	
	//	----------------------
	//		DELETE method
	//	----------------------
	
	//	HTTP DELETE with source id in URI version(Normal Delete)
	public static String doDelete(String urlString) throws ClientProtocolException, IOException{
		return doDelete(urlString, null, null);
	}
	
	//	HTTP DELETE with source id in URI version(Normal Delete), base method
	public static String doDelete(String urlString, String account, String password) throws ClientProtocolException, IOException{
		HttpClient hc;
		HttpResponse response;
		HttpEntity responseEntity;
		HttpDelete deleteRequest;
		
		//	construct http client
		hc = new DefaultHttpClient();
		
		//	set request
		deleteRequest = new HttpDelete(urlString);
		deleteRequest.setHeader("Accept", "application/json");
		
		//	handle HTTP Basic authentication if any
		if(account != null && password != null){
			String encoding = Base64.encodeBase64String((account + ":" + password).getBytes());
			deleteRequest.setHeader("Authorization", "Basic " + encoding);
		}
		
		//	get response
		response = hc.execute(deleteRequest);
		responseEntity = response.getEntity();
		
		//	return content
		if(responseEntity != null){
			//	FIXME: add more HTTP OK codes if needed
			if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT){
				return EntityUtils.toString(responseEntity);
			}
			else{
				//	NOT OK
				throw new RuntimeException("Failed: HTTP error code: " + response.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(responseEntity));
			}
		}
		else
			return null;
	}
	
	//	HTTP DELTE with String entity 
	public static String doDelete(String urlString, String paraString) throws ClientProtocolException, IOException{
		HttpClient hc;
		HttpResponse response;
		HttpEntity responseEntity;
		StringEntity paraStringEntity;
		HttpDeleteWithEntity deleteRequest;
		
		//	construct http client
		hc = new DefaultHttpClient();
		
		//	set request
		paraStringEntity = new StringEntity(paraString);
		
		deleteRequest = new HttpDeleteWithEntity(urlString);
		deleteRequest.setEntity(paraStringEntity);
		
		//	get response
		response = hc.execute(deleteRequest);
		responseEntity = response.getEntity();
		
		//	return content
		if(responseEntity != null)
			return EntityUtils.toString(responseEntity);
		else
			return null;
	}
	
	//	-------------------
	//		PUT method
	//	-------------------
	
	public static String doPut(String urlString, String paraString) throws ClientProtocolException, IOException{
		return doPut(urlString, paraString, null, null);
	}
	
	//	base PUT method
	public static String doPut(String urlString, String paraString, String account, String password) throws ClientProtocolException, IOException{
		HttpClient hc;
		HttpResponse response;
		HttpEntity responseEntity;
		StringEntity paraStringEntity;
		HttpPut putRequest;
		
		//	construct http client
		hc = new DefaultHttpClient();
		
		//	set request
		paraStringEntity = new StringEntity(paraString);
		paraStringEntity.setContentType("application/json");
		
		putRequest = new HttpPut(urlString);
		putRequest.setHeader("Accept", "application/json");
		putRequest.setEntity(paraStringEntity);
		
		//	handle HTTP Basic authentication if any
		if(account != null && password != null){
			String encoding = Base64.encodeBase64String((account + ":" + password).getBytes());
			putRequest.setHeader("Authorization", "Basic " + encoding);
		}
		
		//	get response
		response = hc.execute(putRequest);
		responseEntity = response.getEntity();
		
		//	return content
		if(responseEntity != null){
			int statusCode = response.getStatusLine().getStatusCode();
			
			//	FIXME: add more HTTP OK codes if needed
			if(statusCode == HttpURLConnection.HTTP_OK || 
					statusCode == HttpURLConnection.HTTP_CREATED){
				return EntityUtils.toString(responseEntity);
			}
			else{
				//	NOT OK
				throw new RuntimeException("Failed: HTTP error code: "+ response.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(responseEntity));
			}
		}
		else
			return null;
	}
}
