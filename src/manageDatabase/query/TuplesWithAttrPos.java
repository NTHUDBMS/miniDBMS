package manageDatabase.query;

import java.util.ArrayList;
import java.util.Hashtable;

import structure.Value;

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
	private ArrayList< ArrayList<Value> > tupleList;
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
			ArrayList< ArrayList<Value> > tupleList)
	{
		this.attrPosTable = attrPosTable;
		this.tupleList = tupleList;
	}
	/**
	 * 
	 * @param valueName
	 * @return
	 */
	public int getAttrPos(String valueName){
		if(this.attrPosTable.containsKey(valueName)){
			return this.attrPosTable.get(valueName).intValue();
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
	public ArrayList< ArrayList<Value> > getTupleList(){
		return this.tupleList;
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

}
