package manageDatabase;
import java.util.ArrayList;
/**
 * Select Inherit Query<br>
 * Realized while parsing SQL query.<br>
 * Contains:  
 * 
 * @see Query DBExecutor
 * 
 */
public class Select extends Query{
	
	/**
	 * Columns to be select
	 */
	private ArrayList<String> attrList;
	
	/**
	 * target table to search
	 */
	private ArrayList<String> tableNames;
	
	/**
	 * Conditions specified with "WHERE" command
	 */
	private Condition cond;
	
	/**
	 * 
	 */
	private boolean selectAll;
	
	/**
	 * 
	 */
	private boolean isNormalUser = false;

	
	/**
	 * Constructor with select conditions
	 */
	public Select(ArrayList<String> attrList, ArrayList<String> tableNames, Condition cond){
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.attrList = attrList;
		this.cond = cond;
	}

	
	/**
	 * Constructor with select "*" mark
	 */
	public Select(ArrayList<String> tableNames, Condition cond, boolean selectAll){
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.cond = cond;
		this.selectAll = true;
	}

	
	/**
	 * getter of tables to be selected
	 */
	public ArrayList<String> getTableNames(){
		return this.tableNames;
	}

	
	/**
	 * getter of columns to be selected
	 */
	public ArrayList<String> getAttrStrList(){
		return this.attrList;
	}

	
	/**
	 * getter of select conditions
	 */
	public Condition getCondition(){
		return this.cond;
	}

	
	/**
	 * checker of "*" mark
	 */
	public boolean isSelectAll(){
		return this.selectAll;
	}

	
	/**
	 * 
	 */
	public void setNormalUser(){
		this.isNormalUser = true;
	}

	
	/**
	 * 
	 */
	public boolean isNormalUser(){
		return this.isNormalUser;
	}

}
