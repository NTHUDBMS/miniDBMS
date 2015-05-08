package structure;
import java.io.Serializable;

/**
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
	
	public String toString(){
		return this.name;
	}
	
	public boolean equals(Object o){
		if(this.name.equals(o.toString()))
			return true;
		else
			return false;
		
	}
}
