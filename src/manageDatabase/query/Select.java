package manageDatabase.query;

import java.util.*;

import manageDatabase.expression.Condition;


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
	 * Columns to be select<br>
	 * for all tables
	 */
	private ArrayList<String> attrList;
	
	/**
	 * 
	 */
	private ArrayList<Integer> attrTableRelation;
	
	/**
	 * target table to search<br>
	 * tableNames[0] first<br>
	 * tableNames[1] second<br>
	 */
	private ArrayList<String> tableNames; 
	

	/**
	 * Conditions specified with "WHERE" command
	 */
	private Condition cond;
	
	/**
	 * if this query has "*" command
	 */
	private boolean isSelectAll;
	
	/**
	 * 
	 */
	private boolean isNormalUser = false;

	/**
	 * mode 1 is  Count,  2 is Sum
	 */
	private Aggregation aggregateMode; 
	
	public static enum Aggregation{
		COUNT, SUM, NON
	}
	/**
	 * <pre>
	 * Constructor with select conditions
	 * 2 table 2 attrlist
	 * 1 table 1 attrlist
	 * with condition or not
	 * </pre>
	 * @param attrList
	 * @param attrList2
	 * @param tableNames
	 * @param cond
	 */
	public Select(
			ArrayList<String> attrList,
			ArrayList<Integer> attrTableRelation,
			ArrayList<String> tableNames,
			Condition cond,
			Aggregation aggregateMode) 
	{
		this.queryName = "SELECT";
		this.tableNames = tableNames;
		this.attrList = attrList;
		this.attrTableRelation = attrTableRelation;
		this.cond = cond;
		this.aggregateMode = aggregateMode;
		this.isSelectAll = checkSelectAll();
	}

	
//	
//	/**
//	 * <pre>
//	 * Constructor with select conditions
//	 * 2 table 2 attrlist
//	 * 1 table 1 attrlist
//	 * with condition or not
//	 * </pre>
//	 * @param attrList
//	 * @param attrList2
//	 * @param tableNames
//	 * @param cond
//	 */
//	public Select(
//			ArrayList<String> attrList,
//			ArrayList<Integer> attrTableRelation,
//			ArrayList<String> tableNames,
//			Condition cond) 
//	{
//		this.queryName = "SELECT";
//		this.tableNames = tableNames;
//		this.attrList = attrList;
//		this.attrTableRelation = attrTableRelation;
//		this.cond = cond;
//		this.aggregateMode = Aggregation.NON;
//		this.isSelectAll = checkSelectAll();
//	}
//
//	/**
//	 * COUNT aggregate
//	 * @param attrList
//	 * @param tableNames
//	 * @param cond
//	 * @param aggregateMode
//	 */
//	public Select( 
//			ArrayList<String> attrList,
//			ArrayList<Integer> attrTableRelation,			
//			ArrayList<String> tableNames,
//			Condition cond, 
//			Aggregation aggregateMode)
//	{
//		
//		this.queryName = "SELECT";
//		this.tableNames = tableNames;
//		this.attrList = attrList;
//		this.attrTableRelation = attrTableRelation;
//		//make chang at sql.g4
////		this.attrTableRelation = new ArrayList<Integer>();
////		for(int i=0; i<this.attrList.size(); i++){
////			this.attrTableRelation.add(0);
////		}
//		this.cond = cond;
//		this.aggregateMode = aggregateMode;
//		this.isSelectAll = checkSelectAll();
//	}

	/**
	 * getter of tables to be selected
	 */
	public ArrayList<String> getTableNames(){
		return this.tableNames;
	}

	public int getSelectTableSize(){
		return this.tableNames.size();
	}
	/**
	 * check if Select all tables
	 */
	private boolean checkSelectAll(){
		boolean ans = false;
		int i =0;//# of *
		for(String temp : this.attrList){
			if(temp.equals("*")){
				// ans = true;
				i++;
				// break;
				if (i==tableNames.size()) {
					ans = true;
					break;
				}
			}
		}
		return ans;
	}
	

	/**
	 * getter of select conditions
	 */
	public Condition getCondition(){
		return this.cond;
	}

	
	/**
	 * ??????????????? 
	 */
	public void setNormalUser(){
		this.isNormalUser = true;
	}

	
	/**
	 * ???????????????
	 */
	public boolean isNormalUser(){
		return this.isNormalUser;
	}


	public Aggregation getAggregateMode() {
		return aggregateMode;
	}


	public void setAggregateMode(Aggregation aggregateMode) {
		this.aggregateMode = aggregateMode;
	}


	public ArrayList<Integer> getAttrTableRelation() {
		return attrTableRelation;
	}

	public void setAttrTableRelation(ArrayList<Integer> attrTableRelation) {
		this.attrTableRelation = attrTableRelation;
	}

	public void setSelectAll(boolean isSelectAll) {
		this.isSelectAll = isSelectAll;
	}
	
	public boolean getSelectAll(){
		return this.isSelectAll;
	}

	public ArrayList<String> getAttrList() {
		return this.attrList;
	}

}
