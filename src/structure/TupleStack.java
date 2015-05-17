/**
 * 
 */
package structure;

import java.util.*;

/**
 * The space to store all tuples, which have column view * row view
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
	private ArrayList<Columns> columnList;
	
	/**
	 * Ordered attribute name of columns 
	 */
	private ArrayList<Attribute> attrList;
	
	/**
	 * Position of attribute
	 */
	private Hashtable<String,Integer> attrPosTable;

	/**
	 * 
	 * @param tupleStack
	 * @param attrList
	 */
	public TupleStack(
			ArrayList<Tuple> tupleStack,
			ArrayList<Attribute> attrList)
	{
		super(tupleStack);
		this.setAttrList(attrList); // build attrPosTable as the same time
		this.setColumnList(new ArrayList<Columns>());
		this.setWidth(attrList.size());
		this.setLength(this.size());
	}
	
	/**
	 * Normal Constructor by using super class
	 */
	public TupleStack(ArrayList<Attribute> attrList) {
		super();
		this.setAttrList(attrList); // build attrPosTable as the same time
		this.setColumnList(new ArrayList<Columns>());
		this.setWidth(attrList.size());
		this.setLength(this.size());
	}
	
	
	public TupleStack(){
		super();
	}

	/**
	 * 
	 * @param columnNames
	 * @return
	 */
	public TupleStack newStackByColumns(ArrayList<String> columnNames){
		ArrayList<Attribute> newAttrList = new ArrayList<Attribute>();
		
		int i=0;
		for(Attribute a : this.attrList){
			a.getName().equals(columnNames.get(i));
			newAttrList.add(a);
			i++;
		}
		
		TupleStack newOne = new TupleStack(newAttrList);
		
		return newOne;
	}
	

	/**
	 * Override super method: boolean add(E e).<br>
	 * Store tuple and insert into column view of TupleStack.<br>
	 */
	public boolean add(Tuple tuple){
		boolean ans;
		
		// Insert into column view
		int i=0;
		for(Value v : tuple){
			if(attrList.get(i).getType() == v.getType()){
				columnList.get(i).add(v);
				i++;
			}else throw new Error("INSERT: wrong data type.");
		}
		ans = super.add(tuple);
		return ans;
	}
	
//	/**
//	 * Override super method: void add(int index, E e).<br>
//	 * Store tuple and insert into column view of TupleStack.<br>
//	 */
//	public void add(int index, Tuple tuple){
//		super.add(index, tuple);
//		
//		// Insert into column view
//		
//	}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public Hashtable<String,Integer> getAttrPosTable() {
		return attrPosTable;
	}


	public void setAttrPosTable(Hashtable<String,Integer> attrPosTable) {
		this.attrPosTable = attrPosTable;
	}

	public ArrayList<Attribute> getAttrList() {
		return attrList;
	}

	public void setAttrList(ArrayList<Attribute> attrList) {
		this.attrList = attrList;

		// construct attrPosTable
		this.attrPosTable = new Hashtable<String,Integer>();
		int i = 0;
		for(Attribute attr : attrList){
			this.attrPosTable.put(attr.getName(), i);
			i++;
		}
	}

	public ArrayList<Columns> getColumnList() {
		return columnList;
	}

	public void setColumnList(ArrayList<Columns> columnList) {
		this.columnList = columnList;
	}
	
	
}
