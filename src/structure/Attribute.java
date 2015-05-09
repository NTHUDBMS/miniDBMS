package structure;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 1.attribute name
 * 2.type
 * 3.length for varchar
 * 4.column list
 * 5.primary or not
 * 
 * 
 */
public class Attribute implements Serializable {
private static final long serialVersionUID = 1L;
	
	public static enum Type{
		INT, CHAR, NULL
	}

	//these value will be serialized
	private String name;
	private Type type;
	private boolean primary;
	private int length = 0;
	private ArrayList<Value> columnList = null; //store column value
	public Attribute(Type type, String attrName, int length) {
		this.name = attrName;
		this.type = type;
		this.length = length;
	}
	
	public Attribute(Type type, String attrName) {
		this.name = attrName;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean isPrimary() {
		return this.primary;
	}
	
	public void setPrimary() {
		this.primary = true;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	
	//public String toString(){
		//return this.name;
	//}
	
	public boolean equals(Object o){
		if(this.name.equals(o.toString())) //toString use java library
			return true;
		else
			return false;
		
	}

	public ArrayList<Value> getColumnList() {
		return columnList;
	}

	public void setColumnList(ArrayList<Value> columnList) {
		this.columnList = columnList;
	}
}
