package io.rebot.forkcrane.domain;

import io.rebot.forkcrane.crawler.HttpCrane;
import io.rebot.forkcrane.rating.LanguageRatingNode;
import io.rebot.forkcrane.rating.RatingNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DBObject;
import com.mongodb.util.Hash;
import com.mongodb.util.JSON;

public class Github{
	public String githubUniqId;
	
	public String accessToken;
	public String refreshToken;
	public String updated_at;
	public String created_at;
	public int following;
	public int followers;
	public int public_gists;			// gist 갯수
	
	public String email;
	public String location;
	public String blog; 
	public String company;

	public String name;
	public String avatar_url;			// 내사진

	public String repos_url;			// 내 repos 리스트
	public String organizations_url;	// 등록된orgs
	public String starred_url;			// 별준것들
	public String following_url;		// 내가 등록한사람
	public String followers_url;		// 나를 등록한사람
	
	private int totalStargazersCount = 0;
	private int totalWatcherCount = 0;
	private int totalForksCount = 0;
	
	private List<Repository> repos = new ArrayList<Repository>();
	private List<String> languages = new ArrayList<String>();
	private List<PerformedEvent> events = new ArrayList<PerformedEvent>();

	public Github(String name, String githubUniqId2){
		this.name = name;
		this.githubUniqId = githubUniqId;
	}
	public void setGithub(DBObject obj) {
		// TODO Auto-generated method stub
		this.accessToken = (String)obj.get("accessToken");
		this.updated_at = (String)obj.get("updated_at");
		this.created_at = (String)obj.get("created_at");
		this.following = (Integer)obj.get("following");
		this.followers = (Integer)obj.get("followers");
		this.public_gists = (Integer)obj.get("public_gists");
		
		this.email = (String)obj.get("email");
		this.location = (String)obj.get("location");
		this.blog = (String)obj.get("blog");
		this.company = (String)obj.get("company");
		this.avatar_url = (String)obj.get("avatar_url");
		
		this.repos_url = (String)obj.get("repos_url");
		this.organizations_url = (String)obj.get("organizations_url");
		this.starred_url = (String)obj.get("starred_url");
		this.following_url = (String)obj.get("following_url");
		this.followers_url = (String)obj.get("followers_url");
	}
	
	public void setRopos(String repoJson) throws JSONException{
		JSONArray jArr = new JSONArray(repoJson);
		for(int i=0; i<jArr.length(); i++){
//			if(Constant.DEBUGING) System.out.println("for...i=" + i + "; i<" + jArr.length());
			JSONObject json = jArr.getJSONObject(i);
			
			Repository repo = new Repository();

//			DBObject 으로 업캐스팅 해준 이유
//			JSONObject는 key가 결과값이 null일 경우 NullPointException을 던지기 때문
			repo.setRepo((DBObject) JSON.parse(json.toString()), githubUniqId);
			
			this.totalStargazersCount += repo.getStargazers_count();
			this.totalForksCount += repo.getForks_count();
			this.totalWatcherCount += repo.getWatchers_count();
			
			if( (repo.language != null) && !languages.contains(repo.language)){
				languages.add(repo.language);
			}
			this.repos.add(repo);
		}
	}
	
	public void setEvents(String eventJson) throws JSONException {
		JSONArray jArr = new JSONArray(eventJson);
		for(int i=0; i<jArr.length(); i++){
//			if(Constant.DEBUGING) System.out.println("for...i=" + i + "; i<" + jArr.length());
			JSONObject json = jArr.getJSONObject(i);
			
			PerformedEvent event = new PerformedEvent(this.githubUniqId);
			event.setCreated_at(json.getString("created_at"));
			event.setType(json.getString("type"));
			event.setRepo_url(json.getJSONObject("repo").getString("url"));
			
			this.events.add(event);
		}
	}
	
	public JSONObject getJSON() throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("githubUniqId", githubUniqId);
		json.put("accessToken", this.accessToken);
		json.put("refreshToken", this.refreshToken);
		json.put("updated_at", this.updated_at);
		json.put("created_at", this.created_at);
		
		json.put("following", this.following);
		json.put("followers", this.followers);
		json.put("public_gists", this.public_gists);
		
		json.put("email", this.email);
		json.put("location", this.location);
		json.put("blog", this.blog);
		json.put("company", this.company);
		
		json.put("name", this.name);
		json.put("avatar_url", this.avatar_url);

		json.put("repos_url", this.repos_url);
		json.put("organizations_url", this.organizations_url);
		json.put("starred_url", this.starred_url);
		json.put("following_url", this.following_url);
		json.put("followers_url", this.followers_url);
		
		json.put("totalStargazersCount", this.totalStargazersCount);
		json.put("totalWatcherCount", this.totalWatcherCount);
		json.put("totalForksCount", this.totalForksCount);
		
		return json;
	}
	
	
	

	public List<RatingNode> getRatingNodes(){
		
		List<RatingNode> nodes = new ArrayList<RatingNode>();
		
		// docs을 language수 만큼 생성
		for(int i=0; i<this.languages.size(); i++){
			nodes.add(new RatingNode(languages.get(i)));
		}
		
		for(Repository repo : repos){
//			System.out.println("  repo name : " + repo.name + "(" + repo.getEstimation() + ")");
			
			if(repo.language == null)
				continue;
			int index = languages.indexOf(repo.language);
			
			if(index >= 0){
//				System.out.println("Est(S/W/F) : " + repo.getEstimation() + 
//						"(" + 
//						repo.getStargazers_count() + "/" +
//						repo.getWatchers_count() + "/" +
//						repo.getForks_count() +
//						")");
				nodes.get(index).addStar(repo.getStargazers_count());
				nodes.get(index).addWatch(repo.getWatchers_count());
				nodes.get(index).addFork(repo.getForks_count());
				nodes.get(index).addEstimation(repo.getEstimation());
				nodes.get(index).addURL(repo.html_url);
			}
		}
		
		return nodes;
	}
	
	public List<Repository> getRepos() {
		return repos;
	}

	public JSONObject getEventsJSON() throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		obj.put("user", name);
		obj.put("githubUniqId", githubUniqId);
		JSONArray jsonArr = new JSONArray();
		for(PerformedEvent event : events){
			jsonArr.put(event.getJSON());
		}
		obj.put("event", jsonArr);
		
		return obj;
	}

	
}