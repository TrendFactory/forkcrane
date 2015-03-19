package io.rebot.forkcrane.domain;

import io.rebot.forkcrane.crawler.HttpCrane;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBObject;

public class User{
	
	public String githubUniqId;
	public String name;
	public String email;
	public String provider;
	public Github github;
	
	private DBObject gitObj; 
	public User() {}
	
	public User(DBObject obj) {
		this.githubUniqId = (String) obj.get("githubUniqId");
		this.name = (String) obj.get("name");
		this.email = (String) obj.get("email");
		this.provider = (String) obj.get("provider");
		
		github = new Github(name, githubUniqId);
		this.gitObj = (DBObject) obj.get("github");
	}
	public void setGithub(){
		github.setGithub(gitObj);
	}
	
	public void setRepo() throws IOException, JSONException{
		String repoJson = HttpCrane.getGithubPage(github.repos_url);
		github.setRopos(repoJson);
	}
	
	public void setEvent() throws IOException, JSONException{
		String eventJson = HttpCrane.getGithubPage("https://api.github.com/users/" + this.name + "/events/public");
		github.setEvents(eventJson);
	}

	
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("githubUniqId", this.githubUniqId);
			json.put("name", this.name);
			json.put("email", this.email);
			json.put("provider", this.provider);
			
		} catch (JSONException e) { e.printStackTrace(); }
		
		return json;
	}
	
	public JSONObject getJSON_addGithub_byDUMMY() {
		JSONObject json = new JSONObject();
		try {
			json.put("githubUniqId", this.githubUniqId);
			json.put("name", this.name);
			json.put("email", this.email);
			json.put("provider", this.provider);
			
			json.put("github", github.getJSON());
		} catch (JSONException e) { e.printStackTrace(); }
		
		return json;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Github getGithub(){
		return this.github;
	}
}

