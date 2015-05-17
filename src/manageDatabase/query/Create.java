package manageDatabase.query;

import java.util.ArrayList;
import java.util.Hashtable;

import structure.Attribute;
import structure.Table;

/**
 * Create Inherit Query<br>
 * Realized while parsing SQL query.<br>
 * Contains: table name, Attribute name list, Attribute Position table, Primary List. 
 * 
 * @see Query DBExecutor
 */
public class Create extends Query{
	
	/**
	 * List of Attributes
	 */
	private ArrayList <Attribute> attributes;
	
	/**
	 * Position table of Primary key
	 */
	private ArrayList <Integer> primary;
	
	/**
	 * Position table of Attribute
	 */
	private Hashtable <String, Integer> attrPosTable; //used for attribute position in attributes
	
	/**
	 * Table name
	 */
	private String tableName;
	
	/**
	 * Constructor with:<br>
	 * table name<br>attribute list<br> Primary Position table<br>Attribute Postion table<br>
	 * <br>
	 */
	public Create(
			String tableName,
			ArrayList <Attribute> attrList, 
			ArrayList<Integer> primary, 
			Hashtable <String, Integer> attrPosTable
			) 
	{
		this.queryName = "Create";
		this.tableName = tableName;
		this.attributes = attrList;
		this.primary = primary;
		this.attrPosTable = attrPosTable;
	}
	
	/**
	 * Getter of table name
	 * @return table name
	 */
	public String getTableName(){
		return tableName;
	}
	/**
	 * Getter of attribute list
	 * @return list of attributes
	 */
	public ArrayList <Attribute> getAttributes(){
		return this.attributes;
	}
	
	/**
	 * Getter of Primary key position table
	 * @return primary key table
	 */
	public ArrayList<Integer> getPrimary(){
		return this.primary;
	}
	
	/**
	 * 
	 * @return attribute position table
	 */
	public Hashtable <String, Integer> getAttrPosTalbe(){
		return this.attrPosTable;
	}
	
	/**
	 * new a table
	 * @return table
	 */
	public Table makeTable(){
		//System.out.println(this.attributes.size());
		return new Table(
				this.tableName, 
				this.attributes, 
				this.primary, 
				this.attrPosTable
				);
	}

	/**
	 * Get the attribute position of specified column name
	 * @param valueName : name of the column
	 * @return position of the column
	 */
	public int getAttrPos(String valueName){
		if(this.attrPosTable.containsKey(valueName)){
			return this.attrPosTable.get(valueName).intValue();
		}else{
			return -1;
		}	
	}
}

