package manageDatabase;

import java.util.ArrayList;

public class Insert extends Query{
	private ArrayList<String> valueList;
	private String tableName;
	
	
	public Insert(String tableName, ArrayList<String> valueList){
		this.queryName = "INSERT";
		this.tableName = tableName;
		this.valueList = valueList;
	}
	
	public String getTableName(){
		return this.tableName;
	}
	
	public ArrayList<String> getValueList(){
		return this.valueList;
	}
}
