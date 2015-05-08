package structure;

import java.io.Serializable;
import java.util.ArrayList;

public class TupleFileTemp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList <ArrayList <Value>> tupleList;
	String tableName;
	
	
	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public TupleFileTemp(String tableName,ArrayList <ArrayList <Value>> tupleList) {
		this.tableName = tableName;
		this.setTupleList(tupleList);
		// TODO Auto-generated constructor stub
	}


	public ArrayList <ArrayList <Value>> getTupleList() {
		return tupleList;
	}


	public void setTupleList(ArrayList <ArrayList <Value>> tupleList) {
		this.tupleList = tupleList;
	}

}
