package myPackage;
import java.awt.List;
import java.util.ArrayList;
import java.util.Map;

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
	public double pyX(String yString, Map<Attribute, String> Xrecord) {
		
		// numerator.
		double numerator = pv(classLabel, yString) * pXy(Xrecord, yString);
	
		// denominator.
		double denominator = 0;
		for(String s : classLabel.possibleValues) {
			double a = pv(classLabel, s) * pXy(Xrecord, s);
			denominator += a;
		}
		
		return numerator / denominator;
	}

	public double pXy(Map<Attribute, String> Xrecord, String yString) {
		double res = 1;
		for(Attribute attribute : attributeList) {
			res *= pvlv(attribute, Xrecord.get(attribute), classLabel, yString);
		}
		return res;
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
				if(attributes1.get(i).equals(attribute) && strings1.equals(string)) return (double) values.get(i);
			}
			return -1;
		}
		
		public double get(Attribute attribute1, String string1, Attribute attribute2, String string2) {
			for(int i=0; i<values.size(); i++) {
				if(attributes1.get(i).equals(attribute1) && strings1.equals(string1)
						&& attributes2.get(i).equals(attribute2) && strings2.equals(string2)) return (double) values.get(i);
			}
			return -1;
		}
		
		public void put(Attribute attribute, String string, double value) {
			attributes1.add(attribute);
			strings1.add(string);
			attributes2.add(new Attribute());
			strings2.add("");
			values.add(new Double(value));
		}
	
		public void put(Attribute attribute1, String string1, Attribute attribute2, String string2, double value) {
			attributes1.add(attribute1);
			strings1.add(string1);
			attributes2.add(attribute2);
			strings2.add(string2);
			values.add(new Double(value));
		}
		
	}
}
