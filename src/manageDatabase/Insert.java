package manageDatabase;

import java.util.ArrayList;

/**
 *     This is the class to parse the SQL instruction: INSERT. <br>
 * And this class will be used in Parser to pass informations
 * to Executer. <br>
 * 
 * @see Query DBExecutor
 * 
 */

public class Insert extends Query{
	/**
	 * Values to be inserted, collected while parsing
	 */
	private ArrayList<String> valueList;
	/**
	 * Target table to be inserted
	 */
	private String tableName;
	
	/**
	 * Basic constructor with table name
	 * @param tableName 
	 * : the target table to be inserted.
	 */
	public Insert(String tableName){
		this.tableName = tableName;
	}
	
	/**
	 * Constructor overloads the basic one
	 * @param tableName
	 * : the target table to be inserted.
	 * @param valueList
	 * : the value to be inserted.
	 */
	public Insert(String tableName, ArrayList<String> valueList){
		this.queryName = "INSERT";
		this.tableName = tableName;
		this.valueList = valueList;
	}
	
	/**
	 * Setter of private menber: tableName
	 * 
	 * @param name
	 * : name of the target table to be inserted
	 */
	public void setTableName(String name){
		this.tableName = name;
	}
	
	/**
	 * Getter of private menber: tableName
	 * @return : the name of target table
	 */
	public String getTableName(){
		return this.tableName;
	}
	
	/**
	 * Setter of private menber: valueList 
	 * @param list : the valueList to be inserted
	 */
	public void setArrayList(ArrayList<String> list){
		this.valueList = list;
	}
	
	/**
	 * Getter of private menber: valueList
	 * @return : the valueList
	 */
	public ArrayList<String> getValueList(){
		return this.valueList;
	}
}
