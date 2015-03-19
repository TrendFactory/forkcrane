package io.rebot.forkcrane.rating;

public class TrendLeaderRatingNode{

	private String githubUniqId;
	private String user;
	private RatingNode ratingNode;
	
		public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		
		return user + " / Esti: " + ratingNode.getTotalEstimation() + 
						"(Star/Fork/Watch:" + ratingNode.getTotalStargazersCount() + 
											"/" + ratingNode.getTotalForksCount() + 
											"/" + ratingNode.getTotalWatcherCount() + ")";
	}
	
	public void setRatingNode(RatingNode node){
		this.ratingNode = node;
	}
	public RatingNode getRatingNode(){
		return this.ratingNode;
	}

	public String getUser() {
		return user;
	}

	public String getGithubUniqId() {
		return githubUniqId;
	}

	public void setGithubUniqId(String githubUniqId) {
		this.githubUniqId = githubUniqId;
	}
	
	
}
