package io.rebot.forkcrane.crawler;

import io.rebot.forkcrane.domain.Constant;

import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HttpCrane {
	
	private static final String GITHUB_URL = "https://api.github.com/";
	private static final String ACCESS_TOKEN_STRING = "?access_token=b746ceb6a8bfab2f504a63c944b9c78d1f4dccb4";

	static int apiCount = 0;
	private HttpCrane() { }
	
	public static String getGithubPage(String url) throws IOException {
		
		apiCount++;
		//if(Constant.DEBUGING) ;
		System.out.println("[API Count] :" +apiCount);
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(url + ACCESS_TOKEN_STRING).build();

		Response response = client.newCall(request).execute();

		return response.body().string();
	}
}
