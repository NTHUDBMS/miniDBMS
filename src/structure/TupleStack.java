/**
 * 
 */
package structure;

import java.util.*;

/**
 *
 */
public class TupleStack extends ArrayList<Tuple>{
	
	/**
	 * Serialize ID to implement Serializable interface
	 */
	private static final long serialVersionUID = 7016417759679515823L;
	
	/**
	 * Number of attribute
	 */
	private int width;
	
	/**
	 * Number of attribute
	 */
	private int length;
	
	
	/**
	 * Data in column view
	 */
	private ArrayList<ArrayList<Value>> columnList;
	
	/**
	 * Ordered attribute name of columns 
	 */
	private ArrayList<String> attrList;

	/**
	 * 
	 * @param tupleStack
	 * @param attrList
	 */
	public TupleStack(
			ArrayList<Tuple> tupleStack,
			ArrayList<String> attrList)
	{
		this(tupleStack);
		this.setAttrList(attrList);
		this.setWidth(attrList.size());
		this.setLength(this.size());
	}
	
	
	/**
	 * Normal Constructor by using super class
	 * @param tupleStack
	 */
	public TupleStack(ArrayList<Tuple> tupleStack){
		super(tupleStack);
	}
	
	/**
	 * Override super method: boolean add(E e).<br>
	 * Store tuple and insert into column view of TupleStack.<br>
	 */
	public boolean add(Tuple tuple){
		boolean succeed = super.add(tuple);
		
		// Insert into column view
		
		return succeed;
	}
	
	/**
	 * Override super method: void add(int index, E e).<br>
	 * Store tuple and insert into column view of TupleStack.<br>
	 */
	public void add(int index, Tuple tuple){
		super.add(index, tuple);
		
		// Insert into column view
		
	}
	

	public ArrayList<String> getAttrList() {
		return attrList;
	}

	public void setAttrList(ArrayList<String> attrList) {
		this.attrList = attrList;
	}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public ArrayList<ArrayList<Value>> getColumnList() {
		return columnList;
	}

	public void setColumnList(ArrayList<ArrayList<Value>> columnList) {
		this.columnList = columnList;
	}

	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}
	
	
}
