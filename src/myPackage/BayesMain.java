package myPackage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

public class BayesMain {

	static String type;
	
	static DataSet trainDataSet;
	static DataSet testDataSet;
	
	public static void main(String[] args) throws IOException {
		// Get parameters.
		type = args[2];

		// Load arff files.
		DataSetLoader dataSetLoader = new DataSetLoader(args[0], args[1]);
		
		// Get data sets.
		trainDataSet = dataSetLoader.getTrainData();
		testDataSet = dataSetLoader.getTestData();

		// Build tree.
		TreeBuilderTester treeBuilderTester = new TreeBuilderTester(trainDataSet, testDataSet);
		if(type.equals("n")) {
			treeBuilderTester.buildNaiveBayes();
		} else if (type.equals("t")) {
			treeBuilderTester.buildTAN();
		}
		treeBuilderTester.outputModel();
		treeBuilderTester.test();

		// Graph part.
		double res1 = 0, res2 = 0, res3 = 0;
		for(int i = 0; i < 4; i++) {
			res1 += treeBuilderTester.subsetTrain(25, type);
			res2 += treeBuilderTester.subsetTrain(50, type);
			res3 += treeBuilderTester.subsetTrain(100, type);
		}
		res1 /= 4;
		res2 /= 4;
		res3 /= 4;

		/**********************************************************************************
		 *
		 * Draw the learning curve.
		 * To get the plot, uncomment following line.
		 *
		 *********************************************************************************/
		//draw(type, res1, res2, res3);

	}

	private static void draw(String type, double d1, double d2, double d3) {
		// Draw.
		String title = null;
		if (type.equals("n")) title = "Naive Bayes";
		else if (type.equals("t")) title = "TAN";

		Plot chart = new Plot("Learning Curve", title, d1, d2, d3);
		chart.pack( );
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);

	}
}
