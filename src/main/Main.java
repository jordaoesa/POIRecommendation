package main;

import heuristics.Algorithm;
import heuristics.CosineSimilarity;
import util.GenerateFilteredFiles;

public class Main {
	
	public static void main(String[] args) {
		//used one time just to generate the data necessary to work with.
		//generateFilteredData();
		
		Algorithm algorithm = new CosineSimilarity();
		algorithm.run();
		
		
	}
	
	private static void generateFilteredData(){
		GenerateFilteredFiles gFFiles = new GenerateFilteredFiles();
		gFFiles.generateArrayOfUsers();
	}

}
