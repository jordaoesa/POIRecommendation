package heuristics;

import java.util.ArrayList;
import java.util.List;

import util.LoadData;

public abstract class Algorithm {
	
	//Numero maximo de pois a serem recomendados para o usuario. Este varia em cada iteracao
	private final int MAX_RECOMMENDED_POIS = 10;
	//Numero de conjuntos de treinamento
	private final int MAX_TRAINING_SETS = 10;
	//Numero de usuarios similares a serem levados em conta
	protected final int TOP_SIMILAR_USERS = 10;
	//Numero de pois a serem selecionados de cada usuario
	protected final int NUMBER_POIS_PER_USER = 10;
	//Precisao dos acertos
	private double precision;
	
	/**
	 * Unico metodo publico de todos os Algoritmos/Heuristicas utilizadas no projeto
	 */
	public void run(){
		for(int numRecPois = 1; numRecPois <= MAX_RECOMMENDED_POIS; numRecPois++){
			List<Double> precisions = new ArrayList<>();
			for(int trainingSet = 0; trainingSet < MAX_TRAINING_SETS; trainingSet++){
				executeAlgorithm(numRecPois, trainingSet);
				precisions.add(precision);
			}
			System.out.println("Media dos resultados com " + numRecPois + " POIs recomendados:");
			System.out.println("AveragePrecision -> " + avg(precisions));
			System.out.println("=============================================");
		}
	}
	
	private void executeAlgorithm(int numRecPois, int trainingSet){
		List<String> users = LoadData.getInstance(trainingSet).getUsers();
		
		//int i = 0;
		double denominator = 0.0;
		precision = 0.0;
		for(String userId : users){
			//i++; if(i==5) break;
			
			List<String> hiddenPois = LoadData.getInstance(trainingSet).getHiddenPoisOfUser(userId);
			List<String> recommendedPois = getRecommendedPoisForUser(userId, numRecPois, trainingSet);

			int min = Math.min(numRecPois, recommendedPois.size());

			for(String hiddenPoi : hiddenPois){
				denominator++;
				if(recommendedPois.contains(hiddenPoi)){
					precision += 1.0/min;
				}
			}
		}
		precision = precision / denominator;
		System.out.println("Com " + numRecPois + " POIs para o training set " + trainingSet + ", o resultado foi:");
		System.out.println("precision -> " + precision);
		System.out.println("---------------------------------------------");
	}
	
	private double avg(List<Double> precisions){
		Double avg = 0.0;
		for(Double d : precisions){
			avg += d;
		}
		return avg/precisions.size();
	}
	
	/**
	 * Este metodo deve ser implementado em todos os algoritimos que extenderem esta classe.
	 * O algoritimo somente funcionara com a implementacao do mesmo.
	 * @param userId Id do usuario para o qual queremos recomendar pois
	 * @param trainingSet Conjunto de treinamento corrente
	 * @return
	 */
	public abstract List<String> getRecommendedPoisForUser(String userId, int numRecPois, int trainingSet);

}
