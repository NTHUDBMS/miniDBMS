/**
 * 
 */
package structure;

import java.util.ArrayList;

/**
 *
 */
public class Tuple extends ArrayList<Value>{

	/**
	 * Serialize ID to implement Serializable interface
	 */
	private static final long serialVersionUID = 7662373687797491697L;
	
	/**
	 * Data of this tuple
	 */
	private ArrayList<Value> data;
	
	/**
	 * Constructor
	 * @param data : the data of tuple
	 */
	public Tuple(ArrayList<Value>data){
		this.setData(data);
	}

	public ArrayList<Value> getData() {
		return data;
	}

	public void setData(ArrayList<Value> data) {
		this.data = data;
	}
	

}
