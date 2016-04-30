package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GenerateFilteredFiles {
	
	private final String CHECKINS_FILE = "files/Gowalla_totalCheckins.txt";
	private final String FILTERED_FILE = "files/filtered_file.txt";
	private final String TRAIN_FILE = "files/filtered_train_file.txt";
	private final String TEST_FILE = "files/filtered_test_file.txt";
	private final int MINIMUM_CHECKINS = 10;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private Map<String, ArrayList<String>> data;
	
	public void generateArrayOfUsers() {
		data = new TreeMap();
		try {
			String line;
			String tokens[];
			StringBuffer bufferTrain = new StringBuffer();
			StringBuffer bufferTest = new StringBuffer();
			reader = new BufferedReader(new FileReader(new File(CHECKINS_FILE)));
						
			while((line = reader.readLine()) != null){
				tokens = line.split("\t");
				if(!data.containsKey(tokens[0])){
					data.put(tokens[0], new ArrayList<>());
				}
				if(!data.get(tokens[0]).contains(tokens[4])){
					data.get(tokens[0]).add(tokens[4]);
				}
			}
			
			for(String key: data.keySet()){
				if(data.get(key).size() >= MINIMUM_CHECKINS){
					int size = data.get(key).size();
					bufferTrain.append(key);
					bufferTest.append(key);
					List<Integer> chosen = getChosenPois(size);
					for(int j = 0; j < chosen.size(); j++){
						bufferTest.append("\t" + data.get(key).get(chosen.get(j)));
					}
					for(int j = 0; j < data.get(key).size(); j++){
						if(!chosen.contains(j)){
							bufferTrain.append("\t" + data.get(key).get(j));
						}
					}
					bufferTest.append("\n");
					bufferTrain.append("\n");
				}
			}
			
			writer = new BufferedWriter(new FileWriter(new File(TRAIN_FILE)));
			writer.write(bufferTrain.toString());
			writer.close();
			
			writer = new BufferedWriter(new FileWriter(new File(TEST_FILE)));
			writer.write(bufferTest.toString());
			writer.close();
			
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
		
			
	}
	
	private List<Integer> getChosenPois(int size){
		List<Integer> list = new ArrayList<>();
		final int[] ints = new Random().ints(0, size).distinct().limit(size/MINIMUM_CHECKINS).toArray();
		for(int i=0; i < ints.length; i++){
			list.add(ints[i]);
		}
		return list;
		
	}

}
