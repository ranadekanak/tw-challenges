package com.example.springsocial.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
class Challenge3Output {
	public List<ToolsSortedOnUsage> toolsSortedOnUsage = new ArrayList<>();

	@Override
	public String toString() {
		return "Challenge3Output [toolsSortedOnUsage=" + toolsSortedOnUsage + "]";
	}
	
}

class Challenge3Input {
	public List<ToolUsage> toolUsage = new ArrayList<>();
}

class ToolUsage {
	public String name;
	public String useStartTime;
	public String useEndTime;
}

class ToolsSortedOnUsage {
	public String name;
	public Integer timeUsedInMinutes;
	public Integer getTimeUsedInMinutes() {
		return timeUsedInMinutes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTimeUsedInMinutes(Integer timeUsedInMinutes) {
		this.timeUsedInMinutes = timeUsedInMinutes;
	}
	@Override
	public String toString() {
		return "ToolsSortedOnUsage [name=" + name + ", timeUsedInMinutes=" + timeUsedInMinutes + "]";
	}
	
	
}

@SuppressWarnings("deprecation")
public class Stage3 {

	public static void main(String[] args) throws Exception {

		Challenge3Input challenge = getChallenge();
		Challenge3Output output = new Challenge3Output();

		Map<String, Integer> timeTaken = new HashMap<>();
		for (ToolUsage temp : challenge.toolUsage) {
			if (!timeTaken.containsKey(temp.name)) {
				timeTaken.put(temp.name, timeDiff(temp.useStartTime, temp.useEndTime));
			} else {
				timeTaken.put(temp.name, timeTaken.get(temp.name) + timeDiff(temp.useStartTime, temp.useEndTime));
			}
		}

		timeTaken.forEach((k, v) -> {
			ToolsSortedOnUsage temp = new ToolsSortedOnUsage();
			temp.name = k;
			temp.timeUsedInMinutes = v;
			output.toolsSortedOnUsage.add(temp);
		});
		output.toolsSortedOnUsage.sort(Comparator.comparing(ToolsSortedOnUsage::getTimeUsedInMinutes).reversed());
		System.out.println(output);
		postChallenge(output);
	}

	private static Integer timeDiff(String useStartTime, String useEndTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		long start = format.parse(useStartTime).getTime();
		long end = format.parse(useEndTime).getTime();

		return Integer.parseInt("" + (end - start) / (1000 * 60));
	}

	private static Challenge3Input getChallenge() throws Exception {
		Challenge3Input challenge = null;
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
			challenge = gson.fromJson(apiOutput, Challenge3Input.class);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return challenge;
	}

	private static void postChallenge(Challenge3Output challenge) throws Exception {
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
