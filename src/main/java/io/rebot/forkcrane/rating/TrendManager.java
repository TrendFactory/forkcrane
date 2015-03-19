package io.rebot.forkcrane.rating;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import io.rebot.forkcrane.domain.Constant;
import io.rebot.forkcrane.domain.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.util.JSON;

public class TrendManager implements Rankable{
	
	private List<String> languages = new ArrayList<String>();
	private Map<String, PriorityQueue<TrendLeaderRatingNode>> tlMap = new HashMap();
	private Map<String, LanguageRatingNode> lrMap = new HashMap();
	
	public void add(User user) {

		List<RatingNode> nodes = user.github.getRatingNodes();
//		System.out.println(user.name + "  getRatingNodes() " + nodes.size());
		for(RatingNode node : nodes){
			
			// 0. add language
			addLanguage(node.getLanguage());
			
			// 1. TendLeader
			TrendLeaderRatingNode TLnode = new TrendLeaderRatingNode();
			TLnode.setRatingNode(node);
			TLnode.setUser(user.name);
			TLnode.setGithubUniqId(user.githubUniqId);
			this.tlMap.get(node.getLanguage()).add(TLnode);
			
			
			// 2. Language Rank			
			LanguageRatingNode LRnode = lrMap.get(node.getLanguage());
			LRnode.addDAU();
			LRnode.addReposCount(node.getRepos_url().size());
			LRnode.addEstimation(node.getTotalEstimation());
			LRnode.addStar(node.getTotalStargazersCount());
			LRnode.addFork(node.getTotalForksCount());
			LRnode.addWatch(node.getTotalWatcherCount());
		}
	}
	
	public void add(List<User> users) {
		
		for(User user : users){
			add(user);
		}
		
	}
	
	
	private void addLanguage(List<String> languages){
		
		for(String language : languages){
			addLanguage(language);
		}
	}
	
	
	private void addLanguage(String language){
		
		if( language!=null && !languages.contains(language)){
			if(Constant.DEBUGING) System.out.println("add lang : " + language);
			languages.add(language);
			tlMap.put(language, new PriorityQueue<TrendLeaderRatingNode>(new Comparator<TrendLeaderRatingNode>() {
				public int compare(TrendLeaderRatingNode o1, TrendLeaderRatingNode o2) {
					return o2.getRatingNode().getTotalEstimation() - o1.getRatingNode().getTotalEstimation();
				}
			}));
			lrMap.put(language, new LanguageRatingNode(language));
		}
	}

	
	public void show() {
		System.out.println("Language Rank...");
		
		for(String lang : languages){
			System.out.println(" ("+lang +")  " + lrMap.get(lang).toString());
		}
		
		System.out.println("TrendLeader...");
		for(String lang : languages){
			System.out.println(" ("+lang +")  " + tlMap.get(lang).toString());
		}
	}

	public JSONObject getJSON_TrendLeader() throws JSONException {
		
		JSONObject json = new JSONObject();
		
		for(String language : languages)
		{
			JSONArray usersJsonArr = new JSONArray();
			Object[] users = tlMap.get(language).toArray();
			for(Object o : users){
				TrendLeaderRatingNode user = (TrendLeaderRatingNode)o;
				
				JSONObject userRepo = new JSONObject();
				userRepo.put("githubUniqId", user.getGithubUniqId());
				userRepo.put("name", user.getUser());
				userRepo.put("estimation", user.getRatingNode().getTotalEstimation());
				userRepo.put("forks_count", user.getRatingNode().getTotalForksCount());
				userRepo.put("stargazers_count", user.getRatingNode().getTotalStargazersCount());
				userRepo.put("watchers_count", user.getRatingNode().getTotalWatcherCount());
				
				JSONArray langJson = new JSONArray();
				for(String url : user.getRatingNode().getRepos_url()){
					langJson.put(url);
				}
				userRepo.put("repos_url", langJson);
				
				usersJsonArr.put(userRepo);
			}			
			json.put(language, usersJsonArr);
		}
		
		return json;
	}

	public JSONObject getJSON_LanguageRank() throws JSONException {
		
		JSONObject json = new JSONObject();
		
		for(String language : languages)
		{
			 LanguageRatingNode node = lrMap.get(language);
			 
			 JSONObject obj = new JSONObject();
			 obj.put("DAU", node.getDAU());
			 obj.put("repos_count", node.getReposCount());
			 obj.put("estimation", node.getTotalEstimation());
			 obj.put("forks_count", node.getTotalForksCount());
			 obj.put("stargazers_count", node.getTotalStargazersCount());
			 obj.put("watchers_count", node.getTotalWatcherCount());
			 
			 json.put(language, obj);
		}
		
		return json;
	}
}
