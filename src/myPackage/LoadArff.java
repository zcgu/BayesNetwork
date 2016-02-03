package myPackage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.StyledEditorKit.ForegroundAction;

public class LoadArff {
	
	BufferedReader trainBR;
	BufferedReader testBR;
	
	DataSet trainDataSet;
	DataSet testDataSet;
	
	public LoadArff(String trainPath, String testPath) throws IOException {
		// Set Readers.
		File trainFile = new File(trainPath);
		File testFile = new File(testPath);
		trainBR = new BufferedReader(new FileReader(trainFile));
		testBR = new BufferedReader(new FileReader(testFile));

		// Init data.
		trainDataSet = new DataSet();
		testDataSet = new DataSet();
		
		// Load dataset.
		loadData(trainDataSet, trainBR);
		loadData(testDataSet, testBR);
	}
	
	public DataSet getTrainData() {
		return trainDataSet;
	}
	
	public DataSet getTestData() {
		return testDataSet;
	}
	
	private void loadData(DataSet data, BufferedReader br) throws IOException {
		String line = br.readLine();
		
		while(line != null) {
			// Remove beginning space.
			line = removeBeginningSpace(line);
			if(line.length() <= 0) {
				line = br.readLine();
				continue;
			}
			
			// Comments.
			if(line.startsWith("%")){
				line = br.readLine();
				continue;
			}
			
			// relation.
			if(line.split(" ")[0].equalsIgnoreCase("@RELATION")) {
				handleRelationLine(data, line);
				line = br.readLine();
				continue;
			}
			
			// attribute.
			if(line.split(" ")[0].equalsIgnoreCase("@ATTRIBUTE")) {
				handleAttributeLine(data, line);
				line = br.readLine();
				continue;
			}
			
			// data.
			if(line.split(" ")[0].equalsIgnoreCase("@data")) {
				line = br.readLine();
				continue;
			}
			
			// read data.
			handleDataLine(data, line);
			
			// Read next line.
			line = br.readLine();
		}
	}
	
	private String removeBeginningSpace(String string) {
		while (string.startsWith(" ")) {
			string = string.substring(1, string.length());
		}
		return string;
	}
	
	private void handleRelationLine(DataSet dataSet, String line) {
		String[] splitLine = line.split(" ");
		
		for(int i=0; i<splitLine.length; i++) {
			String s = splitLine[i];
			
			if(s.length() <= 0) continue;
			if(s.equalsIgnoreCase("@Relation") || s.equals(" ")) continue;
			
			dataSet.relation = s;
			return;
		}
	}
	
	private void handleAttributeLine(DataSet dataSet, String line) {
		String[] splitLine = line.split(" ");
		
		for(int i=0; i<splitLine.length; i++) {
			String s = splitLine[i];
			
			if(s.length() <= 0) continue;
			if(s.equalsIgnoreCase("@Attribute") || s.equals(" ")) continue;

			// Get the name part.
			Attribute attribute = new Attribute();
			attribute.name = s;
			
			// Get the value part.
			handleAttirbutePossibleValues(attribute, line);
			
			// Remove "'" in name.
			if(attribute.name.startsWith("'") && attribute.name.endsWith("'")) {
				attribute.name = attribute.name.substring(1, attribute.name.length()-1);
			}
			
			// Add attribute to dataset.
			if(attribute.name.equalsIgnoreCase("class")) dataSet.classLabel = attribute;
			else dataSet.attributeList.add(attribute);
			
			return;
		}
	}
	
	private void handleAttirbutePossibleValues(Attribute attribute, String line) {
		// get values part.
		int pos = line.indexOf(attribute.name, "@attribute".length());
		String valuesString = line.substring(pos + attribute.name.length(), line.length());
		
		// Remove spaces.
		valuesString = valuesString.replaceAll(" ", "");
		
		// Remove "{" and "}".
		if(valuesString.startsWith("{") && valuesString.endsWith("}")) {
			valuesString = valuesString.substring(1, valuesString.length()-1);
		}
		
		// Add values to attribute.
		String[] splitValueString = valuesString.split(",");
		for(String s : splitValueString) attribute.possibleValues.add(s);
	}
	
	private void handleDataLine(DataSet dataSet, String line) {
		// Remove spaces.
		line = line.replaceAll(" ", "");
		
		// Split and add to data set.
		String[] splitLine = line.split(",");
		Map<Attribute, String> map = new HashMap<Attribute, String>();

		for(int i=0; i<splitLine.length; i++) {
			
			// Make sure values in attribute.
			if(i != splitLine.length - 1) {
				if(!dataSet.attributeList.get(i).possibleValues.contains(splitLine[i]))
					System.out.println("Some thing wrong: " + splitLine[i] 
							+ " is not in attribute " + dataSet.attributeList.get(i).name
							+ "'s possible values.");
				
				map.put(dataSet.attributeList.get(i), splitLine[i]);
			} else {
				if(!dataSet.classLabel.possibleValues.contains(splitLine[i]))
					System.out.println("Some thing wrong: " + splitLine[i] 
							+ " is not in attribute " + dataSet.attributeList.get(i).name
							+ "'s possible values.");
				
				map.put(dataSet.classLabel, splitLine[i]);
			}
		}
		
		dataSet.data.add(map);
	}
}
