package com.clouidfy.rest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CloudifyRestClient {
    // HostOnly - 172.16.0.1 Bridge - 10.11.94.58
	private static final String HOSTURL = "http://172.16.0.1/api/v3.1";
	private final String USERNAME = "admin";
	private final String PASSWORD = "admin"; 

	private static RestClient restClient1;
	private static final Map<String,String> headers = new HashMap<String, String>(); { headers.put("Tenant", "default_tenant"); }

	public CloudifyRestClient() throws Exception {
		URL  url = new URL(HOSTURL);
		restClient1 = new RestClient(USERNAME, PASSWORD, url);
	}

	public static String processList(String operationOn) throws Exception {
		return processList(operationOn, null);
	}
	
	public static String processList(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;
		if(params!=null && params.entrySet().size()>0)
			relativeURL = relativeURL +  "?" + getParamStr(params);
		
		System.out.println(operationOn.toUpperCase()+ " LIST: "+relativeURL);
		return (String)restClient1.get(relativeURL, headers);
	}
	
	public static String processGet(String operationOn) throws Exception {
		return processGet(operationOn, null);
	}
	
	public static String processPut(String operationOn, Map<String, Object> params,String deploymentsId) throws Exception{
		String relativeURL =  null;
		if (operationOn != null && deploymentsId != null) {
			relativeURL = operationOn +"/"+deploymentsId;			
		}
		System.out.println( "print the relative url: "+relativeURL);		
		return (String)restClient1.put(relativeURL, params, headers);
	}
	public static String processGet(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;
		if(params!=null && params.entrySet().size()>0)
			relativeURL = relativeURL +  "?" + getParamStr(params);
		
		System.out.println(operationOn + "GET: "+relativeURL);
		return (String)restClient1.get(relativeURL, headers);
	}
	
	// update execution
	public static String processUpdateExecutions(String operationOn, String executionId, Map<String, String> params, String printShort) throws Exception {
		String operation= null;
		String relativeURL =  null;
		if ("updateexecutions".equals(operationOn)){
			 operation ="executions";
		}
		 Map<String, String> printShortMap = new HashMap<String, String>();
		if (printShort!=null && printShort.equals("-s")) {
			printShortMap.put("_include", "id");
		}
		if (printShortMap!= null && printShortMap.size() >0 ) {
		 relativeURL = operation+"/"+executionId+ "?"+getParamStr1(printShortMap);
		}
		else {
		 relativeURL = operation+"/"+executionId;
		}

		System.out.println(operationOn.toUpperCase()+ ": "+relativeURL);
		//return null;
		return (String)restClient1.patch(relativeURL, params, headers);
	}
	
	public static String processExecutions(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;

		System.out.println(operationOn.toUpperCase()+ ": "+relativeURL);
		//return null;
		return (String)restClient1.post(relativeURL, params, headers);
	}

	public String executeListCommand(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>1?inputTokens[1]:"";
		String printShort = inputTokens.length>2?inputTokens[2]:null;

		Map<String, String> params = new HashMap<String,String>();
		if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", "id");
		}
		
		response = CloudifyRestClient.processList(operationOn, params);
		return response;
	}
	
	public String executeExecutionsCommand(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>0?inputTokens[0]:"";
		String deploymentId = inputTokens.length>1?inputTokens[1]:"";
		String worflowId = inputTokens.length>2?inputTokens[2]:null;

		/*ObjectMapper mapper = new ObjectMapper();
        ObjectNode executionsNode = mapper.createObjectNode();
        executionsNode.put("deployment_id", deploymentId);
        executionsNode.put("workflow_id", worflowId);
        executionsNode.toString();*/
        
        Map<String, String> params = new HashMap<String,String>();
        params.put("deployment_id", deploymentId);
        params.put("workflow_id", worflowId);
		
		response = CloudifyRestClient.processExecutions(operationOn, params);
		return response;
	}
	
	public String updateExecutionsCommand(String[] inputTokens) throws Exception {
		String response = "";
		String operationOn = inputTokens.length>0?inputTokens[0]:"";
		String executionId = inputTokens.length>1?inputTokens[1]:"";
		//String worflowId = inputTokens.length>2?inputTokens[2]:null;
		String status = inputTokens.length>2?inputTokens[2]:"";
		String printShort = inputTokens.length>3?inputTokens[3]:null;
		System.out.println("pritn the executionId"+executionId);
		System.out.println("pritn the status"+status);
		
		//String printShort = inputTokens.length>2?inputTokens[2]:null;

		//Map<String, String> params = new HashMap<String,String>();
		/*if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", "id");
		}*/
		
		
		
		
		
		
		Map<String, String> params = new HashMap<String,String>();
		params.put("status", status);
		
		
		response = CloudifyRestClient.processUpdateExecutions(operationOn,executionId, params,printShort);
		return response;
		//return null;
		
	}

	public String executeGetCommand(String[] inputTokens) throws Exception{
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
		
		response = CloudifyRestClient.processGet(operationOn, params);
		return response;
	}
	
	public String executecreateDeploymentsCommand(String[] inputTokens) throws Exception {
		String response = "";
		String operationOn = inputTokens.length>0?inputTokens[0]:"";
		String deploymentsId = inputTokens.length>1?inputTokens[1]:"";
		String blueprintId = inputTokens.length>2?inputTokens[2]: "";				
		String agentPrimaryKeyPath = inputTokens.length>3?inputTokens[3]:"";
		String agentuser = inputTokens.length>4?inputTokens[4]:"";
		String serverIp = inputTokens.length>5?inputTokens[5]:"";
		String webserverPort = inputTokens.length>6?inputTokens[6]:"";
		Map<String, Object> inputsMap = new HashMap<String,Object>();
		if (agentPrimaryKeyPath!= null && !"".equals(agentPrimaryKeyPath)) {
			inputsMap.put("agent_private_key_path",agentPrimaryKeyPath);
		}
		if (agentuser!= null && !"".equals(agentuser)) {
			inputsMap.put("agent_user", agentuser);
		}
		if (serverIp != null && !"".equals(serverIp)) {
			inputsMap.put("server_ip", serverIp);
		}
		if (webserverPort != null && !"".equals(webserverPort)) {
			inputsMap.put("webserver_port", webserverPort);		
		}
			Map<String,Object> params = new HashMap<String,Object>();
			if (blueprintId!= null) {
			params.put("blueprint_id", blueprintId);
			}			
			if (inputsMap!= null ) {
			params.put("inputs", inputsMap);
			}			
		response = CloudifyRestClient.processPut(operationOn, params,deploymentsId);
		return response;
	}
	public static String getParamStr1(Map<String, String> params) {
		StringBuilder paramsBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			paramsBuilder.append(entry.getKey()).append("=").append(entry.getValue());
		} 
		return paramsBuilder.toString();
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
				response = client.executeListCommand(inputTokens);
				break;
			case "get":
				response = client.executeGetCommand(inputTokens);
				break;
			case "executions":
				response = client.executeExecutionsCommand(inputTokens);
				break;
			case "updateexecutions":
				response = client.updateExecutionsCommand(inputTokens);
				break;
			case "deployments":
				response = client.executecreateDeploymentsCommand(inputTokens);
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
		System.out.println("Usage: executions <deployment-id> [workflowId] \n \t worflowid: install|uninstall");
		System.out.println("Usage: executions <execution-id> [status] \n \t status: cancelled");
	}

}