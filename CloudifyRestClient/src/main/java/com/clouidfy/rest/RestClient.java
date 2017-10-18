
package com.clouidfy.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {

	private static final ObjectMapper PROJECT_MAPPER = new ObjectMapper();
	private static final String FORWARD_SLASH = "/";
	private static final String MIME_TYPE_APP_JSON = "application/json";

	private final HttpClient httpClient;
	private final URL url;
	private final String urlStr;

	public RestClient(final String username, final String password, final URL url) throws Exception {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);

		this.httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build(); 
		this.url = url;
		this.urlStr = createUrlStr();
	}

	public final Object get(final String relativeUrl) throws Exception {
		return get(relativeUrl, null);
	}

	public final Object get(final String relativeUrl, final Map<String, String> headers) throws Exception {
		final String url = getFullUrl(relativeUrl);
		final HttpGet httpMethod = new HttpGet(url);
		if (headers != null) {
			for (final Map.Entry<String, String> entry : headers.entrySet()) {
				httpMethod.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return executeHttpMethod(httpMethod);
	}

	public final Object post(final String relativeUrl) throws Exception {
		return post(relativeUrl, null, null);
	}

	public final Object post(final String relativeUrl, final Map<String, Object> params, final Map<String, String> headers) throws Exception {
		final HttpPost httppost = new HttpPost(getFullUrl(relativeUrl));
		if (headers != null) {
			for (final Map.Entry<String, String> entry : headers.entrySet()) {
				httppost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		if (params != null) {
			HttpEntity entity;
			try {
				final String json = RestClient.mapToJson(params);
				entity = new StringEntity(json, "UTF-8");
				httppost.setEntity(entity);
				httppost.setHeader(HttpHeaders.CONTENT_TYPE, MIME_TYPE_APP_JSON);
			} catch (final IOException e) {
				throw new Exception(e);
			}
		}
		return executeHttpMethod(httppost);
	}

	public final Object delete(final String relativeUrl, final Map<String, String> params) throws Exception {
		final HttpDelete httpdelete = new HttpDelete(getFullUrl(relativeUrl));
		if (params != null) {
			for (final Map.Entry<String, String> entry : params.entrySet()) {
				httpdelete.getParams().setParameter(entry.getKey(), entry.getValue());
			}
		}
		return executeHttpMethod(httpdelete);
	}

	private Object executeHttpMethod(final HttpRequestBase httpMethod) throws Exception {
		String responseBody;
		try {
			final HttpResponse response = httpClient.execute(httpMethod);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				final String reasonPhrase = response.getStatusLine().getReasonPhrase();
				throw new Exception(reasonPhrase);

			}
			return getResponse(response);
		} catch (final IOException e) {
			throw new Exception(e);
		} finally {
			httpMethod.abort();
		}
	}

	public static String getResponseBody(final HttpResponse response) throws Exception {
		InputStream instream = null;
		try {
			final HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new Exception();
			}
			instream = entity.getContent();
			return getStringFromStream(instream);
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getResponse(final HttpResponse httpResponse) throws Exception {
		final String responseBody = getResponseBody(httpResponse);
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(responseBody, Object.class);

			String indentedResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			return indentedResponse;
		} catch (IOException e) {
			System.out.println("failed to read the responseBody (of request to )."
					+ ", error was " + e.getMessage());
			throw new Exception();
		}
	}

	/** Utils **/
	private String createUrlStr() {
		String urlStr = url.toExternalForm();
		if (!urlStr.endsWith(FORWARD_SLASH)) {
			urlStr += FORWARD_SLASH;
		}
		return urlStr;
	}

	private String getFullUrl(final String relativeUrl) {
		String safeRelativeURL = relativeUrl;
		if (safeRelativeURL.startsWith(FORWARD_SLASH)) {
			safeRelativeURL = safeRelativeURL.substring(1);
		}
		return urlStr + safeRelativeURL;
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
	
	public final Object put(final String relativeUrl, final Map<String, Object> params,final Map<String, String> headers) throws Exception {
		final HttpPut httpput = new HttpPut(getFullUrl(relativeUrl));
		if (headers != null) {
			for (final Map.Entry<String, String> entry : headers.entrySet()) {
				httpput.addHeader(entry.getKey(), entry.getValue());
			}
		}
		if (params != null) {
			HttpEntity entity;
			try {
				final String json = RestClient.mapToJson(params);				
				entity = new StringEntity(json, "UTF-8");
				httpput.setEntity(entity);
				httpput.setHeader(HttpHeaders.CONTENT_TYPE, MIME_TYPE_APP_JSON);
			} catch (final IOException e) {
				throw new Exception(e);
			}
		}		
		return executeHttpMethod(httpput);
	}

	public static String mapToJson(final Map<String, ?> map) throws IOException {
		return PROJECT_MAPPER.writeValueAsString(map);
	}

}
