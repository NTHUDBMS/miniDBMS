package structure;

import com.google.common.base.Objects;

/**
 * 
 * Value implement Serializable<br>
 * this structure is the basic data element in database
 * 
 */
public class Value implements java.io.Serializable, Comparable<Value> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274565752285L;

	/**
	 * Value type that parsed in SQL command
	 */
	private Attribute.Type type;
	
	/**
	 * true if is null
	 */
	private boolean isNull;

	/**
	 * Value if type is Integer
	 */
	private int intValue;
	/**
	 * Value if type is varchar
	 */
	private String charValue;

	public Value(){
		this.type = Attribute.Type.NULL;
		this.isNull = true;
	}
	
	
	/**
	 * Construct by integer
	 * @param intValue : integer value
	 */
	public Value(int intValue){
		this.intValue = intValue;
		this.type = Attribute.Type.INT;
		this.isNull = false;
	}

	/**
	 * Construct by char
	 * @param charValue : String value
	 */
	public Value(String charValue){
		this.charValue = charValue;
		this.type = Attribute.Type.CHAR;
		this.isNull = false;
	}
	
	public Value(Value value) {
	    if (value.type == Attribute.Type.CHAR) {
	        this.charValue = value.charValue;
	        this.isNull = false;
	        this.type = Attribute.Type.CHAR;
	    }
	    else {
	        this.intValue = value.intValue;
	        this.isNull = false;
	        this.type = Attribute.Type.INT;
	    }
           
	}

	/**
	 * Overrides toString()<br>
 	 * make intValue to String if necessary
	 */
	public String toString(){
		if(this.type == Attribute.Type.INT){
			return String.valueOf(this.intValue);
		}else if(this.type == Attribute.Type.CHAR){
			return this.charValue;
		}else{
			return null;
		}
	}

	/**
	 * 
	 * @return integer value if is 'int'
	 */
	public int getInt(){
		return intValue;
	}

	/**
	 * 
	 * @return String value if is 'varchar'
	 */
	public String getChar(){
		return charValue;
	}

	/**
	 * 
	 * @return value type
	 */
	public Attribute.Type getType(){
		return type;
	}

	/**
	 * Compare two Value
	 * @param value : value to be compared
	 * @return
	 *  TRUE if same type & same value<br>
	 */
	public boolean equals(Object obj){
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    Value value = (Value) obj;
		if(value.getType() == this.type){
			if(this.type == Attribute.Type.CHAR){
				if(value.getChar().equals(this.getChar())){
					return true;
				}
			} 
			else if(this.type == Attribute.Type.INT){
				if(value.getInt() == this.getInt()){
					return true;
				}
			} 
		}
        return false;
	}

	@Override
	public int hashCode() {
	    int result = 37;
	    if (this.type == Attribute.Type.CHAR) {
	        result += this.charValue.hashCode() * 37;
        }
	    else if (this.type == Attribute.Type.INT) {
	        result += this.intValue * 37;
	    }
//        System.out.println(result);
	    return result;
	}

	public boolean isNull() {
		return isNull;
	}


    @Override
    public int compareTo(Value o) {
        if (this.type == Attribute.Type.CHAR) {
            return this.charValue.compareTo(o.charValue);
	    }
	    else if (this.type == Attribute.Type.INT) {
	        return this.intValue - o.intValue;
	    }
        return 0;
    }
}
