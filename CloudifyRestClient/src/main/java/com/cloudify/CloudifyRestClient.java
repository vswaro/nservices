package com.cloudify;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CloudifyRestClient {

	private static final String HOSTURL = "http://172.16.0.1/api/v3.1";
	private final String USERNAME = "admin";
	private final String PASSWORD = "admin"; 

	private RestClient restClient1;
	private final Map<String,String> headers = new HashMap<String, String>(); { headers.put("Tenant", "default_tenant"); }

	public CloudifyRestClient() throws Exception {
		URL  url = new URL(HOSTURL);
		restClient1 = new RestClient(USERNAME, PASSWORD, url);
	}

	public String getBlueprintsList() throws Exception {
		return (String)restClient1.get("blueprints?_include=id", headers);
	}

	public String getDeploymentsList() throws Exception {
		return (String)restClient1.get("deployments?_include=id", headers);
	}

	public String getExecutionList() throws Exception {
		return (String)restClient1.get("executions?_include=id", headers);
	}

	public String getNodeInstancesList() throws Exception {
		return (String)restClient1.get("node-instances?_include=id", headers);
	}

	public String getNodesList() throws Exception {
		return (String)restClient1.get("nodes?_include=id", headers);
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
			String key = scanner.nextLine();
			key = key!=null?key.toLowerCase():key;

			switch (key) {
			case "list blueprints":
				response = client.getBlueprintsList();
				break;
			case "list deployments":
				response = client.getDeploymentsList();
				break;
			case "list executions":
				response = client.getExecutionList();
				break;
			case "list node-instances":
				response = client.getNodeInstancesList();
				break;
			case "list nodes":
				response = client.getNodesList();
				break;
			case "help":
				printHelp();
				break;
			case "exit":
				flag = false;
				break;
			default:
				flag = false;
				break;
			}
			if(!key.equals("help") && !key.equals("exit") && !key.equals("")) {
				System.out.println("Printing response from host("+ HOSTURL +") ....");
				System.out.println(response);
			}
		}
		scanner.close();

	}

	private static void printHelp() {
		System.out.println("Help : These commands are defined internally. Type 'help' or'exit'");
		System.out.println("Usage: list [options]\n \t options: blueprints|deployments|executions|node-instances|nodes");
		System.out.println("Usage: get [options]\n \t options: blueprints|deployments|executions|node-instances|nodes");
	}

}
