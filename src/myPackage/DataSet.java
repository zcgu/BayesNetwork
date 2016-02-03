package myPackage;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import myPackage.TreeStructure.Line;

public class DataSet {

	// Relation Name.
	public String relation;
	
	// All attributes.
	public ArrayList<Attribute> attributeList;

	// Class( The last attribute)
	public Attribute classLabel;
	
	// The data.
	public ArrayList<Map<Attribute, String> > data;
	
	// tmp cache.
//	private PList pList;
	
	public DataSet() {
		relation = null;
		attributeList = new ArrayList<>();
		classLabel = null;
		data = new ArrayList<>();
//		pList = new PList();
	}
	
	private double pv(Attribute attribute, String string) {
//		if(pList.get(attribute, string) > -0.5) return pList.get(attribute, string);
		
		// Calculate.
		double num = 0;
		for(Map<Attribute, String> map : data) {
			if(map.get(attribute).equals(string)) num += 1;
		}
		
//		pList.put(attribute, string, num / data.size());
		return (num + 1) / (data.size() + attribute.possibleValues.size());
	}
	
	private double pvlv(Attribute attribute1, String string1, Attribute attribute2, String string2) {
//		if(pList.get(attribute1, string1, attribute2, string2) > -0.5)
//			return pList.get(attribute1, string1, attribute2, string2);
		
		// Calculate.
		double num = 0;
		double total = 0;
		for(Map<Attribute, String> map : data) {
			if(map.get(attribute2).equals(string2)){
				total += 1;
				if(map.get(attribute1).equals(string1)) num += 1;
			}
		}
		
//		pList.put(attribute1, string1, attribute2, string2, num / total);
		return (num + 1) / (total+ attribute1.possibleValues.size());
		
	}
	
	private double pvvlv(Attribute attribute1, String string1, Attribute attribute2, String string2, 
			Attribute attribute3, String string3) {
		
		// Calculate.
		double num = 0;
		double total = 0;
		for(Map<Attribute, String> map : data) {
			if(map.get(attribute3).equals(string3)){
				total += 1;
				if(map.get(attribute1).equals(string1) && map.get(attribute2).equals(string2)) num += 1;
			}
		}
		
		return (num + 1) / (total + attribute1.possibleValues.size() * attribute2.possibleValues.size());
	}
	
	private double pvvv(Attribute attribute1, String string1, Attribute attribute2, String string2, 
			Attribute attribute3, String string3) {
		
		// Calculate.
		double num = 0;
		for(Map<Attribute, String> map : data) {
			if(map.get(attribute1).equals(string1) && map.get(attribute2).equals(string2)
					&& map.get(attribute3).equals(string3)){
				num += 1;
			}
		}
		
		return (num + 1) / (data.size() + attribute1.possibleValues.size() * 
				attribute2.possibleValues.size() * attribute3.possibleValues.size());
		
	}
	public double pyXn(String yString, Map<Attribute, String> Xrecord) {
		
		// numerator.
		double numerator = pv(classLabel, yString) * pXyn(Xrecord, yString);
	
		// denominator.
		double denominator = 0;
		for(String s : classLabel.possibleValues) {
			double a = pv(classLabel, s) * pXyn(Xrecord, s);
			denominator += a;
		}
		
		return numerator / denominator;
	}

	private double pXyn(Map<Attribute, String> Xrecord, String yString) {
		double res = 1;
		for(Attribute attribute : attributeList) {
			res *= pvlv(attribute, Xrecord.get(attribute), classLabel, yString);
		}
		return res;
	}

	public double pyXt(String yString, Map<Attribute, String> Xrecord, TreeStructure treeStructure) {
		// numerator.
		double numerator = pv(classLabel, yString) * pXyt(Xrecord, yString, treeStructure);

		// denominator.
		double denominator = 0;
		for(String s : classLabel.possibleValues) {
			double a = pv(classLabel, s) * pXyt(Xrecord, s, treeStructure);
			denominator += a;
		}

		return numerator / denominator;
	}

	private double pXyt(Map<Attribute, String> Xrecord, String yString, TreeStructure treeStructure) {
		double res = 1;

		for(Attribute attribute : attributeList) {
			ArrayList<Attribute > parentList = new ArrayList<>();
			ArrayList<String > stringList = new ArrayList<>();

			for(Line line : treeStructure.lines) {
				if(line.endPoint.equals(attribute)) {
					parentList.add(line.startPoint);
					if(line.startPoint.equals(classLabel)) stringList.add(yString);
					else stringList.add(Xrecord.get(line.startPoint));
				}
			}

			res *= pxlvvv(attribute, Xrecord.get(attribute), parentList, stringList);
		}

		return res;
	}

	private double pxlvvv(Attribute x, String s, ArrayList<Attribute > list, ArrayList<String > slist) {

		double num = 0;
		double total = 0;

		for(Map<Attribute, String > map : data) {
			// Not match condition.
			boolean flag = false;
			for(int i = 0; i < list.size(); i++) if(!map.get(list.get(i)).equals(slist.get(i))) flag = true;
			if(flag) continue;

			total += 1;
			if(map.get(x).equals(s)) num += 1;
		}

		return (num + 1) / (total + x.possibleValues.size());
	}

	public double iXXY(Attribute X1, Attribute X2) {
		double res = 0;
		
		for (String s1 : X1.possibleValues){
			for(String s2 : X2.possibleValues){
				for(String sy : classLabel.possibleValues) {
					res += pvvv(X1, s1, X2, s2, classLabel, sy) 
							* Math.log( pvvlv(X1, s1, X2, s2, classLabel, sy) / 
									(pvlv(X1, s1, classLabel, sy) * pvlv(X2, s2, classLabel, sy)) )
							/ Math.log(2);
				}
			}
		}
		
		return res;
	}
	private class PList {
		ArrayList<Attribute> attributes1;
		ArrayList<String> strings1;
		ArrayList<Attribute> attributes2;
		ArrayList<String> strings2;
		ArrayList<Double> values;
		
		public PList() {
			attributes1 = new ArrayList<>();
			attributes2 = new ArrayList<>();
			strings1 = new ArrayList<>();
			strings2 = new ArrayList<>();
			values = new ArrayList<>();
		}		
		
		public double get(Attribute attribute, String string) {
			for(int i=0; i<values.size(); i++) {
				if(attributes1.get(i).equals(attribute) && strings1.get(i).equals(string)) return values.get(i);
			}
			return -1;
		}
		
		public double get(Attribute attribute1, String string1, Attribute attribute2, String string2) {
			for(int i=0; i<values.size(); i++) {
				if(attributes1.get(i).equals(attribute1) && strings1.get(i).equals(string1)
						&& attributes2.get(i).equals(attribute2) && strings2.get(i).equals(string2)) return values.get(i);
			}
			return -1;
		}
		
		public void put(Attribute attribute, String string, double value) {
			attributes1.add(attribute);
			strings1.add(string);
			attributes2.add(new Attribute());
			strings2.add("");
			values.add(value);
		}
	
		public void put(Attribute attribute1, String string1, Attribute attribute2, String string2, double value) {
			attributes1.add(attribute1);
			strings1.add(string1);
			attributes2.add(attribute2);
			strings2.add(string2);
			values.add(value);
		}
		
	}
}
