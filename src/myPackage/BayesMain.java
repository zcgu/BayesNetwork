package myPackage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BayesMain {

	static String type;
	
	static DataSet trainDataSet;
	static DataSet testDataSet;
	
	static String outPath = "out.txt";
	
	public static void main(String[] args) throws IOException {
		// Get parameters.
		type = args[2];

		// Load arff files.
		LoadArff loadArff = new LoadArff(args[0], args[1]);
		
		// Get data sets.
		trainDataSet = loadArff.getTrainData();
		testDataSet = loadArff.getTestData();
		
		// Build Naive Bayes.
		if(type.equals("n")) {
			NaiveBayes naiveBayes = new NaiveBayes(trainDataSet, testDataSet, outPath);
			naiveBayes.setNaiveBayesStructure();
			naiveBayes.test();
		} else if (type.equals("t")) {
			TAN tan = new TAN(trainDataSet, testDataSet, outPath);
			tan.setTANStructure();
			tan.test();
		}
		
		// Finish.
		System.out.println("done");
	}

}
