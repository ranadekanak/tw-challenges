package com.example.springsocial.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Challenge4Output {
	public List<String> toolsToTakeSorted = new ArrayList<>();
	
}

class Challenge4Input {
	public List<Tool> tools = new ArrayList<>();
	public Integer maximumWeight;
}

class Tool {
	public String name;
	public Integer weight;
	public Integer value;
	
	public Integer getValue() {
		return value;
	}
}



@SuppressWarnings("deprecation")
public class Stage4 {

	public static void main(String[] args) throws Exception {
		Challenge4Input challenge = getChallenge();
		Challenge4Output output = new Challenge4Output();
		List<Tool> tools = challenge.tools;
		tools.sort(Comparator.comparing(Tool::getValue).reversed());
		
		for(Tool tool: tools) {
			if(challenge.maximumWeight > 0) {
				output.toolsToTakeSorted.add(tool.name);
				challenge.maximumWeight -= tool.weight;
				
				System.out.println(tool.name + " has value " + tool.value + ", and weight " + tool.weight +", remaining max weight " + challenge.maximumWeight );
				
			}
		}
		
		System.out.println(output.toolsToTakeSorted);
		postChallenge(output);
	}

	private static Challenge4Input getChallenge() throws Exception {
		Challenge4Input challenge = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpGet getRequest = new HttpGet("https://http-hunt.thoughtworks-labs.net/challenge/input");
			getRequest.addHeader("userId", "z4MuKDS1");
			HttpResponse response = httpClient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			System.out.println(apiOutput);
			Gson gson = new GsonBuilder().create();
			challenge = gson.fromJson(apiOutput, Challenge4Input.class);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return challenge;
	}

	private static void postChallenge(Challenge4Output challenge) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost postRequest = new HttpPost("https://http-hunt.thoughtworks-labs.net/challenge/output");
			postRequest.addHeader("userId", "z4MuKDS1");
			postRequest.addHeader("content-type", "application/json");
			StringEntity userEntity = new StringEntity(new Gson().toJson(challenge));
			postRequest.setEntity(userEntity);
			HttpResponse response = httpClient.execute(postRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			if (statusCode != 201) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

}
