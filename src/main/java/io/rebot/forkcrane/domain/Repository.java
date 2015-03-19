
package io.rebot.forkcrane.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBObject;

public class Repository {
	private String githubUniqId;
	
	public String created_at;
	public String updated_at;
	public String pushed_at;
	public boolean fork;
	
	public String language;
	
	public String name;
	public String full_name;
	public String html_url;
	public String dedescription;
	
	public int stargazers_count;
	public int watchers_count;
	public int forks_count;
	public int estimation;
	
	public void setRepo(DBObject json, String githubUniqId) throws JSONException {
		this.githubUniqId = githubUniqId;
		
		this.created_at = (String) json.get("created_at");
		this.updated_at = (String) json.get("updated_at");
		this.pushed_at = (String) json.get("pushed_at");
		this.fork = json.get("fork").toString().equals("true");
		
		this.language = (String) json.get("language");
		
		this.name = (String) json.get("name");
		this.full_name = (String) json.get("full_name");
		this.html_url = (String) json.get("html_url");
		this.dedescription = (String) json.get("dedescription");
		
		this.stargazers_count = (Integer) json.get("stargazers_count");
		this.watchers_count = (Integer) json.get("watchers_count");
		this.forks_count = (Integer) json.get("forks_count");
		
		estimation = calceEtimation();
	}
	
	private int calceEtimation(){
		int point = this.stargazers_count + this.watchers_count*3 + this.forks_count*5;
		return (point > 0) ? point : 1;
	}
	
	public JSONObject getJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put("githubUniqId", this.githubUniqId);
		
		json.put("created_at", this.created_at);
		json.put("updated_at", this.updated_at);
		json.put("pushed_at", this.pushed_at);
		json.put("fork", this.fork);
		
		json.put("language", this.language);

		json.put("name", this.name);
		json.put("full_name", this.full_name);
		json.put("html_url", this.html_url);
		json.put("dedescription", this.dedescription);
		
		json.put("stargazers_count", this.stargazers_count);
		json.put("watchers_count", this.watchers_count);
		json.put("forks_count", this.forks_count);
		json.put("estimation", this.estimation);
		
		return json;
	}
	

	public String getURL() {
		return html_url;
	}

	public int getStargazers_count() {
		return stargazers_count;
	}

	public int getWatchers_count() {
		return watchers_count;
	}

	public int getForks_count() {
		return forks_count;
	}

	public int getEstimation() {
		return estimation;
	}
	
	
	
}