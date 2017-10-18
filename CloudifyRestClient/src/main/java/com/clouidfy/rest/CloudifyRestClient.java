package com.clouidfy.rest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CloudifyRestClient {
    // HostOnly - 172.16.0.1 Bridge - 10.11.94.58
	private static final String HOSTURL = "http://10.11.94.58/api/v3.1/";
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
		
		System.out.println("Response from Cloudify host("+HOSTURL+relativeURL+")....");
		return (String)restClient1.get(relativeURL, headers);
	}
	
	public static String processGet(String operationOn) throws Exception {
		return processGet(operationOn, null);
	}
	public static String processGet(String operationOn, Map<String, String> params) throws Exception {
		String relativeURL = operationOn;
		if(params!=null && params.entrySet().size()>0)
			relativeURL = relativeURL +  "?" + getParamStr(params);
		
		System.out.println("Response from Cloudify host("+HOSTURL+relativeURL+")....");
		return (String)restClient1.get(relativeURL, headers);
	}
	
	public static String processExecutions(String operationOn, Map<String, Object> params) throws Exception {
		String relativeURL = operationOn;

		System.out.println("Response from Cloudify host("+HOSTURL+relativeURL+")....");
		return (String)restClient1.post(relativeURL, params, headers);
	}

	public String executeListCommand(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>1?inputTokens[1]:"";
		String printShort = inputTokens.length>2?inputTokens[2]:null;
		String printShortDetailId = inputTokens.length>3?inputTokens[3]:null;
		printShortDetailId = printShortDetailId==null?"id":printShortDetailId;
		
		Map<String, String> params = new HashMap<String,String>();
		if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", printShortDetailId);
		}
		
		response = CloudifyRestClient.processList(operationOn, params);
		return response;
	}
	
	public String executeGetCommand(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>1?inputTokens[1]:"";
		String inputId = inputTokens.length>2?inputTokens[2]:null;
		String printShort = inputTokens.length>3?inputTokens[3]:null;
		String printShortDetailId = inputTokens.length>4?inputTokens[4]:null;
		printShortDetailId = printShortDetailId==null?"id":printShortDetailId;
		
		Map<String, String> params = new HashMap<String,String>();
		if(inputId!=null)
			params.put("id", inputId);
		if (printShort!=null && printShort.equals("-s")) {
			params.put("_include", printShortDetailId);
		}
		
		response = CloudifyRestClient.processGet(operationOn, params);
		return response;
	}
	
	public String executeExecutionsCommand(String[] inputTokens) throws Exception{
		String response = "";
		String operationOn = inputTokens.length>0?inputTokens[0]:"";
		String deploymentId = inputTokens.length>1?inputTokens[1]:"";
		String worflowId = inputTokens.length>2?inputTokens[2]:null;

        Map<String, Object> params = new HashMap<String,Object>();
        params.put("deployment_id", deploymentId);
        params.put("workflow_id", worflowId);
		
		response = CloudifyRestClient.processExecutions(operationOn, params);
		return response;
	}
	public static String getParamStr(Map<String, String> params) {
		StringBuilder paramsBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			paramsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		} 
		return paramsBuilder.toString();
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
	
	public static String processPut(String operationOn, Map<String, Object> params,String deploymentsId) throws Exception{
		String relativeURL =  null;
		if (operationOn != null && deploymentsId != null) {
			relativeURL = operationOn +"/"+deploymentsId;			
		}	
		return (String)restClient1.put(relativeURL, params, headers);
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
				try {
					response = client.executeListCommand(inputTokens);
				} catch (Exception e) {
					response = e.getMessage();
				}
				break;
			case "get":
				try {
					response = client.executeGetCommand(inputTokens);
				} catch (Exception e) {
					response = e.getMessage();
				}
				break;
			case "executions":
				try {
					response = client.executeExecutionsCommand(inputTokens);
				} catch (Exception e) {
					response = e.getMessage();
				}
				break;
			case "deployments":
				try {
				response = client.executecreateDeploymentsCommand(inputTokens);
				}catch(Exception e) {
					response = e.getMessage();
				}
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
		System.out.println("Commands Help");
		System.out.println("Usage: 'help' or'exit'");
		System.out.println("Usage: list [options]\n \t options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups|events \n\t inputs: -s <json elements> -> prints minimal details (or) by default prints all \n\t Example: list deployments -s id,deployment_id");
		System.out.println("Usage: get [options] [inputs]\n \t options: blueprints|deployments|executions|node-instances|nodes|plugins|tentants|users|user-groups|status|version \n \t inputs: -s <json elements> -> prints minimal details (or) by default prints all \n\t Example: get executions 953f7e7a-0fb8-4af4-9899-a5f5c0595a3f -s id");
		System.out.println("Usage: executions <deployment-id> [workflowId] \n\t worflowId: install|uninstall \n\t Example: executions nodecellar-docker-deploy1 install ");
		System.out.println("Usage: deployments <deployment-id> <blueprint-id> inputs[] \n\t Inputs: agent_private_key_path && agent_user && server_ip && webserver_port \n\t Example: deployments helloworldDeploy helloworld /home/cloudify-cert.pem cloudify 10.0.2.15 8081 ");
	}

}
