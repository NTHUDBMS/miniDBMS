/**
 * 
 */
package structure;

import java.util.ArrayList;

/**
 * Self-defined class of an ArrayList of Value<br>
 * Add something more
 */
public class Tuple extends ArrayList<Value>{

	/**
	 * Serialize ID to implement Serializable interface
	 */
	private static final long serialVersionUID = 7662373687797491697L;
	
	public Tuple(){
		super();
	}
	
	public Tuple(ArrayList<Value>data){
		super(data);
	}
}
