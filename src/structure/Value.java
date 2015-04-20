package structure;
/**
 * Value implement Serializable
 * 
 */
public class Value implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274565752285L;

	/**
	 * Value type that parsed in SQL command
	 */
	private Attribute.Type type;

	/**
	 * Value if type is Integer
	 */
	private int intValue;
	/**
	 * Value if type is varchar
	 */
	private String charValue;

	/**
	 * Construct by integer
	 * @param intValue : integer value
	 */
	public Value(int intValue){
		this.intValue = intValue;
		this.type = Attribute.Type.INT;
	}

	/**
	 * Construct by char
	 * @param charValue : String value
	 */
	public Value(String charValue){
		this.charValue = charValue;
		this.type = Attribute.Type.CHAR;
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
	public boolean equals(Value value){

		if(value.getType() == this.type){
			if(this.type == Attribute.Type.CHAR){
				if(value.getChar().equals(this.getChar())){
					return true;
				}
			}else if(this.type == Attribute.Type.INT){
				if(value.getInt() == this.getInt()){
					return true;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		return false;

	}
}
