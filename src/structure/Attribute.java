package structure;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <br>
 * Contains:<br>
 * 1.attribute name<br>
 * 2.type<br>
 * 3.length for varchar<br>
 * 4.column list<br>
 * 5.primary or not<br>
 * 
 * 
 */
public class Attribute implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The type of the data to be stored<br>
	 * 1. int: integer<br>
	 * 2. varchar: string<br>
	 * 3. null: empty value<br>
	 */
	public static enum Type{
		INT, CHAR, NULL
	}
	/**
	 * Attribute name
	 */
	private String name;
	
	/**
	 * type of attribute
	 */
	private Type type;
	
	/**
	 * if it's primary column
	 */
	private boolean primary;
	
	/**
	 * length of the data stored under this attribute
	 */
	private int length = 0;
	
	/**
	 * Data stored under this attribute
	 */
	private ArrayList<Value> columnList;
	
	/**
	 * Constructor with specified length
	 * @param type
	 * @param attrName
	 * @param length
	 */
	public Attribute(Type type, String attrName, int length) {
		this.name = attrName;
		this.type = type;
		this.length = length;
	}
	
	/**
	 * Constructor without specified length 
	 * @param type
	 * @param attrName
	 */
	public Attribute(Type type, String attrName) {
		this(type,attrName,0);
	}
	
	public Attribute(Attribute attribute) {
	    this.name = attribute.name;
	    this.type = attribute.type;
	    this.length = attribute.length;
    }

    @Override
    public String toString() {
        return "Attribute [name=" + name + "]";
    }

    /**
	 * 
	 * @return name of attribute
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param name name of attribute
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return type of attribute
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * 
	 * @param type type of attribute
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return if this attribute is primary key
	 */
	public boolean isPrimary() {
		return this.primary;
	}
	
	/**
	 * set this attribute to primary key
	 */
	public void setPrimary() {
		this.primary = true;
	}
	
	/**
	 * 
	 * @return the length of data stored under this attribute
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * 
	 * @param length the length of data stored under this attribute
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	
	/**
	 * Override equals method to do compare<br>
	 * if name of attribute is the same, true
	 */
	public boolean equals(Attribute o){
		if(this.name.equals(o.getName())) 
			return true;
		else
			return false;
		
	}
	
	public boolean equals(String o) {
	    if (this.name.equals(o)) {
	        return true;
	    }
	    else return false;
	}
	
	/**
	 * 
	 * @return the data store under this attribute
	 */
	public ArrayList<Value> getColumnList() {
		return columnList;
	}

	/**
	 * set the data of this attribute to input 
	 * @param columnList the data store under this attribute
	 */
	public void setColumnList(ArrayList<Value> columnList) {
		this.columnList = columnList;
	}
}
