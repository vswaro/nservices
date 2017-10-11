package com.cloudify;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CloudifyRestClient1 {

	public static void main(String[] args) {
		
		CloudifyRestClient1 client = new CloudifyRestClient1();
		try {
			client.getBlueprints();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	public void getBlueprints() throws Exception {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
		provider.setCredentials(AuthScope.ANY, credentials);

		String url = "http://172.16.0.1/api/v3.1/blueprints";
		
		HttpHost httpHost = new HttpHost("172.16.0.1", 0, "80");

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider)
				.build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("Tenant", "default_tenant");

		HttpResponse response = client.execute(request);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		//System.out.println(result.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(result.toString(), Object.class);
		
		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		System.out.println(indented);
		
		
	}



}
