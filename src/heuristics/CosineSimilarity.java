package heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import util.LoadData;

public class CosineSimilarity extends Algorithm {

	@Override
	public List<String> getRecommendedPoisForUser(String userId, int numRecPois, int trainingSet) {
		
		List<String> mostSimilarUsers = getMostSimilarUsers(userId, trainingSet);
		List<String> recommendedPois = getRecommendedPois(userId, mostSimilarUsers, trainingSet);
		
		List<String> list = new ArrayList<>();
		//pegar os pois em ordem de adicionados (do usuario mais similar para os menos)
		int min = Math.min(numRecPois, recommendedPois.size());
		for(int i = 0; i < min; i++){
			list.add(recommendedPois.get(i));
		}
		
		return list;
	}
	
	/**
	 * Para cada usuario nos dados
	 *   - Calcula a similaridade do usuario alvo com eles
	 *   - Seleciona os TOP_SIMILAR_USERS usuarios mais similares
	 *   - Adiciona-os em uma lista e retorna
	 * @param userId
	 * @param trainingSet
	 * @return
	 */
	private List<String> getMostSimilarUsers(String userId, int trainingSet){
		List<String> mostSimilarUsers = new ArrayList<>();
		List<UserSimilarity> similarities = new ArrayList<>();
		
		for(String id : LoadData.getInstance(trainingSet).getAllUsersAndPois().keySet()){
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
	/**
	 * Para cada um dos TOP_SIMILAR_USERS usuarios mais similares
	 *   - Rancomicamente seleciona NUMBER_POIS_PER_USER
	 *   - Adiciona estes pois a uma nova lista
	 *   - Retorna a lista
	 * @param mostSimilarUsers
	 * @param trainingSet
	 * @return
	 */
	private List<String> getRecommendedPois(String userId, List<String> mostSimilarUsers, int trainingSet){
		List<String> recommendedPois = new ArrayList<>();
		List<String> myPois = LoadData.getInstance(trainingSet).getVisitedPoisOfUser(userId);
		
		for(String uId : mostSimilarUsers){
			//pega somente um numero determinado de pois daquele usuario
			List<String> chosenPois = getChosenPois(LoadData.getInstance(trainingSet).getVisitedPoisOfUser(uId));
			for(String poi : chosenPois){
				if(!recommendedPois.contains(poi) && !myPois.contains(poi)){
					recommendedPois.add(poi);
				}
			}
		}
		
		return recommendedPois;
	}
	
	/**
	 * Random nos pontos recomendados (Ja que nao temos melhor maneira de selecionar e selecionar
	 * em order seria *injusto*)
	 * */
	private List<String> getChosenPois(List<String> recommendedPoisPerUser){
		List<String> list = new ArrayList<>();
		int min = Math.min(NUMBER_POIS_PER_USER, recommendedPoisPerUser.size());
		final int[] ints = new Random().ints(0, recommendedPoisPerUser.size()).distinct().limit(min).toArray();
		
		for(int i = 0; i < ints.length; i++){
			list.add(recommendedPoisPerUser.get(i));
		}
		return list;
	}
	
	/**
	 * Calcula similaridade baseada em Coseno
	 * @param userId
	 * @param userIdOther
	 * @param trainingSet
	 * @return
	 */
	private UserSimilarity calculateSimilarity(String userId, String userIdOther, int trainingSet){
		UserSimilarity similarity = new UserSimilarity(userIdOther);
		
		List<String> pois = LoadData.getInstance(trainingSet).getVisitedPoisOfUser(userId);
		List<String> poisOther = LoadData.getInstance(trainingSet).getVisitedPoisOfUser(userIdOther);
		
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
	
	/**
	 * Uma simples estrutura para facilitar a comparacao entre os usuarios - Comparable
	 * @author jordao
	 *
	 */
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
