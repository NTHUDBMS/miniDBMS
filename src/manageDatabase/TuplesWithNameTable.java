package manageDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

import structure.Value;

//Wrapper class to save tuple list and name hashtable
public class TuplesWithNameTable {
		
	private Hashtable<String, Integer> nameTable;

	private ArrayList< ArrayList<Value> > tupleList;

	private int updatedTuples = 0;

	public TuplesWithNameTable(Hashtable<String, Integer> nameTable, ArrayList< ArrayList<Value> > tupleList){
		this.nameTable = nameTable;
		this.tupleList = tupleList;
	}

	public int getAttrPos(String valueName){
		if(this.nameTable.containsKey(valueName)){
			return this.nameTable.get(valueName).intValue();
		}else{
			return -1;
		}
	}

	public Hashtable<String, Integer> getNameTable(){
		return this.nameTable;
	} 

	public ArrayList< ArrayList<Value> > getTupleList(){
		return this.tupleList;
	}

	public int getUpdatedTuplesNum(){
		return this.updatedTuples;
	}

	public void setUpdatedTuplesNum(int num){
		this.updatedTuples = num;
	}

}
