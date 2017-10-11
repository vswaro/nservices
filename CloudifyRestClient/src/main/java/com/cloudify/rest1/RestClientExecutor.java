package com.cloudify.rest1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientExecutor {

	private static final String FORWARD_SLASH = "/";

	private final HttpClient httpClient;
	private String urlStr;

	public RestClientExecutor(final String url, final String username, final String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);

		this.httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build(); 
		this.urlStr = url;
		if (!this.urlStr.endsWith(FORWARD_SLASH)) {
			this.urlStr += FORWARD_SLASH;
		}
	}

	public String get(final String relativeUrl) throws Exception {
		String fullUrl = getFullUrl(relativeUrl);
		final HttpGet getRequest = new HttpGet(fullUrl);
		getRequest.addHeader("Tenant", "default_tenant");
		System.out.println("Execute get request to " + relativeUrl);
		return executeRequest(getRequest);
	}

	public String post(final String relativeUrl, final Object postBody) throws Exception {
			final HttpEntity stringEntity;
			String jsonStr;
			try {
				jsonStr = new ObjectMapper().writeValueAsString(postBody);
				stringEntity = new StringEntity(jsonStr, "UTF-8");
			} catch (final IOException e) {
				throw  e;
			}

			final HttpPost postRequest = new HttpPost(getFullUrl(relativeUrl));
			postRequest.addHeader("Tenant", "default_tenant");
			postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			postRequest.setEntity(stringEntity);
			return executeRequest(postRequest);
	}


	private String getFullUrl(final String relativeUrl) {
		String safeRelativeURL = relativeUrl;
		if (safeRelativeURL.startsWith(FORWARD_SLASH)) {
			safeRelativeURL = safeRelativeURL.substring(1);
		}
		return urlStr + safeRelativeURL;
	}

	private String executeRequest(final HttpRequestBase request) throws Exception {
		HttpResponse httpResponse = null;
		httpResponse = httpClient.execute(request);
		String url = request.getURI().toString();
		return getResponse(httpResponse, url);
	}

	private String getResponse(final HttpResponse httpResponse, final String url) throws Exception {
		final String responseBody = getResponseBody(httpResponse);
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(responseBody, Object.class);

			String indentedResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			return indentedResponse;
		} catch (IOException e) {
			System.out.println("failed to read the responseBody (of request to " + url + ")."
					+ ", error was " + e.getMessage());
			throw new Exception();
		}
	}

	private String getResponseBody(final HttpResponse response)throws Exception {
		InputStream instream = null;
		try {
			final HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			instream = entity.getContent();
			return getStringFromStream(instream);
		} catch (IOException e) {
			throw new Exception();
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static String getStringFromStream(final InputStream is) throws IOException { 
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is)); 
		final StringBuilder sb = new StringBuilder(); 
		String line = null; 
		while ((line = bufferedReader.readLine()) != null) { 
			sb.append(line); 
		} 
		return sb.toString(); 
	} 
}
