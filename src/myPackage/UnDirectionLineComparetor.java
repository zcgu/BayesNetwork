package myPackage;

import java.util.Comparator;

import myPackage.TreeStructure.UnDirectionLine;

public class UnDirectionLineComparetor implements Comparator<UnDirectionLine>{

	DataSet trainDataSet;
	
	public UnDirectionLineComparetor(DataSet trainDataSet) {
		this.trainDataSet = trainDataSet;
	}
	
	@Override
	public int compare(UnDirectionLine line1, UnDirectionLine line2) {
		if(Math.abs(line1.i - line2.i) < 1e-10) {
			int p11 = trainDataSet.attributeList.indexOf(line1.point1);
			int p12 = trainDataSet.attributeList.indexOf(line1.point2);
			int p21 = trainDataSet.attributeList.indexOf(line2.point1);
			int p22 = trainDataSet.attributeList.indexOf(line2.point2);
			
			if(Math.min(p11, p12) < Math.min(p21, p22)) return 1;
			else if (Math.min(p11, p12) < Math.min(p21, p22)) return -1;
			else{
				if(Math.max(p11, p12) < Math.max(p21, p22)) return 1;
				else return -1;
			}
		} else {
			if(line1.i > line2.i) return 1;
			else return -1;
		}
	}

}
