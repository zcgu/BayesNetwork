package myPackage;

import java.util.ArrayList;
import java.util.Comparator;
import myPackage.DataSet.Attribute;

/**
 * This class represent model.
 *
 */
public class Tree {
	
	public ArrayList<UnDirectionLine > unDirectionLines;
	
	public ArrayList<Line> lines;
	
	public Tree() {
		unDirectionLines = new ArrayList<>();
		lines = new ArrayList<>();
	}

	/**
	 * This is the edge with direction.
	 *
	 */
	static public class Line {
		
		public Attribute startPoint;
		public Attribute endPoint;

		public Line(Attribute startPoint, Attribute endPoint) {
			this.endPoint = endPoint;
			this.startPoint = startPoint;
		}
	}

	/**
	 * Edge without direction.
	 *
	 */
	static public class UnDirectionLine {
		
		public Attribute point1;
		public Attribute point2;
		
		public double i = 0;
		
		public UnDirectionLine() {
			point1 = new Attribute();
			point2 = new Attribute();
		}
	}

	/**
	 * Comparator for line without direction.
	 *
	 */
	public static class UnDirectionLineComparator implements Comparator<UnDirectionLine> {

		DataSet trainDataSet;

		public UnDirectionLineComparator(DataSet trainDataSet) {
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
}
