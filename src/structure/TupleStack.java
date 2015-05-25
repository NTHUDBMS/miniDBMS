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
	 * Number of tuple
	 */
	private int length;
	
	
	/**
	 * Data in column view
	 * Column is arrayList here
	 */
	private ArrayList<Column> columnList; 
	
	/**
	 * Ordered attribute name of columns 
	 */
	private ArrayList<Attribute> attrList;
	private ArrayList<String> selectattrList;
//	private ArrayList<String> selectattrListWithTable;

	/**
	 * Position of attribute  attribute to tableId
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
		this.setWidth(attrList.size());
		this.setLength(this.size());
		this.setColumnList(new ArrayList<Column>());
	}
	
	/**
	 * Normal Constructor by using super class
	 */
	public TupleStack(ArrayList<Attribute> attrList) {
		super();
		this.setAttrList(attrList); // build attrPosTable as the same time
		this.setWidth(attrList.size());
		this.setLength(this.size());
		// no need to add column view at this stage
		// but add tuple is used this arrayList...
		this.setColumnList(new ArrayList<Column>());
	}
	/**
	 * Normal Constructor by using super class
	 * input String attrList because of select
	 * @param selectattrList is attrlist selected 
	 * @param bool select is useless, distinguish constructor
	 */
	public TupleStack(ArrayList<String> selectattrList,boolean select) {
		super();
		this.setselectattrList(selectattrList); // build attrPosTable as the same time
		this.setWidth(selectattrList.size());
		this.setLength(this.size());
		this.setColumnList(new ArrayList<Column>());
	}
	
	
	public TupleStack(){
		super();//call arrayList<Tuple> constructor
	}


	/**
	 * This method also build up attribute position table
	 * @param attrList
	 */
	public void setAttrList(ArrayList<Attribute> attrList) {
		this.attrList = attrList;

//		 construct attrPosTable
		 this.attrPosTable = new Hashtable<String,Integer>();
		 int i = 0;
		 for(Attribute attr : attrList){
		 	this.attrPosTable.put(attr.getName(), i);
		 	i++;
		 }
	}
	
	
	/**
	 * set the name of each each attributes first
	 * then construct TupleStack(ArrayList<Attribute> attrList)
	 * 
	 * @param list of columnNames
	 * @return new tupleStacks
	 */
	public TupleStack newStackByColumns(ArrayList<String> columnNames){
		ArrayList<Attribute> newAttrList = new ArrayList<Attribute>();
		int i=0;
		for(Attribute a : this.attrList){
			//just return true or false not assign
//			a.getName().equals(columnNames.get(i));
			a.setName(columnNames.get(i));
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
//			if(attrList.get(i).getType() == v.getType())
			{
//				(columnList.get(i)).add(v);
//				i++;
			}
//			else throw new Error("INSERT: wrong data type.");
		}
		ans = super.add(tuple);
		++this.length; //add length
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
	
	/**
	 * Override super method: E remove(int).<br>
	 * Also remove tuple form column view
	 */
	public Tuple remove(int index){
		Tuple ret = this.remove(index);
		for(Column c : columnList){
			c.remove(index);
		}
		
		return ret;
	}


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


	/**
	 * This method also build up attribute position table
	 * @param attrList
	 */
	public void setselectattrList(ArrayList<String> selectattrList) {
		this.setSelectattrList(selectattrList);
		// construct attrPosTable
		this.attrPosTable = new Hashtable<String,Integer>();
		 int i = 0;
		 for(String attr : selectattrList){
		 	this.attrPosTable.put(attr, i);
		 	i++;
		 }
	}

	public ArrayList<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(ArrayList<Column> columnList) {
		
		for(int i=0; i<this.width; i++){
			columnList.add(new Column());
		}
		this.columnList = columnList;
	}

	public ArrayList<String> getSelectattrList() {
		return selectattrList;
	}

	public void setSelectattrList(ArrayList<String> selectattrList) {
		this.selectattrList = selectattrList;
	}

//	public ArrayList<String> getSelectattrListWithTable() {
//		return selectattrListWithTable;
//	}
//
//	public void setSelectattrListWithTable(ArrayList<String> selectattrListWithTable) {
//		this.selectattrListWithTable = selectattrListWithTable;
//	}
	
	
}
