package myPackage;

import java.util.ArrayList;

public class Attribute {
	
	public String name;
	
	public ArrayList<String> possibleValues;
	
	public Attribute() {
		name = null;
		possibleValues = new ArrayList<String >();
	}
	
	public void setPossibleValues(ArrayList<String> list) {
		possibleValues = list;
	}
	
	@Override
	public int hashCode() {
		return name.length();
	}
	
	@Override
	public boolean equals(Object object) {
		Attribute attribute = (Attribute) object;
		
		if(this.name.equals(attribute.name)) return true;
		else return false;
	}
}