package main;

import heuristics.Algorithm;
import heuristics.CosineSimilarity;
import util.GenerateFilteredFiles;

public class Main {
	
	public static void main(String[] args) {
		//usado somente uma vez para criar o conjunto de dados maior
//		generateFilteredData();
		
		//usado uma unica vez somente pra criar os datasets
//		generateTrainAndTestDataSets();
		
		Algorithm algorithm = new CosineSimilarity();
		algorithm.run();
		
		
	}
	
	private static void generateTrainAndTestDataSets(){
		GenerateFilteredFiles gFFiles = new GenerateFilteredFiles();
		gFFiles.generateTrainAndTestDataSets();
	}
	
	private static void generateFilteredData(){
		GenerateFilteredFiles gFFiles = new GenerateFilteredFiles();
		gFFiles.generateArrayOfUsers();
	}

}
