package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LoadData {
	
	private static final String TRAIN_FILE = "files/filtered_train_file.txt";
	private static final String TEST_FILE = "files/filtered_test_file.txt";
	
	private static LoadData loader;
	private static Map<String, List<String>> trainDataMap;
	private static Map<String, List<String>> testDataMap;
	
	
	private LoadData(){}
	
	public static LoadData getInstance(){
		if(loader == null){
			trainDataMap = getData(TRAIN_FILE);
			testDataMap = getData(TEST_FILE);
			loader = new LoadData();
		}
		return loader;
	}
	
	public List<String> getUsers(int trainingSet){
		return new ArrayList<>(trainDataMap.keySet());
	}
	
	public List<String> getVisitedPoisOfUser(String userId, int trainingSet){
		 return trainDataMap.get(userId);
	}
	
	public List<String> getHiddenPoisOfUser(String userId, int trainingSet){
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
