/**
 * 
 */
package structure;

import java.util.ArrayList;

/**
 * Self-defined class of an ArrayList of Value<br>
 * Add something more
 * row view 
 */
public class Tuple extends ArrayList<Value>{

	/**
	 * Serialize ID to implement Serializable interface
	 */
	private static final long serialVersionUID = 7662373687797491697L;
	
	public Tuple(){
		super();
	}
	
	public Tuple(ArrayList<Value> data){
		super(data);
	}
	
	public Tuple(Tuple t) {
	    for (Value v: t) {
	        this.add(new Value(v));
	    }
	}
}
