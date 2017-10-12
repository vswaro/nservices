package com.clouidfy.rest;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	public static void main(String[] args) throws Exception{

		Map<String, Object> params = new HashMap<String,Object>();

		params.put("blueprint_id", "");
		Map<String, String> inputsMap = new HashMap<String,String>();
		//if (agentPrimaryKeyPath != null) {
		inputsMap.put("agent__private_key_path", "/home/cloudify-cer.pem");
		inputsMap.put("agent_user", "cloudify");
		inputsMap.put("server_ip", "10.0.2.15");
		inputsMap.put("webserver_port", "8081"); 
		params.put("input", inputsMap);

		ObjectMapper PROJECT_MAPPER = new ObjectMapper();
		System.out.println(PROJECT_MAPPER.writeValueAsString(params));
	}
}
