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
	private ArrayList<String> attrList;//for first table
	private ArrayList<String> attrList2;//for second table
	
	/**
	 * target table to search
	 * tableNames[0] first
	 * tableNames[1] second
	 */
	private ArrayList<String> tableNames; 
	/**
	 * Conditions specified with "WHERE" command
	 */
	private Condition cond;
	
	/**
	 *  SELECT * 
	 */
	private boolean selectAll;
	/**
	 * 
	 */
	private boolean isNormalUser = false;

	private int aggregateMode= 0; 
	/* mode 1 is  Count,  2 is Sum*/
	
	/**
	 * Constructor with select conditions
	 * 2 table 2 attrlist
	 * 1 table 1 attrlist
	 * with condition or not
	 */
	public Select(ArrayList<String> attrList,ArrayList<String >attrList2,
			ArrayList<String> tableNames, Condition cond) 
	{
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.attrList = attrList;
		this.attrList2 = attrList2;
		this.cond = cond;
	}

	
	/**
	 * Constructor with select "*" mark
	 * print two tables all or one tables all
	 * and with condition or not
	 */
	public Select(ArrayList<String> tableNames, Condition cond, boolean selectAll,int aggregateMode){
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.cond = cond;
		this.selectAll = true;
	}

	public Select(ArrayList<String> attrList, ArrayList<String> tableNames,
			 Condition cond, int aggregateMode){
				this.queryName = "SELECT";
				this.setAggregateMode(aggregateMode);
				this.cond = cond;
				this.tableNames = tableNames;
				this.attrList = attrList;
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
	 * getter of columns to be selected
	 */
	public ArrayList<String> getAttrStrList2(){
		return this.attrList2;
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

	
	
	
	
	public void setNormalUser(){
		this.isNormalUser = true;
	}

	
	public boolean isNormalUser(){
		return this.isNormalUser;
	}


	public int getAggregateMode() {
		return aggregateMode;
	}


	public void setAggregateMode(int aggregateMode) {
		this.aggregateMode = aggregateMode;
	}

}
