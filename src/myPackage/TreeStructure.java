package myPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class TreeStructure {
	
	public ArrayList<UnDirectionLine > unDirectionLines;
	
	public ArrayList<Line> lines;
	
	public TreeStructure() {
		unDirectionLines = new ArrayList<>();
		lines = new ArrayList<>();
	}
	
	static public class Line {
		
		public Attribute startPoint;
		public Attribute endPoint;
				
		public Line(Attribute startPoint, Attribute endPoint) {
			this.endPoint = endPoint;
			this.startPoint = startPoint;
		}
	}
	
	static public class UnDirectionLine {
		
		public Attribute point1;
		public Attribute point2;
		
		public double i = 0;
		
		public UnDirectionLine() {
			point1 = new Attribute();
			point2 = new Attribute();
		}
	}
}
