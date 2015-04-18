package manageDatabase;
import java.util.ArrayList;

public class Select extends Query{

	private ArrayList<String> attrList;
	private ArrayList<String> tableNames;
	private Condition cond;
	private boolean selectAll;
	private boolean isNormalUser = false;

	public Select(ArrayList<String> attrList, ArrayList<String> tableNames, Condition cond){
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.attrList = attrList;
		this.cond = cond;
	}

	public Select(ArrayList<String> tableNames, Condition cond, boolean selectAll){
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.cond = cond;
		this.selectAll = true;
	}

	public ArrayList<String> getTableNames(){
		return this.tableNames;
	}

	public ArrayList<String> getAttrStrList(){
		return this.attrList;
	}

	public Condition getCondition(){
		return this.cond;
	}

	public boolean isSelectAll(){
		return this.selectAll;
	}

	public void setNormalUser(){
		this.isNormalUser = true;
	}

	public boolean isNormalUser(){
		return this.isNormalUser;
	}

}
