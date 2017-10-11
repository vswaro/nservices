package com.cloudify;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CloudifyRestClient {
    // HostOnly - 172.16.0.1 Bridge - 10.11.94.58
	private static final String HOSTURL = "http://10.11.94.58/api/v3.1";
	private final String USERNAME = "admin";
	private final String PASSWORD = "admin"; 

	private static RestClient restClient1;
	private static final Map<String,String> headers = new HashMap<String, String>(); { headers.put("Tenant", "default_tenant"); }

	public CloudifyRestClient() throws Exception {
		URL  url = new URL(HOSTURL);
		restClient1 = new RestClient(USERNAME, PASSWORD, url);
	}

	public static String getList(String operationOn) throws Exception {
		return getList(operationOn, null);
	}
	
	public static String getList(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;
		if(params!=null && params.entrySet().size()>0)
			relativeURL = relativeURL +  "?" + getParamStr(params);
		
		System.out.println(operationOn.toUpperCase()+ " LIST: "+relativeURL);
		return (String)restClient1.get(relativeURL, headers);
	}
	
	public static String getGet(String operationOn) throws Exception {
		return getGet(operationOn, null);
	}
	public static String getGet(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;
		if(params!=null && params.entrySet().size()>0)
			relativeURL = relativeURL +  "?" + getParamStr(params);
		
		System.out.println(operationOn + "GET: "+relativeURL);
		return (String)restClient1.get(relativeURL, headers);
	}

	public String executeCommandList(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>1?inputTokens[1]:"";
		String printShort = inputTokens.length>2?inputTokens[2]:null;

		Map<String, String> params = new HashMap<String,String>();
		if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", "id");
		}
		
		response = CloudifyRestClient.getList(operationOn, params);
		return response;
	}

	public String executeCommandGet(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>1?inputTokens[1]:"";
		String inputId = inputTokens.length>2?inputTokens[2]:null;
		String printShort = inputTokens.length>3?inputTokens[3]:null;
		
		Map<String, String> params = new HashMap<String,String>();
		if(inputId!=null)
			params.put("id", inputId);
		if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", "id");
		}
		
		response = CloudifyRestClient.getGet(operationOn, params);
		return response;
	}
	
	public static String getParamStr(Map<String, String> params) {
		StringBuilder paramsBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			paramsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		} 
		return paramsBuilder.toString();
	}

	public static void main(String[] args) throws Exception {
		CloudifyRestClient client = new CloudifyRestClient();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Cloudify Rest Client (Host:"+HOSTURL+")");
		System.out.println("----------------------------------------------------------------------");
		printHelp();

		String response = "";
		boolean flag = true;
		while (flag) {
			System.out.println(">>");
			String input = scanner.nextLine();
			input = input!=null?input.toLowerCase():input;

			String[] inputTokens = input.split(" ");
			String operation = "";
			if(inputTokens.length>0) {
				operation = inputTokens[0];
			}

			switch (operation) {
			case "list":
				response = client.executeCommandList(inputTokens);
				break;
			case "get":
				response = client.executeCommandGet(inputTokens);
				break;
			case "help":
				printHelp();
				break;
			case "exit":
				flag = false;
				break;
			default:
				operation = "exit";
				flag = false;
				break;
			}
			if(!input.equals("help") && !input.equals("exit") && !input.equals("")) {
				System.out.println(response);
			}
			
			if(operation.equals("exit")) {
				System.out.println("Logged out.");
			}
		}
		scanner.close();

	}

	private static void printHelp() {
		System.out.println("Help : These commands are defined internally. Type 'help' or'exit'");
		System.out.println("Usage: list [options]\n \t options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups");
		System.out.println("Usage: get [options] [inputs]\n \t options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups|status|version \n \t inputs: -s  prints short details, default prints all");
	}

}
