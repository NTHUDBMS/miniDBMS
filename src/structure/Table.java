package structure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;


public class Table implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tablename;
	private ArrayList<Attribute> attrList;
	private ArrayList<Integer> primaryList;
	private Hashtable<String, ForeignReference> referenceTable;
	
	public Table(String tableName, ArrayList<Attribute> attrList, ArrayList<Integer> primaryList) {
		this.tablename = tableName;
		this.attrList = attrList;
		this.primaryList = primaryList;
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
	
}
