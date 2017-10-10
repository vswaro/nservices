package com.cloudify;

import java.util.Scanner;

public class CloudifyRestClient2 {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		String url = "http://172.16.0.1/api/v3.1";
		try {
			RestClient restClient = new RestClient(url, "admin", "admin");
			boolean flag = true;
	        while (flag) {
				System.out.println("Cloudify Rest Client");
				System.out.println("Endpoint: "+url);
				System.out.println("------------------------------");
				System.out.println("1 - List Blueprints");
				System.out.println("2 - List Deployments");
				System.out.println("3 - List Events");
				System.out.println("4 - List Node Instances");
				System.out.println("5 - List Nodes");
				System.out.println("6 - List Plugins");
				System.out.println("7 - List Tenants");
				System.out.println("8 - List Users");
				System.out.println("9 - List Executions");
	            System.out.print("Enter : ");
	            String key = scanner.nextLine();
	            String response = "";
	            switch (key) {
				case "1":
					response = restClient.get("blueprints?_include=id");
					break;
				case "2":
					response = restClient.get("deployments?_include=id");
					break;
				case "3":
					response = restClient.get("events?_include=id");
					break;
				case "4":
					response = restClient.get("node-instances?_include=id");
					break;
				case "5":
					response = restClient.get("nodes?_include=id");
					break;
				case "6":
					response = restClient.get("plugins?_include=id");
					break;
				case "7":
					response = restClient.get("tenants?_include=id");
					break;
				case "8":
					response = restClient.get("users?_include=id");
					break;
				case "9":
					response = restClient.get("executions?_include=id,blueprint_id,deployment_id");
					break;
				default:
					flag = false;
					break;
				}
	            System.out.println(response);
	            System.out.println("------------------------------");
	        }
	        System.out.println("Exit");
	        
			
		} catch (Exception e) {
			System.out.println(e);
		}
		scanner.close();
		
	}
}
