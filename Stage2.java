package com.example.springsocial.util;

import java.util.ArrayList;
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

@SuppressWarnings("deprecation")
class ChallengeOutput {
	public List<String> toolsFound = new ArrayList<String>();
}

class ChallengeInput {

	public String hiddenTools;
	public List<String> tools;
}
@SuppressWarnings("deprecation")
public class Stage2 {

	public static void main(String[] args) throws Exception {

		ChallengeInput challenge2 = getChallenge();
		ChallengeOutput output = new ChallengeOutput();

		String input = challenge2.hiddenTools;
		List<String> tools = challenge2.tools;
		for (String tool : tools) {
			int found = 0;
			int count = 0;
			for (char temp : tool.toCharArray()) {
				for (; count < input.length(); count++) {
					if (temp == input.charAt(count)) {
						found++;
						break;
					}
				}
			}
			if (tool.length() == found) {
				System.out.println(tool);
				output.toolsFound.add(tool);
			}
		}

		postChallenge(output);
	}

	private static ChallengeInput getChallenge() throws Exception {
		ChallengeInput challenge = null;
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
			challenge = gson.fromJson(apiOutput, ChallengeInput.class);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return challenge;
	}

	private static void postChallenge(ChallengeOutput challenge) throws Exception {
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
