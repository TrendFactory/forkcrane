package io.rebot.forkcrane.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class PerformedEvent {
	String type;
	String created_at;
	String repo_url;
	String githubUniqId;
	
	public PerformedEvent(String githubUniqId){
		
	}
	
	public JSONObject getJSON() throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put("githubUniqId", githubUniqId);
		json.put("type", type);
		json.put("created_at", created_at);
		json.put("repo_url", repo_url);
		
		return json;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getRepo_url() {
		return repo_url;
	}
	public void setRepo_url(String repo_url) {
		this.repo_url = repo_url;
	}
	
	
}
