package heuristics;

import java.util.List;

import util.LoadFiles;

public abstract class Algorithm {
	
	private final int MAX_RECOMMENDED_POIS = 10;
	protected final int TOP_SIMILAR_USERS = 10;
	
	public void executeAlgorithm(){
		List<String> users = LoadFiles.getInstance().getUsers();
		
		for(String userId : users){
			List<String> hiddenPois = LoadFiles.getInstance().getHiddenPoisOfUser(userId);
			List<String> recommendedPois = getRecommendedPoisForUser(userId);
			
			for(int numRecPois = 1; numRecPois <= MAX_RECOMMENDED_POIS; numRecPois++){
				double precision = 0.0;
				int min = Math.min(numRecPois, recommendedPois.size());
				List<String> subList = recommendedPois.subList(0, min);

				for(String hiddenPoi : hiddenPois){
					if(subList.contains(hiddenPoi)){
						precision += 1.0/min;
					}
				}
				
				System.out.println("Com " + numRecPois + " POIs recomendados, o resultado foi:");
				System.out.println("precision -> " + precision);
				System.out.println("=========================================");
			}
		}
	}
	
	public abstract List<String> getRecommendedPoisForUser(String userId);

}
