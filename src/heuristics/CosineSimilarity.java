package heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.LoadData;

public class CosineSimilarity extends Algorithm {

	@Override
	public List<String> getRecommendedPoisForUser(String userId, int trainingSet) {
		
		List<String> mostSimilarUsers = getMostSimilarUsers(userId, trainingSet);
		List<String> recommendedPois = getRecommendedPois(mostSimilarUsers, trainingSet);
		
		return recommendedPois;
	}
	
	private List<String> getMostSimilarUsers(String userId, int trainingSet){
		List<String> mostSimilarUsers = new ArrayList<>();
		List<UserSimilarity> similarities = new ArrayList<>();
		
		for(String id : LoadData.getInstance().getAllUsersAndPois().keySet()){
			if(!id.equals(userId)){
				similarities.add(calculateSimilarity(userId, id, trainingSet));
			}
		}
		
		Collections.sort(similarities, Collections.reverseOrder());
		for(UserSimilarity similarity : similarities){
			mostSimilarUsers.add(similarity.getUserId());
		}
		
		int min = Math.min(TOP_SIMILAR_USERS, mostSimilarUsers.size());
		return mostSimilarUsers.subList(0, min);
	}
	
	private List<String> getRecommendedPois(List<String> mostSimilarUsers, int trainingSet){
		List<String> recommendedPois = new ArrayList<>();
		for(String userId : mostSimilarUsers){
			for(String poi : LoadData.getInstance().getVisitedPoisOfUser(userId, trainingSet)){
				if(!recommendedPois.contains(poi)){
					recommendedPois.add(poi);
				}
			}
		}
		int min = Math.min(NUMBER_POIS_PER_USER, recommendedPois.size());
		return recommendedPois.subList(0, min);
	}
	
	private UserSimilarity calculateSimilarity(String userId, String userIdOther, int trainingSet){
		UserSimilarity similarity = new UserSimilarity(userIdOther);
		
		List<String> pois = LoadData.getInstance().getVisitedPoisOfUser(userId, trainingSet);
		List<String> poisOther = LoadData.getInstance().getVisitedPoisOfUser(userIdOther, trainingSet);
		
		double numerator = 0.0;
		double denominator = 0.0;
		for(String poi : pois){
			if(poisOther.contains(poi)){
				numerator++;
			}
		}
		denominator = (Math.sqrt(pois.size()) * Math.sqrt(poisOther.size()));
		if(denominator == 0.0) return similarity;
		
		
		similarity.setSimilarity(numerator/denominator);
		
		
		return similarity;
	}
	
	private class UserSimilarity implements Comparable<UserSimilarity> {
		private String userId;
		private Double similarity;
		
		public UserSimilarity(String userId) {
			this.userId = userId;
			this.similarity = 0.0;
		}
		
		@Override
		public int compareTo(UserSimilarity o) {
			return getSimilarity().compareTo(o.getSimilarity());
		}

		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Double getSimilarity() {
			return similarity;
		}
		public void setSimilarity(Double similarity) {
			this.similarity = similarity;
		}
	}

}
