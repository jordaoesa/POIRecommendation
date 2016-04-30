package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LoadData {
	
	private static final String TRAIN_FILE = "files/filtered_train_file.txt";
	private static final String TEST_FILE = "files/filtered_test_file.txt";
	
	private static final String DATASET_TRAIN_FILE = "files/dataset_train_file_";
	private static final String DATASET_TEST_FILE = "files/dataset_test_file_";
	
	private static int lastLoadedDataSet = 0;
	private static LoadData loader;
	private static Map<String, List<String>> trainDataMap;
	private static Map<String, List<String>> testDataMap;
	
	private LoadData(){}
	
	public static LoadData getInstance(int trainingSet){
		//switch dataset
//		if(trainingSet == -1){
//			if(loader == null){
//				trainDataMap = getData(TRAIN_FILE);
//				testDataMap = getData(TEST_FILE);
//				loader = new LoadData();
//			}
//			lastLoadedDataSet = -1;
//		}else 
		if(loader == null || trainingSet != lastLoadedDataSet){
			trainDataMap = getData(DATASET_TRAIN_FILE + trainingSet + ".txt");
			testDataMap = getData(DATASET_TEST_FILE + trainingSet + ".txt");
			
			loader = new LoadData();
			lastLoadedDataSet = trainingSet;
		}
		return loader;
	}
	
	public Map<String, List<String>> getAllUsersAndPois(){
		return trainDataMap;
	}
	
	public List<String> getUsers(){
		return new ArrayList<>(trainDataMap.keySet());
	}
	
	public List<String> getVisitedPoisOfUser(String userId){
		 return trainDataMap.get(userId);
	}
	
	public List<String> getHiddenPoisOfUser(String userId){
		return testDataMap.get(userId);
	}
	
	private static Map<String, List<String>> getData(String filePath){
		Map<String, List<String>> map = new TreeMap<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(filePath)));
			
			String line;
			String tokens[];
			while((line = reader.readLine()) != null){
				tokens = line.split("\t");
				map.put(tokens[0], Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length)));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return map;
	}
	
}
