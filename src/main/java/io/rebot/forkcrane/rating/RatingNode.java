package io.rebot.forkcrane.rating;

import java.util.ArrayList;
import java.util.List;

public class RatingNode {
	
	private String language;
	private int totalStargazersCount;
	private int totalWatcherCount;
	private int totalForksCount;
	private int totalEstimation;		// 해당언어의 estimation 합산
	private List<String> repos_url = new ArrayList<String>(); 
	// 해당 언어에 대한 watch, fork, star (1명의 언어에 대한 repo 총합)
	
	public RatingNode(String language){
		this.language = language;
		this.totalStargazersCount = 0;
		this.totalWatcherCount = 0;
		this.totalForksCount = 0;
		this.totalEstimation = 0;
	}

	public void addStar(int star){
		this.totalStargazersCount += star;
	}
	
	public void addWatch(int watch){
		this.totalWatcherCount += watch;
	}
	
	public void addFork(int fork){
		this.totalForksCount += fork;
	}
	
	public void addEstimation(int estimation){
		this.totalEstimation += estimation;
	}
	
	public void addURL(String url){
		this.repos_url.add(url);
	}

	public String getLanguage() {
		return language;
	}

	public int getTotalStargazersCount() {
		return totalStargazersCount;
	}

	public int getTotalWatcherCount() {
		return totalWatcherCount;
	}

	public int getTotalForksCount() {
		return totalForksCount;
	}

	public int getTotalEstimation() {
		return totalEstimation;
	}

	public List<String> getRepos_url() {
		return repos_url;
	}
	
	
}