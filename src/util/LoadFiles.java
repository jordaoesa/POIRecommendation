package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LoadFiles {
	
	private final String TRAIN_FILE = "files/filtered_train_file.txt";
	private final String TEST_FILE = "files/filtered_test_file.txt";
	
	private BufferedReader reader;
	
	public Map<String, List<String>> getTrainData(){
		return getData(TRAIN_FILE);
	}
	
	public Map<String, List<String>> getTestData(){
		return getData(TEST_FILE);
	}
	
	private Map<String, List<String>> getData(String filePath){
		Map<String, List<String>> map = new TreeMap<>();
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
