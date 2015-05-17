package manageDatabase.query;

import java.util.*;

import structure.*;

/**
 * 
 */
public class TuplesWithAttrPos {
	/**
	 * 
	 */
	private Hashtable<String, Integer> attrPosTable;
	/**
	 * 
	 */
	private TupleStack tupleStack;
	/**
	 * 
	 */
	private int updatedTuples = 0;
	
	/**
	 * Constructor
	 * @param attrPosTable
	 * @param tupleList
	 */
	public TuplesWithAttrPos(
			Hashtable<String, Integer> attrPosTable, 
			TupleStack tupleList)
	{
		this.attrPosTable = attrPosTable;
		this.setTupleStack(tupleList);
	}
	/**
	 * 
	 * @param attributeName
	 * @return
	 */
	public int getAttrPos(String attributeName){
		if(this.attrPosTable.containsKey(attributeName)){
			return this.attrPosTable.get(attributeName).intValue();
		}else{
			return -1;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Hashtable<String, Integer> getAttrPosTable(){
		return this.attrPosTable;
	} 
	/**
	 * 
	 * @return
	 */
	public int getUpdatedTuplesNum(){
		return this.updatedTuples;
	}
	/**
	 * 
	 * @param num
	 */
	public void setUpdatedTuplesNum(int num){
		this.updatedTuples = num;
	}
	public TupleStack getTupleStack() {
		return tupleStack;
	}
	public void setTupleStack(TupleStack tupleStack) {
		this.tupleStack = tupleStack;
	}

}
