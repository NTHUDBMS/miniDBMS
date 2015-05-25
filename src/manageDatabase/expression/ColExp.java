package manageDatabase.expression;

import java.util.Hashtable;

import structure.Tuple;
import structure.Value;
import dbms.DBExecutor;

public class ColExp extends Exp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName; /*could be alias */
	private String colomnName;

	public ColExp(String tableName, String colomnName){
		this.setColomnName(colomnName);
		this.setTableName(tableName);
	}

	public String getColomnName() {
		return colomnName;
	}

	public void setColomnName(String colomnName) {
		this.colomnName = colomnName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * 
	 * @see Exp
	 */
	public Object accept(DBExecutor visitor, Value value) {
		return visitor.visit(this, value); 
	}

	public Object accept(
			DBExecutor visitor, 
			Hashtable<String, Integer> attrPosTable, 
			Tuple tuple) 
	{
		return visitor.visit(this, attrPosTable, tuple); 
	}

}
