package myPackage;

import java.beans.beancontext.BeanContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import myPackage.TreeStructure.Line;

public class NaiveBayes {

	DataSet trainDataSet;
	
	DataSet testDataSet;
	
	TreeStructure treeStructure;
	
	String outPath;
	
	BufferedWriter br;
	
	public NaiveBayes(DataSet trainDataSet, DataSet testDataSet, String outPath) throws IOException {
		this.trainDataSet = trainDataSet;
		this.testDataSet = testDataSet;
		this.outPath = outPath;

		File outFile = new File(outPath);
		br = new BufferedWriter(new FileWriter(outFile));
	}
	
	public void setNaiveBayesStructure() throws IOException {
		treeStructure = new TreeStructure();
		
		// build tree.
		for(int i=0; i<trainDataSet.attributeList.size(); i++) {
			Line line = new Line(trainDataSet.classLabel, trainDataSet.attributeList.get(i));
			treeStructure.lines.add(line);
		}
		
		// Output.
		for(int i=0; i<treeStructure.lines.size(); i++) {
			br.write(treeStructure.lines.get(i).endPoint.name + " ");
			br.write(treeStructure.lines.get(i).startPoint.name);
			br.newLine();
		}
		br.newLine();
	}
	
	public void test() throws IOException {
		int correct = 0;
		
		for(int i=0; i<testDataSet.data.size(); i++) {
			Map<Attribute, String> record = testDataSet.data.get(i);
			
			// Find the max
			double res = -1;
			String resString = "";
			for(String yString : testDataSet.classLabel.possibleValues) {
				double tmpRes = trainDataSet.pyX(yString, record);
				
				if(tmpRes > res) {
					res = tmpRes;
					resString = yString;
				}
			}
			
			// Output.
			br.write(resString + " " + record.get(testDataSet.classLabel) + " ");
			br.write(Double.toString(trainDataSet.pyX(resString, record)));
			br.newLine();
			
			if(record.get(testDataSet.classLabel).equals(resString)) correct++;
		}
		
		br.newLine();
		br.write(Integer.toString(correct));
		br.newLine();
		br.close();
	}
	
}
