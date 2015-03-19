package io.rebot.forkcrane.rating;

public class LanguageRatingNode extends RatingNode{

	public LanguageRatingNode(String language) {
		super(language);
	}

	private int DAU;			// 해당언어를 사용하는 모든 user수
	private int reposCount;		// 해당언어를 사용하는 모든 user repo수
	
//	public LanguageRatingNode(String language, int estimation, int DAU, int reposCount){
//		super(language);
//		this.language = language;
//		this.estimation = estimation;
//		this.DAU = DAU;
//		this.reposCount = reposCount;
//	}

	public void addDAU() {
		DAU++;
	}
	
	public int getDAU(){
		return DAU;
	}

	public int getReposCount() {
		return reposCount;
	}

	public void addReposCount(int reposCount) {
		this.reposCount += reposCount;
	}
	
	@Override
	public String toString() {
		
		return "Esti: " + this.getTotalEstimation() + 
				"  Star/Fork/Watch: " + this.getTotalStargazersCount() + "/" + this.getTotalForksCount() + "/" + this.getTotalWatcherCount();
	}
	
}