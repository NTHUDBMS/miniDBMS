package manageDatabase;

import structure.Value;
import dbms.DBExecutor;

public class ColExp extends Exp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName;
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



}
