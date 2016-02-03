package myPackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import myPackage.Tree.Line;

public class DataSet {

	// Relation Name.
	public String relation;
	
	// All attributes.
	public ArrayList<Attribute> attributeList;

	// Class( The last attribute)
	public Attribute classLabel;
	
	// The data.
	public ArrayList<Map<Attribute, String> > data;
	
	public DataSet() {
		relation = null;
		attributeList = new ArrayList<>();
		classLabel = null;
		data = new ArrayList<>();
	}

	public static class Attribute {

		public String name;

		public ArrayList<String> possibleValues;

		public Attribute() {
			name = null;
			possibleValues = new ArrayList<>();
		}

		@Override
		public int hashCode() {
			return name.length();
		}

		@Override
		public boolean equals(Object object) {
			if(!(object instanceof Attribute)) return false;

			Attribute attribute = (Attribute) object;
			return this.name.equals(attribute.name);
		}
	}

	public static class AttributeValuePair {
		public Attribute attribute;
		public String value;

		AttributeValuePair(Attribute attribute, String value) {
			this.attribute = attribute;
			this.value = value;
		}
	}

	/**
	 * This function is use to calculate condition probability.
	 * Every probability (including non-condition probability) is calculate by this function.
	 *
	 * @param list1 : the variables and corresponding values on the left.
	 * @param list2 : the variables and corresponding values on the right.
     * @return : calculate result.
     */
	public double pCondition(List<AttributeValuePair> list1, List<AttributeValuePair> list2) {

		double num = 0;
		double total = 0;

		for(Map<Attribute, String > map : data) {
			// Not match condition.
			boolean flag = false;
			for(AttributeValuePair pair : list2) if(!map.get(pair.attribute).equals(pair.value)) flag = true;
			if(flag) continue;

			total += 1;

			flag = false;
			for(AttributeValuePair pair : list1) if(!map.get(pair.attribute).equals(pair.value)) flag = true;
			if(flag) continue;

			num += 1;
		}

		double add = 1;
		for (AttributeValuePair pair : list1) add *= pair.attribute.possibleValues.size();
		return (num + 1) / (total + add);
	}

	private double p(Attribute attribute, String value) {

		ArrayList<AttributeValuePair> list1 = new ArrayList<>();
		ArrayList<AttributeValuePair> list2 = new ArrayList<>();
		list1.add(new AttributeValuePair(attribute, value));

		return pCondition(list1, list2);
	}

	private double p(Attribute attribute1, String value1, Attribute attribute2, String value2,
					 Attribute attribute3, String value3) {

		ArrayList<AttributeValuePair> list1 = new ArrayList<>();
		ArrayList<AttributeValuePair> list2 = new ArrayList<>();
		list1.add(new AttributeValuePair(attribute1, value1));
		list1.add(new AttributeValuePair(attribute2, value2));
		list1.add(new AttributeValuePair(attribute3, value3));

		return pCondition(list1, list2);

	}

	private double pCondition(Attribute attribute1, String value1, Attribute attribute2, String value2) {

		ArrayList<AttributeValuePair> list1 = new ArrayList<>();
		ArrayList<AttributeValuePair> list2 = new ArrayList<>();
		list1.add(new AttributeValuePair(attribute1, value1));
		list2.add(new AttributeValuePair(attribute2, value2));

		return pCondition(list1, list2);
	}

	public double pyX(String yString, Map<Attribute, String> Xrecord, Tree tree) {
		// numerator.
		double numerator = p(classLabel, yString) * pXy(Xrecord, yString, tree);

		// denominator.
		double denominator = 0;
		for(String s : classLabel.possibleValues) {
			double a = p(classLabel, s) * pXy(Xrecord, s, tree);
			denominator += a;
		}

		return numerator / denominator;
	}

	private double pXy(Map<Attribute, String> Xrecord, String yString, Tree tree) {
		double res = 1;

		for(Attribute attribute : attributeList) {

			ArrayList<AttributeValuePair> list1 = new ArrayList<>();
			list1.add(new AttributeValuePair(attribute, Xrecord.get(attribute)));

			ArrayList<AttributeValuePair> list2 = new ArrayList<>();
			for(Line line : tree.lines) {
				if(line.endPoint.equals(attribute)) {
					AttributeValuePair pair;

					if(line.startPoint.equals(classLabel)) pair = new AttributeValuePair(line.startPoint, yString);
					else pair = new AttributeValuePair(line.startPoint, Xrecord.get(line.startPoint));

					list2.add(pair);
				}
			}

			res *= pCondition(list1, list2);
		}

		return res;
	}


	public double i(Attribute X1, Attribute X2) {
		double res = 0;
		
		for (String s1 : X1.possibleValues){
			for(String s2 : X2.possibleValues){
				for(String sy : classLabel.possibleValues) {
					ArrayList<AttributeValuePair> list1 = new ArrayList<>();
					ArrayList<AttributeValuePair> list2 = new ArrayList<>();
					list1.add(new AttributeValuePair(X1, s1));
					list1.add(new AttributeValuePair(X2, s2));
					list2.add(new AttributeValuePair(classLabel, sy));

					res += p(X1, s1, X2, s2, classLabel, sy)
							* Math.log( pCondition(list1, list2) /
									(pCondition(X1, s1, classLabel, sy) * pCondition(X2, s2, classLabel, sy)) )
							/ Math.log(2);
				}
			}
		}
		
		return res;
	}

}
