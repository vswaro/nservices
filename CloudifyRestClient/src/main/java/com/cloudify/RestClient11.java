package com.cloudify;

public class RestClient11 {
	RestClientExecutor clientExecutor;
	public RestClient11(final String url, final String username, final String password) throws Exception {
		this.clientExecutor = createExecutor(url, username, password);
	}
	
	private RestClientExecutor createExecutor(final String url, final String username, final String password) throws Exception {
		return new RestClientExecutor(url, username, password);
	}
	
	public String get(String relativeUrl) throws Exception{
		return this.clientExecutor.get(relativeUrl);
	}
}