package io.rebot.forkcrane.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class LanguageRank {
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private List<String> langs = new ArrayList<String>();
	
	public void add(String language, int estimation){
		
		if(language == null ) return;
		
		int point = estimation + 1;
		
		try{
			point += map.get(language); 
		}catch(NullPointerException e){ 
			langs.add(language);
		}
		
		map.put(language, point);
		
//		System.out.println(language + " : " + map.get(language));
	}

	
	public JSONObject getJSON(){
		class Node{
			String language;
			int estimation;
			public Node(String language, int estimation){
				this.language = language;
				this.estimation = estimation;
			}
		}
		
		List<Node> lists = new ArrayList<Node>();
		for(String language : langs){
			lists.add(new Node(language, map.get(language)));
		}
		
		Collections.sort(lists, new Comparator<Node>() {
			public int compare(Node o1, Node o2) {
				return o2.estimation - o1.estimation;
			}
		});
		
		JSONObject json = new JSONObject();
		for(int i=0; i<lists.size(); i++){
//			System.out.println("" + (i+1) + " / " + lists.get(i).language + " / " + lists.get(i).estimation);
			try {
				json.put("" + (i+1), lists.get(i).language);
			} catch (JSONException e) { e.printStackTrace(); }
		}
		
		return json;
	}
}

