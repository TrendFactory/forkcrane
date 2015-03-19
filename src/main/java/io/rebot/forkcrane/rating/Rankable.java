package io.rebot.forkcrane.rating;

import java.util.List;

import io.rebot.forkcrane.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public interface Rankable {
	
	public void add(List<User> users);
	public void add(User user);
	public JSONObject getJSON_TrendLeader() throws JSONException;
	public JSONObject getJSON_LanguageRank() throws JSONException;
	
}