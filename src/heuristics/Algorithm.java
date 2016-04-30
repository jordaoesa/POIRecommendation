package heuristics;

import java.util.ArrayList;
import java.util.List;

import util.LoadData;

public abstract class Algorithm {
	
	private final int MAX_RECOMMENDED_POIS = 10;
	private final int MAX_TRAINING_SETS = 1;
	protected final int TOP_SIMILAR_USERS = 10;
	private double precision;
	
	
	public void run(){
		for(int numRecPois = 1; numRecPois <= MAX_RECOMMENDED_POIS; numRecPois++){
			List<Double> precisions = new ArrayList<>();
			for(int trainingSet = 0; trainingSet < MAX_TRAINING_SETS; trainingSet++){
				executeAlgorithm(numRecPois, trainingSet);
				precisions.add(precision);
			}
			System.out.println("Resultados finais com " + numRecPois + " POIs recomendados:");
			System.out.println("AveragePrecision -> " + avg(precisions));
			System.out.println("=========================================");
		}
	}
	
	private void executeAlgorithm(int numRecPois, int trainingSet){
		List<String> users = LoadData.getInstance().getUsers(trainingSet);
		int i = 0;
		for(String userId : users){
			i++; if(i==10) break;
			
			precision = 0.0;
			List<String> hiddenPois = LoadData.getInstance().getHiddenPoisOfUser(userId, trainingSet);
			List<String> recommendedPois = getRecommendedPoisForUser(userId, trainingSet);
			
			int min = Math.min(numRecPois, recommendedPois.size());
			List<String> subList = recommendedPois.subList(0, min);

			for(String hiddenPoi : hiddenPois){
				if(subList.contains(hiddenPoi)){
					precision += 1.0/min;
				}
			}
			
			System.out.println("Com " + numRecPois + " POIs recomendados para user " + userId + ", o resultado foi:");
			System.out.println("precision -> " + precision);
			System.out.println("=========================================");
		}
	}
	
	private double avg(List<Double> precisions){
		Double avg = 0.0;
		for(Double d : precisions){
			avg += d;
		}
		return avg/precisions.size();
	}
	
	public abstract List<String> getRecommendedPoisForUser(String userId, int trainingSet);

}
