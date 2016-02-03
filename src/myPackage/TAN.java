package myPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.w3c.dom.Attr;

import myPackage.TreeStructure.Line;
import myPackage.TreeStructure.UnDirectionLine;

public class TAN {
	DataSet trainDataSet;
	
	DataSet testDataSet;
	
	TreeStructure treeStructure;
	
	String outPath;
	
	BufferedWriter br;
	
	public TAN(DataSet trainDataSet, DataSet testDataSet, String outPath) throws IOException {
		this.trainDataSet = trainDataSet;
		this.testDataSet = testDataSet;
		this.outPath = outPath;

		File outFile = new File(outPath);
		br = new BufferedWriter(new FileWriter(outFile));
	}
	
	public void setTANStructure() throws IOException {
		treeStructure = new TreeStructure();
		
		// build tree
		buildMST();
		assignDirections();
		for(int i=0; i<trainDataSet.attributeList.size(); i++) {
			Line line = new Line(trainDataSet.classLabel, trainDataSet.attributeList.get(i));
			treeStructure.lines.add(line);
		}
		
		// Output.
		for(int i=0; i<trainDataSet.attributeList.size(); i++) {
			Attribute attribute = trainDataSet.attributeList.get(i);
			ArrayList<Attribute> parent = new ArrayList<>();
			
			// Get all parents.
			for(Line line : treeStructure.lines) {
				if(line.endPoint.equals(attribute)) parent.add(line.startPoint);
			}
			
			// Sort.
			Collections.sort(parent, new Comparator<Attribute>() {
				@Override
				public int compare(Attribute o1, Attribute o2) {
					if (o1.equals(trainDataSet.classLabel)) return 1;
					if (o2.equals(trainDataSet.classLabel)) return -1;
					
					if (trainDataSet.attributeList.indexOf(o1) > trainDataSet.attributeList.indexOf(o2)) return 1;
					else return -1;
				}
			});
			
			// Output.
			br.write(attribute.name);
			for(int j=0; j<parent.size(); j++) br.write(" " + parent.get(j).name);
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
			//TODO : test.
			
			
			// Output.
			br.write(resString + " " + record.get(testDataSet.classLabel) + " " + Double.toString(res));
			br.newLine();
			
			if(record.get(testDataSet.classLabel).equals(resString)) correct++;
		}
		
		br.newLine();
		br.write(Integer.toString(correct));
		br.newLine();
		br.close();
	}

	private void buildMST() {
		ArrayList<Attribute> vnew = new ArrayList<>();
		ArrayList<Attribute> v = trainDataSet.attributeList;
		
		ArrayList<UnDirectionLine> enew = new ArrayList<>();
		ArrayList<UnDirectionLine> e = new ArrayList<>();
		
		// Init.
		vnew.add(trainDataSet.attributeList.get(0));
		for(Attribute X1 : v) {
			for(Attribute X2 : v) {
				UnDirectionLine line = new UnDirectionLine();
				line.point1 = X1;
				line.point2 = X2;
				line.i = trainDataSet.iXXY(X1, X2);
				e.add(line);
			}
		}
		Collections.sort(e, new UnDirectionLineComparetor(trainDataSet));
		
		// Loop
		while(vnew.size() < trainDataSet.attributeList.size()) {
			for(int i=e.size() -1; i >= 0; i--) {
				if(vnew.contains(e.get(i).point1) && !vnew.contains(e.get(i).point2)) {
					vnew.add(e.get(i).point2);
					enew.add(e.get(i));
					break;
				}
				if(!vnew.contains(e.get(i).point1) && vnew.contains(e.get(i).point2)) {
					vnew.add(e.get(i).point1);
					enew.add(e.get(i));
					break;
				}
			}
		}
		
		// finally.
		treeStructure.unDirectionLines = enew;
	}
	
	private void assignDirections() {
		Attribute root = trainDataSet.attributeList.get(0);
		
		// Add first.
		ArrayList<Attribute> que = new ArrayList<>();
		que.add(root);
		
		// Loop.
		while(!que.isEmpty()) {
			Attribute current = que.get(0);
			que.remove(0);
			
			for(int i = treeStructure.unDirectionLines.size() -1; i >= 0; i--){
				if(treeStructure.unDirectionLines.get(i).point1.equals(current)) {
					Line line = new Line(current, treeStructure.unDirectionLines.get(i).point2);
					treeStructure.lines.add(line);
					que.add(line.endPoint);
					treeStructure.unDirectionLines.remove(i);
				}
				else if(treeStructure.unDirectionLines.get(i).point2.equals(current)) {
					Line line = new Line(current, treeStructure.unDirectionLines.get(i).point1);
					treeStructure.lines.add(line);
					que.add(line.endPoint);
					treeStructure.unDirectionLines.remove(i);
				}				
			}
		}
	}
}
