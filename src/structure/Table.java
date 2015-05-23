package structure;
import java.io.Serializable;
import java.util.*;

import dbms.DBMS;

/**
 * <pre>
 * Table structure which defined a table in DBMS
 * Each table contains:
 * 	1. table name
 * 	2. attributes (list structure)
 * 	3. primary position table (each element indicate the position of primary attribute)
 * 	4. attribute position table (hash attribute name with position)
 * 	5. ![referenceTable] (I don't know WTF is this)
 * 	6. ![subSchemaList] (this either)
 * 
 * </pre>
 */
public class Table implements Serializable {
	/**
	 * ???
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Table name of the table
	 */
	private String tablename;
	
	/**
	 * Attribute list of table
	 */
	private ArrayList<Attribute> attrList;
	
	/**
	 * Primary key position table
	 */
	private ArrayList<Integer> primaryList;
	/**
	 * Position of attributes 
	 */
	private Hashtable<String, Integer> attrPosTable;

	/**
	 * ??? WTF is this
	 */
	private Hashtable<String, ForeignReference> referenceTable;
	/**
	 * ?? WTF is this
	 */
	private ArrayList<String> subSchemaList = null;
	
	/**
	 * 
	 * @param tableName : table name
	 * @param attrList : List of attribute
	 * @param primaryList : primary key position
	 * @param attrPosTable : attribute position
	 */
	public Table(
			String tableName, 
			ArrayList<Attribute> attrList, 
			ArrayList<Integer> primaryList,
			Hashtable<String, Integer> attrPosTable
			) 
	{
		if(tableName!=null)
			this.tablename = tableName;
		else
			DBMS.outConsole("* table name null");
		
		if(attrList!=null){
			this.attrList = new ArrayList<Attribute>();
			this.attrList.addAll(attrList);
		}else
			DBMS.outConsole("* attrList null");
		
		if(primaryList!=null){
			this.primaryList = new ArrayList<Integer>();
			this.primaryList.addAll(primaryList);
		}
		else
			DBMS.outConsole("* primaryList null");
		
		if(attrPosTable!=null){
			this.attrPosTable = new Hashtable<String, Integer>();
			this.attrPosTable.putAll(attrPosTable);
		}
		else
			DBMS.outConsole("* attrPosTable null");
	}
	
	
	public String getTableName() {
		return this.tablename;
	}
	public void setTableName(String tablename) {
		this.tablename = tablename;
	}

	
	public ArrayList<Attribute> getAttrList() {
		return this.attrList;
	}
	public void setAttrList(ArrayList<Attribute> attrList) {
		this.attrList = attrList;
	}


	public ArrayList<Integer> getPrimaryList() {
		return this.primaryList;
	}
	public void setPrimaryList(ArrayList<Integer> primaryList) {
		this.primaryList = primaryList;
	}


	public Hashtable<String, ForeignReference> getReferenceTable() {
		return this.referenceTable;
	}
	public void setReferenceTable(Hashtable<String, ForeignReference> referenceTable) {
		this.referenceTable = referenceTable;
	}
	
	/**
	 * Get Attribute position
	 * @param valueName : name of attribute
	 * @return
	 */
	public int getAttrPos(String valueName){
		if(this.attrPosTable.containsKey(valueName))
		{
			return this.attrPosTable.get(valueName).intValue();
		}else{
			return -1;
		}	
	}

	/**
	 * Get attribute position table
	 * @return
	 */
	public Hashtable<String, Integer> getAttrPosHashtable(){
		return this.attrPosTable;
	}

	public void setSubschema(ArrayList<String> subSchemaList){
		this.subSchemaList = subSchemaList;
	}

	public ArrayList<String> getSubschemaList(){
		return this.subSchemaList;
	}
	
}
