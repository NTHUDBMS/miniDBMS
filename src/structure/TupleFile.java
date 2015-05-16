package structure;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 *
 */
public class TupleFile implements Serializable {
	/**
	 * ???
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 2-D array of Value<br>
	 * array of tuples<br>
	 */
	private ArrayList <ArrayList <Value>> tupleList;
	
	
	/**
	 * Table name of this tuple stack<br>
	 * File will named as "tableName".db
	 */
	private String tableName;
	
	/**
	 * 
	 * @return name of table name of this tuple stack
	 */
	public String getTableName() {
		return this.tableName;
	}


	/**
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	/**
	 * Constructor with table name, tuple stack
	 * @param tableName
	 * @param tupleList
	 */
	public TupleFile(
			String tableName,
			ArrayList <ArrayList <Value>> tupleList) 
	{
		this.tableName = tableName;
		this.setTupleList(tupleList);
	}

	/**
	 * 
	 * @return the tuple stack
	 */
	public ArrayList <ArrayList <Value>> getTupleList() {
		return tupleList;
	}

	/**
	 * 
	 * @param tupleList the tuple stack
	 */
	public void setTupleList(ArrayList <ArrayList <Value>> tupleList) {
		this.tupleList = tupleList;
	}

}
