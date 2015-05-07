package manageDatabase.query;

import java.util.ArrayList;
import java.util.Hashtable;

import structure.Value;

/**
 * 
 */
public class TuplesWithNameTable {
	/**
	 * 
	 */
	private Hashtable<String, Integer> nameTable;
	/**
	 * 
	 */
	private ArrayList< ArrayList<Value> > tupleList;
	/**
	 * 
	 */
	private int updatedTuples = 0;
	
	/**
	 * 
	 * @param nameTable
	 * @param tupleList
	 */
	public TuplesWithNameTable(Hashtable<String, Integer> nameTable, ArrayList< ArrayList<Value> > tupleList){
		this.nameTable = nameTable;
		this.tupleList = tupleList;
	}
	/**
	 * 
	 * @param valueName
	 * @return
	 */
	public int getAttrPos(String valueName){
		if(this.nameTable.containsKey(valueName)){
			return this.nameTable.get(valueName).intValue();
		}else{
			return -1;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Hashtable<String, Integer> getNameTable(){
		return this.nameTable;
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
