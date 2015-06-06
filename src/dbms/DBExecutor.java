package dbms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import manageDatabase.expression.BinaryExp;
import manageDatabase.expression.ColExp;
import manageDatabase.expression.Condition;
import manageDatabase.expression.Exp;
import manageDatabase.expression.IdExp;
import manageDatabase.expression.IntExp;
import manageDatabase.expression.StrExp;
import manageDatabase.query.Create;
import manageDatabase.query.Insert;
import manageDatabase.query.Query;
import manageDatabase.query.Select;
import structure.Attribute;
import structure.Table;
import structure.Tuple;
import structure.TupleFile;
import structure.TupleStack;
import structure.Value;

/**
 * This class performs the major part of DBMS.<br>
 * 
 * 
 */

public class DBExecutor{
	/**
	 * Database Defined URL:<br>
	 * .......
	 */
	private static final String databaseDefUrl = "databaseDef.dat";
	
	/**
	 * list of all the table currently contained in DBMS
	 */
	private ArrayList<String> tableList;
	
	/**
	 * 
	 */
	Hashtable<String, Table> tables;

	
	/**
	 * used to store tuples, after insert complete store this to file 
	 */
	private ArrayList <TupleFile> tupleFilePool; //each table has one
	
	/**
	 * Constructor which initialize the private member
	 */
	public DBExecutor(){
		// clear databaseDefUrl
		this.tableList = new ArrayList<String>();
		this.tupleFilePool = new ArrayList <TupleFile>();
	}
	
	public static void pause(){
		System.err.println("Press Enter to continue.");
		try{
			System.in.read();
		}catch(Exception e){};
	}
	
	/**
	 *     While an SQL query has be parsed, DBExecutor gets
	 * the query information and execute it.<br>
	 *     The Executor first recognizing the SQL and execute
	 * it by the corresponding class.<br>
	 * 
	 * @param query
	 * : the SQL queries informations.
	 */
	public void execute(Query query)
	{
		long startTime = System.currentTimeMillis();

		try{
			if (query instanceof Create) {
				create((Create)query);
			}
			else if (query instanceof Insert){ 
				insert((Insert)query);
			}
			else if(query instanceof Select){
				System.out.println("Start");
				select((Select) query);
			}
		}
//		catch(NullPointerException ex){
//			System.err.println(ex.getMessage());
//		}
		catch(IOException ex){
			System.err.println(ex.getMessage());
		}
		catch(ClassNotFoundException ex){
			System.err.println(ex.getMessage());
		}
		catch(Error ex){
			System.err.println(ex.getMessage());
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;		
		System.out.println("Completed the execution time of query is "+totalTime);
	}

	/**
	 * The operation to perform SQL : Create<br>
	 * 
	 * @param query
	 * : the object obtains the informations to be created.
	 * @throws IOException
	 * @throws Error
	 * @throws ClassNotFoundException
	 * check if table is already exit in hash table
	 * if not put tableName in hash
	 * and write hash tables in file
	 */
	private void create(Create query)
		throws IOException, Error, ClassNotFoundException 
	{
		// built hashTable by tableName as hash key
//		Hashtable<String, Table> tables = null;
		
		// find table definition data
		File tableFile = new File(databaseDefUrl);
		
		if (tableFile.exists()) {
			tables = this.getTablePool(tableFile);
			
		}else{
			tables = new Hashtable<String, Table>();
		}
		
		// create the table
		if (!tables.containsKey(query.getTableName())) {
			//store table in hash table
			tables.put(query.getTableName(), query.makeTable());
			this.tableList.add(query.getTableName());
			
			//write tables in tablefiles
			this.writeTableDef(tableFile, tables);
			
			//dump message
			System.out.println("Table is created\n---------");
			
		}else{
			//DBMS.outConsole("get "+tables.get(query.getTableName()).getTableName());
			throw new Error("CREATE Table:" + query.getTableName()+ " fail, already exist");
		}
		
	}
	
	

	/**
	 * The operation to perform SQL: INSERT.<br>
	 * get tuple list from file
	 * convert new tuple in string to new tuple in types
	 * check primary whether is notRepeat
	 * if not store new added tuple list to type file
	 * 
	 * @param query : The object contains the informations to be inserted.
	 * @throws IOException
	 * @throws Error
	 * @throws ClassNotFoundException
	 * 
	 */
	public void insert (Insert query)
			throws IOException, Error, ClassNotFoundException
	{
		
		///////////////////////////////////////////
		// get table from tableFile
		///////////////////////////////////////////
		Hashtable <String, Table> tablePool = null;
		tablePool = this.tables;
		//search Table from Memory first
		boolean insertTableInMemory = false;
		String tableName = query.getTableName();
		if(tablePool!=null&&tablePool.containsKey(tableName))
		{
				insertTableInMemory = true;
		}
		//search table from Disk
		if (insertTableInMemory==false|tablePool==null)  
		{
			File tableFile = new File(databaseDefUrl);
			if (tableFile.exists()) {
				//get table from disk 
				//will also get the inserted columns!! because we store 
				//column data in attribute
				tablePool = this.getTablePool(tableFile);
			}else{
				throw new Error("INSERT: No database defined");
			}
		}
		///////////////////////////////////////////
		// Insert into tables
		// 	 using hash structure
		///////////////////////////////////////////

		//tupleStack is used to store tuples in memory
		TupleStack tupleStack = null;
		Table table;
		
		
		if ((table = tablePool.get(query.getTableName()))!= null ) { 
			// check values integrity and input in tuple
			Tuple tuple = this.convertInsertValueType(table, query.getValueList());
			tupleStack = this.getTupleList(query.getTableName());
			
			// check primary key null or notRepeat
			if (tuple != null) {
				boolean checkPrimaryKeyRepeated = 
						this.checkPrimarys(
								table.getPrimaryList(), 
								tupleStack, 
								tuple
						);
				if (!checkPrimaryKeyRepeated) {
					//change
					tupleStack.add(tuple);//will also save in columns but what if
					//no need to save columnList now ... 
					//can do this latter
//					saveColumnList(table, tuple); //... save tuple in column again = =
					//I save column in attribute data structure
				}else{
					throw new Error ("INSERT: primary key is notRepeat or null\n");
				}
			}
			
			//see if we can just save one tuple instead of all tuples
		}else{
			throw new Error ("INSERT: No Table "+ query.getTableName() + " Found\n");
		}
	}
	
	/**
	 * input tuple elements to associated attrList
	 * @param table : target table to store tuple
	 * @param tuple : tuple to be store
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void saveColumnList(
			Table table, 
			ArrayList <Value> tuple
			) throws ClassNotFoundException, IOException
	{
		ArrayList<Attribute> attrList = table.getAttrList();
		
		// for each column of tuple
		for(int i =0;i< tuple.size();++i)
		{
			//not directly called from structure
			//check if it is already exist or still in disk
			//or not even created
			
			ArrayList <Value> columnList =
					getColumn(table, attrList.get(i).getName());
			//getCoumn won't return null
//			if(columnList!=null)
//			{
				columnList.add(tuple.get(i));
//			}
//			else 
//			{
//				columnList = new Tuple(); //columnList is not tuple
//				columnList.add(tuple.get(i));
//			}
		}
	}
	private ArrayList <Integer> getColumnByPos(
			int position, TupleStack tuples)
	{
		ArrayList <Integer> selectColumn = new ArrayList <Integer>();
		for(Tuple tuple: tuples)
			selectColumn.add(tuple.get(position).getInt());
		return selectColumn;

	}
	
	
	
	/**
	 * retrieve columnList
	 * if not found in memory could go to disk and set all column list
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private ArrayList <Value> getColumn(
			Table table,
			String attrName
			) throws ClassNotFoundException, IOException
	{
		ArrayList<Attribute> attrList = table.getAttrList();
		ArrayList <Value> columnList;
		columnList = attrList.get(table.getAttrPos(attrName)).getColumnList();
		
//		if(columnList ==null)
//			columnList = new ArrayList<Value>();
//		
		//no need to do following now since we store 
		//column data in attribute
		//when open table file the column data
		//will also write in memory~
		
		//file I/O
		//transform all tuples into column not just one column we want
		//then next time we can directly take from memory
		TupleStack tupleStack = null;
		if(columnList ==null)
			//have possibility do I/O or get tupleList from memory
			//since it has not been transform into column yet
			tupleStack = getTupleList(table.getTableName()); 
		
		if(columnList == null&& tupleStack.size()>0)
		{
			for(int i =0;i< tupleStack.size();++i)
			{
				ArrayList <Value> tuple = tupleStack.get(i);
				if(tuple!=null)
					for(int j= 0;j< attrList.size();++j)
					{
						columnList = attrList.get(j).getColumnList();
						if(columnList==null)
							columnList = new ArrayList<Value> (); 
						columnList.add(tuple.get(j));
					}
			}
		}
		
//		if can not found in file initialize here
		else columnList = new ArrayList<Value>();
		
		return columnList;
	}
	
	/**
	 * Fetch TupleList by correspond table name.<br>
	 * If it's in memory, we take it, else we look from HardDisk.<br>
	 * ---Call getTupleList() if in HardDisk.<br>
	 * If not in file and not in memory, create a new one.
	 * 
	 * @param tableName : table the fetch data
	 * @return tuple list of the table
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private TupleStack getTupleList(String tableName) 
			throws ClassNotFoundException, IOException
	{
		TupleStack  tupleStackReturn = null;
		Hashtable<String, Table> tablePool = null;
		Table table = null;
		File tableFile = new File(databaseDefUrl);
		
		//Check if database defined
		if(tableFile.exists()){
			tablePool = this.getTablePool(tableFile);
		}else{
			throw new Error("SELECT: No Table Defined");
		}
		
		table = tablePool.get(tableName);
		
		//fetch tupleFile if still store in memroy
		for (TupleFile temp : this.tupleFilePool) {
			if (temp.getTableName().equals(tableName)) {
				tupleStackReturn = temp.getTupleStack();
				break;
			}
		}
		
		//tupleFile not in memory, fetch from HardDisk
		if(tupleStackReturn==null){
			File tupleFile = new File(tableName + ".db");
			if (tupleFile.exists())
			{
				tupleStackReturn = this.getTupleStack(tupleFile);
			}
			else
			{
				//not exist, create new one & put into pool
				tupleStackReturn = new TupleStack(table.getAttrList());
		
				TupleFile newTupleFile = new TupleFile(tableName, tupleStackReturn);
				this.tupleFilePool.add(newTupleFile);
			}
		}// end if tupleListReturn
		return tupleStackReturn;
	}
	
	
	/**
	 * get tuple from tuplefile (tupples)
	 * used for insertion
	 * @param tupleFile
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private TupleStack  getTupleStack(File tupleFile)
			throws IOException, ClassNotFoundException,InvalidClassException
	{
		TupleStack  tupleStack = null;
		FileInputStream fileIn = new FileInputStream(tupleFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
//		ArrayList<Tuple> obj = (ArrayList<Tuple>) in.readObject();
//		tupleStack = (TupleStack) in.readObject();
		tupleStack = (TupleStack) in.readObject();
		in.close();
		fileIn.close();

		return tupleStack;
	}

	/**
	 * save stored all store tupleList to file <br>
	 * each table has a tupleList<br>
	 * if file already has data<br>
	 * it will clear it <br>
	 * to avoid double insertion <br>
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void saveTupleList() throws ClassNotFoundException, IOException
	{
		TupleStack tupleStack= null;
		
		//save tuples in each table to related tupleFile
		for (TupleFile temp : this.tupleFilePool) {
			
			Hashtable<String, Table> tablePool = null;
			Table table = null;
			File tableFile = new File(databaseDefUrl);
			
			//Check if database defined
			if(tableFile.exists()){
				tablePool = this.getTablePool(tableFile);
			}else{
				throw new Error("SELECT: No Table Defined");
			}
			
			File tupleFile = new File(temp.getTableName() + ".db");
			table = tablePool.get(temp.getTableName());
			if (tupleFile.exists()) {
				tupleStack = this.getTupleStack(tupleFile);
				tupleStack.clear(); //see if we can not clear 
			}
			else{
				tupleStack = new TupleStack(table.getAttrList());
			}
			
			//only append temp tuple list in memory
			//so when we have multiple not continuing insert
			// in sql, it will append the old but inserted ones
			// solution add clear()
			
			tupleStack.addAll(temp.getTupleStack());
			
			//store ALL inserted tupples into file
			this.saveTupleList(tupleFile, tupleStack);
			System.out.println("TupleFile store successfully\n---------");
		}		
	}
	
	/**
	 * 
	 * @param tupleFile 
	 * @param tupleStack
	 * @throws IOException
	 */
	private void saveTupleList(File tupleFile, TupleStack tupleStack)throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(tupleFile);
		ObjectOutputStream out = new ObjectOutputStream(outFile);
		
		out.writeObject(tupleStack);
		out.close();
		outFile.close();
	}
	
	/**
	 * Dump each value of this tuple
	 * @param tuple : the tuple to be dump
	 */
	public void printTuple(ArrayList <Value> tuple){
		int i=0;
		for(Value v : tuple){
			switch(v.getType()){
				case INT:
					System.out.print("#"+i+" "+v.getInt()+"\t");
					break;
				case CHAR:
					System.out.print("#"+i+" "+v.getChar()+"\t");
					break;
				case NULL:
					System.out.print("#"+i+" "+"null"+"\t");
					break;
			}
			i++;
		}
	}

	
	/**
	 * Perform the Select action of the query
	 * @param query : the "Select_From" query
	 * @throws IOException
	 * @throws Error
	 * @throws ClassNotFoundException
	 */
	public void select(Select query) throws IOException, Error, ClassNotFoundException{
		
		
		
//		boolean isNormalUser = query.isNormalUser();
//		long startTime = System.currentTimeMillis();

		//////////////////////////////////////////
		// Check if TablePool defined
		//////////////////////////////////////////
		Hashtable<String, Table> tablePool = null;
		ArrayList<String> tableNames = query.getTableNames();
		tablePool = this.tables;
		boolean selectTableInMemory = true;
		if(tablePool!= null)
			for(String tableName : tableNames){
				if(!tablePool.containsKey(tableName)){
					selectTableInMemory = false;
				}
			}
		else selectTableInMemory = false;
					
		File tableFile = new File(databaseDefUrl);
		//get table from file I/O
		if(selectTableInMemory == false && tableFile.exists()){
//		if(tableFile.exists()){
			tablePool = this.getTablePool(tableFile);
		}else if(selectTableInMemory==false && (!tableFile.exists())){
			throw new Error("SELECT: No Table Defined");
		}
//		long endTime1   = System.currentTimeMillis();
//		long totalTime1 = endTime1 - startTime;		
//		System.out.println("get table "+totalTime1);

		//////////////////////////////////////////
		// Hash table and arrayList to save table
		// Hash table to save tuples for each table
		//////////////////////////////////////////
		
		Hashtable<String, Table> tableList = 
				new Hashtable<String, Table>();
		
		ArrayList<Table> tableArrayList = 
				new ArrayList<Table>();
		
		Hashtable<String, TupleStack> tupleHashTable = 
				new Hashtable<String, TupleStack>();
		
		
		//////////////////////////////////////////
		// Check if the TupleFile defined
		//////////////////////////////////////////
		
		//tableNames have type user1.userId bug
		
		for(String tableName : tableNames){
			if(!tablePool.containsKey(tableName)){
				throw new Error("SELECT: No table " + tableName + " Found");
			}else{
				tableList.put(tableName, tablePool.get(tableName));
				System.out.println("add table "+tableName+" okay");
				tableArrayList.add(tablePool.get(tableName));
			}
			
			
			//get tuple file from memory first
			//if not in memory go disk
			//getTypleList function will serve that
			tupleHashTable.put(tableName,this.getTupleList(tableName));
			System.out.println("put tuplelist in select succeed");
		}
		//if get column here it will be like redo insert, very unefficientcy
//		this.getColumn(tableList.get(tableNames.get(0)), tableList.get(tableNames.get(0)).getAttrList().get(0).getName());
		
		////////////////////////////////////////
		// Get conditional attributes if not null
		////////////////////////////////////////
//		System.out.println("processed here for debug");
		Condition selectCond=null;
		if(query.getCondition()!=null)
		{
			selectCond = query.getCondition();
			System.out.println("add condition okay");
		}
		
		ArrayList<String> conditionTableList = null;
		ArrayList<String> conditionAttributeList = null;
		if(selectCond != null){
			//get IdList will setting Idlist and TableList in condition.java
			conditionAttributeList = selectCond.getIdList();
			conditionTableList = selectCond.getTableList();
		}
		//debug
//		if(conditionAttributeList!=null)
//			for(String temp:conditionAttributeList)
//			System.out.print("attr: "+temp);
		//debug
		
		////////////////////////////////////////
		// Get all attributes without duplicates
		////////////////////////////////////////

		ArrayList <Integer> attrTableRelation = query.getAttrTableRelation();
		
		ArrayList<String> targetAttrList = query.getAttrList();

		//used to check if select or conditional attrs in tables
		ArrayList<String> allAttrList =  new ArrayList<String>();
		ArrayList<String> allTableList = new ArrayList<String>();
		
		
		//select all can only distinguish all tables are selected
		//follower codes are used to add select attrs frm
		//tables to one allAttrList
		
		// add attributes into "allAttrList"
		//input target list attr and convert * to attrs

		//			userId will add twice because in diffrent table
			for(int i =0; i<targetAttrList.size();++i)
			{
				String selectAttr = targetAttrList.get(i);
				Integer tableIndex = attrTableRelation.get(i);
				//get what table
				if(tableIndex==-1)
				{
					//find which table for this condition attr
					int findTimes =0;
					if (selectAttr.equals("*")) {
						tableIndex = 0;
						findTimes++;
					}
					else
						for(Table table: tableArrayList)
						{
							int attrPos = table.getAttrPos(selectAttr);
							if(attrPos!=-1)
								{
									tableIndex =tableArrayList.indexOf(table); 
									findTimes++;
								}
						}
					if(findTimes==0 | findTimes>1)
						throw new Error("Select attribute has no responding table"
								+ " or can specify in which tables");
					else attrTableRelation.set(i, tableIndex); //reset the related table
				}
				//debug
//				System.out.println("table Index is " + tableIndex+ " attribute is " +targetAttrList.get(i));
				Table temp = tableArrayList.get(tableIndex);
				//add all attrs in selected table if it's *
				if(selectAttr.equals("*"))
				{
					
					for(Attribute attr : temp.getAttrList())
					{
						//adding specify table
						allTableList.add(temp.getTableName());
						allAttrList.add(attr.getName());
//						System.out.println("add allattrlist of select"+attr.getName());
					}
				}
				else
				{
					allTableList.add(temp.getTableName());
					allAttrList.add(targetAttrList.get(i));
//					System.out.println("add allattrlist of select"+targetAttrList.get(i));
				}
			}
		
		//used for cartesianProduct 
		ArrayList<String> selectAttrList = new ArrayList<String>();
		ArrayList<String> selectTableList = new ArrayList<String>();
					
		selectAttrList.addAll(allAttrList);//used for cartesianProduct 
		selectTableList.addAll(allTableList);//used for cartesianProduct

		
		//for ColExp
		
		////////////////////////////////////////
		// Add condition attributes into
		// After here allAttrList could have element like Table.attr
		//	 "allAttrList" which will be
		//	 used at inner joint function
		////////////////////////////////////////
		
		
		if(conditionAttributeList != null){
			for(int i =0; i<conditionAttributeList.size();++i)
			{
				String inputAttr = conditionAttributeList.get(i);
				String inputTableName =conditionTableList.get(i);
				boolean alreadyContain = false;
				//check if attrs in condition already selected
				for(int j =0;j<allTableList.size();++j)
				{
					if(allTableList.get(j).equals(inputTableName)
					&& allAttrList.get(j).equals(inputAttr))
						{
							alreadyContain = true;
							//for ColExp
							//no need to the other one
							//can be set to TAble.attr
							//then it will be compared IdExp with ColExp
							//still fine
							//allAttrListWithTable
							allAttrList.set(j, inputTableName+"."+inputAttr);
							
						}
				}
				if(alreadyContain ==false){
					//for the need of col condition
					if(conditionTableList.get(i)=="")
					{
						allAttrList.add(inputAttr);						
						//allAttrListWithTable.add(inputAttr);
						//find which table for this condition attr
						int findTimes =0;
						for(String tableName: tableList.keySet())
						{
							int attrPos = tableList.get(tableName).getAttrPos(inputAttr);
							if(attrPos!=-1)
								{
									allTableList.add(tableName);
									findTimes++;
								}
						}
						if(findTimes==0 | findTimes>1)
							throw new Error("Condtion attribute has no responding table"
									+ " or can specify in which tables");
					}
					else
						{
							allTableList.add(inputTableName);
							//for ColExp
							//allAttrListWithTable
							allAttrList.add(inputTableName+"."+inputAttr);
						}
				}
				
			}
			}
		
		////////////////////////////////////////
		// Check if a selected attribute or 
		//	 conditional attribute in the table
		////////////////////////////////////////
		
		//check if attrName exist in respond table
		for(int i =0;i<allAttrList.size();++i){
			String attrName = allAttrList.get(i);
			String tableName = allTableList.get(i);
			boolean containsAttr = false;
			if(attrName.contains(".")==true)
			{
				String[] split = attrName.split("\\.");
				attrName = split[1];
			}
			if(tableList.get(tableName).getAttrPos(attrName) != -1
			  | attrName.equals("*")){	
				containsAttr = true;
			}
			if(containsAttr == false)
			{
				throw new Error("SELECT: Attribute " + attrName +" does not exists");
			}			
		}
//		long endTime2   = System.currentTimeMillis();
//		long totalTime2 = endTime2 - endTime1;		
//		System.out.println("lots of checking "+totalTime2);

		//////////////////////////////////////////
		// Start joining multiple tables to a single 
		//	 table that depends on all attributes 
		//	 needs to be in the new table
		// get cartesianProduct 
		//////////////////////////////////////////
		TupleStack combinedTable;
		if(query.getCondition()==null)
			{
				Condition con = null;
				combinedTable = 
				this.combineTables(
						tableArrayList, 
						tupleHashTable, 
						selectAttrList,
						selectTableList,
						con //would be null
				);
			}
		else{
			combinedTable = 
					this.combineTables(
							tableArrayList, 
							tupleHashTable, 
							allAttrList, //include the select and condition attr
							allTableList,
							query.getCondition()
					);
		}
//		long endTime3   = System.currentTimeMillis();
//		long totalTime4 = endTime3 - endTime2;		
//		System.out.println("combine table cartesian select condition "+totalTime4);

		if(query.getCondition()==null)
		{
//			without condition result is here
//			System.out.println("print table --no condition ~~~~~~~~~~~~~~~~~~~~~~~~~");
			combinedTable = this.getTuplesBySelectedColumns(selectTableList,
															selectAttrList, 
															combinedTable);
			printOut(combinedTable, query);
//			long endTime4   = System.currentTimeMillis();
//			long totalTime5 = endTime4 - endTime3;		
//			System.out.println("selected column no condition "+totalTime5);

			return;
		}
		
		////////////////////////////////////////
		// Evaluate condition
		////////////////////////////////////////
		
//		if(selectCond != null&&combinedTable!=null){
//			combinedTable = getTuplesBySelectedCond(selectCond,
//													combinedTable);
//		}
		
		////////////////////////////////////////
		// Obtain selected values tuples
		////////////////////////////////////////
		TupleStack selectedValuesTable = null;

		selectedValuesTable = this.getTuplesBySelectedColumns(selectTableList,
															selectAttrList, 
															  combinedTable);
		
		
//		printOut(combinedTable,query);
//		long endTime5   = System.currentTimeMillis();
//
//		long totalTime6 = endTime5 - endTime3;		
//		System.out.println("selected column with condition "+totalTime6);

		if(selectedValuesTable!=null)
			printOut(selectedValuesTable,query);
		else System.out.println("the selected result is empty");
	}



	/**
	 * Get table by name from table pool.<br>
	 * 
	 * @param name : table name
	 * @return 
	 *   Table object stored in hashtable.<br>Return null if not found.
	 *   
	 * @throws ClassNotFoundException
	 * @throws IOException
	 **/

	public Table getTableByName(String name) throws ClassNotFoundException, IOException{
		File tableFile = new File(databaseDefUrl);
		Hashtable<String, Table> tablePool;
		if(tableFile.exists()){
			tablePool = this.getTablePool(tableFile);
			return tablePool.get(name);
		}
		else return null;
		
	}
	
	private void printOut(TupleStack combinedTable, Select query)
	{
		if(query.getAggregateMode() == Select.Aggregation.NON)
			printTable(combinedTable);
			
		else if(query.getAggregateMode() == Select.Aggregation.COUNT)
		{
			System.out.println("----------------select "+combinedTable.size()+" tuples");
		}
		else if(query.getAggregateMode() ==Select.Aggregation.SUM)
		{
			int sum = 0;
			if(combinedTable.getSelectattrList().size()==1)
			{
					for(Tuple tuple:combinedTable)
					{
						sum+=tuple.get(0).getInt();	
					}
			}
			System.out.println("----------------Sum is "+ sum);
		}
	}
	
	
	/**
	 * Print the table which finally match the query instruction<br>
	 * @param tupleList 
	 */
	private void printTable(TupleStack tupleList){
		System.out.println("==================Printing table now==================");
		
		ArrayList<String> selectattrs = tupleList.getSelectattrList(); 
		if(tupleList.size()== 0){
			throw new Error("No tuple selected");
		}
		if(selectattrs!=null)
		{
			for(String attrName:selectattrs){
				System.out.printf("%-25s", attrName);
			}			
		}
		System.out.println();

		for(Tuple tuple : tupleList){
			for(Value value : tuple){
				System.out.printf("%-25s", value.toString());
			}
			System.out.println();
		}
		
		System.out.println(tupleList.size() + " tuples selected");
		
	}

	/**
	 * Scale down original tuple by selected columns
	 * @param selectedList
	 * @param tupleStack
	 * @return
	 */
	private TupleStack getTuplesBySelectedColumns(
			ArrayList<String> selectTableList,
			ArrayList<String> selectedList,
			TupleStack tupleStack)
	{
		
		ArrayList<String> oldAttrList = tupleStack.getSelectattrList();
		ArrayList<Integer> saveIndex = new ArrayList<Integer>();
		ArrayList<String> newAttrList = new ArrayList<String>();
//		System.out.println("old attrs"+oldAttrList);
//		System.out.println("new attrs"+selectedList);

		if(oldAttrList.size()==selectedList.size())
		{
			return tupleStack;
		}
		for(int i=0;i< selectedList.size();++i)
		{
			String selectAttr = selectedList.get(i);
			String selectAttrWtable = 
					selectTableList.get(i)+"."+selectedList.get(i);
			if(oldAttrList.contains(selectAttr))
				{
//					System.out.println("save index add"+oldAttrList.indexOf(selectAttr));
					saveIndex.add(oldAttrList.indexOf(selectAttr));
					newAttrList.add(selectAttr);
//					System.out.println("printout debugggg");
					
				}
			else if(oldAttrList.contains(selectAttrWtable))
				{
//				System.out.println("save index add"+oldAttrList.indexOf(selectAttrWtable));
					saveIndex.add(oldAttrList.indexOf(selectAttrWtable));
					newAttrList.add(selectAttrWtable);
//					System.out.println("printout debugggg");
					
				}
			else throw new Error("select Attribute not in condition tuple Stack");
			
		}
		
		// create new TupleStack by selected columns
//		System.out.println("select index are"+ saveIndex);
		TupleStack newTupleList = new TupleStack(newAttrList,true);
		for(Tuple tuple : tupleStack){
			Tuple newOne = new Tuple();
			for(int i=0; i< saveIndex.size();++i)
			{
				// get selected column from old tuple, add to new one
//					tuple.remove(i);
//				System.out.println("tuple in get column"+tuple); 
				newOne.add(tuple.get(saveIndex.get(i)));
			}
			newTupleList.add(newOne);
		}
		return newTupleList;
	}
	
	/**
	 * create new tupleStack base on condition
	 * @param cond
	 * @param tuples
	 * @return
	 */
	private TupleStack getTuplesBySelectedCond(
			Condition cond, 
			TupleStack tuples)
	{
		Hashtable<String, Integer> attrPos = null;
		TupleStack newTupleList = new TupleStack(tuples.getSelectattrList(),true);

		if(newTupleList!=null)
			attrPos = newTupleList.getAttrPosTable();
		Exp exp = cond.getExp();
		Object retBool;
//		if(tuples!=null&& tuples.getAttrPosTable()!=null)//debug used
		for(Tuple tuple : tuples){
//			if(tuple.size()>=attrPos.size()) 
				retBool = exp.accept(this, attrPos, tuple);
//			else retBool = false; 
			if(retBool instanceof Boolean){
				if( ((Boolean) retBool).booleanValue() == true){
					newTupleList.add(tuple);
				}
			}else{
				throw new Error("SELECT: Tuple select condition evaluation failed");
			}
		}
		return newTupleList;
	}

	/**
	 * 
	 * @param tables
	 * @param tupleHashtable
	 * @param allAttributes
	 * @param selectAll
	 * @param isNormalUser
	 * @return
	 */
	private TupleStack combineTables(
			ArrayList<Table> tables, 
			Hashtable<String, TupleStack> tupleHashtable, 
			ArrayList<String> selectAttrList, 
			ArrayList<String> selectTableList,
			Condition condition)

	{
 
		LinkedList<TupleStack> allTables = new LinkedList<TupleStack>();
		ArrayList<String> tableName = new ArrayList<String>();
		for(Table table : tables){
			TupleStack tupleList = tupleHashtable.get(table.getTableName());

			//Get a table that contains all values needed
			TupleStack neededValueTable = null;
			
			//will create new tupleStack
//			long startTime = System.currentTimeMillis();

			neededValueTable = this.getNeededValuesTuples(
					  table,
					  selectTableList,
					  tupleList,
					  selectAttrList,
					  condition);
//			long endTime = System.currentTimeMillis();
//			System.out.println("get need value "+(endTime- startTime));
			if(condition !=null)
			{
			ArrayList<Exp> expList1= new ArrayList<Exp>(); 	
			ArrayList<Exp> expList2= new ArrayList<Exp>(); 			 
			condition.setHashExpList(expList1);
			condition.setRangeExpList(expList2);
			condition.setcompareExpList(condition.getExp());
			expList1 =condition.getHashExpList();
			expList2 = condition.getRangeExpList();
			expList1.addAll(expList2);
			for(Exp exp:expList1) //exp must be binary type here
			{
				Condition con = new Condition(exp);
				Exp left = ((BinaryExp) exp).getLeft();
//				Exp right = ((BinaryExp) exp).getRight();
				//
				if((left instanceof ColExp
					&&((ColExp) left).getTableName().equals(table.getTableName()))
					|
//					(right instanceof ColExp
//					&&((ColExp) left).getTableName().equals(table.getTableName()))
//					|
					(left instanceof IdExp
					&&(table.getAttrPos(((IdExp)left).getId())!=-1)
					)
//					|
//					(right instanceof IdExp
//					&&(table.getAttrPos(((IdExp)right).getId())!=-1)
//					)
					)
				{
					//will create new tupleStack
					neededValueTable = this.getTuplesBySelectedCond(con, neededValueTable);
//					long endTime2 = System.currentTimeMillis();
//					System.out.println("get condition value "+(endTime2- endTime));
				}
			}
			}
			tableName.add(table.getTableName());
			allTables.add(neededValueTable);
		}

		//would be result if no Where clause
		//create new tupleStack
//		return cartesianProduct(allTables,condition);
		
		return cartesianProduct(allTables.get(0),allTables.get(1),condition, tableName);
		
	}

//	/**
//	 * Cartesian Product<br>
//	 * will create new tupleStack by combining two tupleStack<br>
//	 * Join is completed here <br>
//	 * @param allTables
//	 * @return
//	 */
//	private TupleStack cartesianProduct(LinkedList<TupleStack> allTables,Condition condition){
//
//		while(allTables.size() >= 2){
//			TupleStack combinedTable = cartesianProduct(allTables.get(0), allTables.get(1), condition);
//			allTables.removeFirst();
//			allTables.removeFirst();
//			allTables.addFirst(combinedTable);
//		}
//		
//		return allTables.get(0);
//	}

	
	private TupleStack cartesianProduct(TupleStack table1, TupleStack table2,Condition condition,ArrayList<String>tableName){
		long startTime = System.currentTimeMillis();

//		Hashtable<String, Integer> newAttrPos;
		TupleStack tupleList;

//		Hashtable<String, Integer> table1NameTable = table1.getAttrPosTable();
//		Hashtable<String, Integer> table2NameTable = table2.getAttrPosTable();
//
//		newAttrPos = new Hashtable<String, Integer>(table1NameTable);
//		//0 1 2 3 4 5 6+0 6+1 6+2
//		//Update name table position
//		int table1Size = table1NameTable.size();
//		for(String key : table2NameTable.keySet()){
//			 newAttrPos.put(key, table2NameTable.get(key) + table1Size);
//			// newAttrPos.put(key, table2NameTable.get(key));
//		}

		tupleList = new TupleStack(); 

//		tupleList.setAttrPosTable(newAttrPos);

		//set selectattrList
		ArrayList<String> newSelectAttrs = new ArrayList<String>();
		ArrayList<String> table1Attrs = table1.getSelectattrList();
		ArrayList<String> table2Attrs = table2.getSelectattrList();
		newSelectAttrs.addAll(table1Attrs);
		newSelectAttrs.addAll(table2Attrs);
		tupleList.setselectattrList(newSelectAttrs);
//		System.out.println("1join 2"+newSelectAttrs);
//		System.out.println("1join 2"+tupleList.getAttrPosTable());

		ArrayList<String> newSelectAttrs2 = new ArrayList<String>();
		newSelectAttrs2.addAll(table2Attrs);
		newSelectAttrs2.addAll(table1Attrs);
		
		if(condition!=null)
		{
			Exp selectExp = condition.getExp();
			condition.setJoinExp(selectExp);
			Exp joinExp = condition.getJoinExp();
			if(joinExp!= null)
			{
				Exp left = ((BinaryExp)joinExp).getLeft();
				Exp right =  ((BinaryExp)joinExp).getRight();
				String leftAttr= ((ColExp)left).getColomnName();
				String rightAttr = ((ColExp)right).getColomnName();
				int posInTable1;
				int posInTable2;
//				System.out.println(table1Attrs);
//				System.out.println(table2Attrs);
				String tableName1= tableName.get(0);
				String tableName2= tableName.get(1);
				posInTable1 = table1Attrs.indexOf(tableName1+"."+leftAttr);
				if(posInTable1 == -1)
					{
						posInTable1 = table1Attrs.indexOf(tableName1+"."+rightAttr);
						posInTable2 = table2Attrs.indexOf(tableName2+"."+leftAttr);
					}
				else
				posInTable2 = table2Attrs.indexOf(tableName2+"."+rightAttr);
//				System.out.println(leftAttr);
//				System.out.println(rightAttr);
//				
//				System.out.println("pos"+posInTable1);
//				System.out.println("pos"+posInTable2);
				ArrayList <Integer> joinColumn1 = getColumnByPos(posInTable1,table1);
				ArrayList <Integer> joinColumn2 = getColumnByPos(posInTable2,table2);
				Set<Integer> joinColumn1Set = new HashSet<Integer> (joinColumn1);
				HashMap<Integer,Integer> map1 = new HashMap<Integer, Integer>();
				HashMap<Integer,Integer> map2 = new HashMap<Integer, Integer>();
				
				for(int i=0;i<joinColumn1.size();++i)
				{
					map1.put((joinColumn1.get(i)), i);
				}
				for(int i=0;i<joinColumn2.size();++i)
				{
					map2.put((joinColumn2.get(i)), i);
				}
				boolean toBeJoin;//true if table1, else table2
				//if all are distinct value, it should be zero
//				System.out.println("joinColumn1Set.size()"+joinColumn1Set.size()
//						+"joinColumn1Set.size()"+joinColumn1.size());
				if(joinColumn1Set.size() != joinColumn1.size())//1 join 2
					{
						toBeJoin = false;
//						System.out.println("table1 join table2");
					}
				else toBeJoin = true;//table 2 join table 1
//				System.out.println("debug table1"+tableName1);
//				System.out.println("debug table2"+tableName2);
//				System.out.println("debug "+table1Attrs);
//				System.out.println("debug "+table2Attrs);
				if(toBeJoin== true) //2 join 1
				{
					//update select attrs
//					System.out.println("to be join is true change selectlist");

					tupleList.setselectattrList(newSelectAttrs2);
//					System.out.println("2 to 1 "+tableName2+"join "+tableName1+" "+newSelectAttrs2);
//					System.out.println(tupleList.getAttrPosTable());
					//Table2 join Table1
					//search index in table1 that match 
					//to attr in tuple of table2
//					for(Tuple tuple2: table2){
						for(int j=0;j< table2.size();j++){
							Tuple tuple2 = table2.get(j);
							Integer target = joinColumn2.get(j);
//							System.out.println(target.getInt());
							int find = -1;
							if(map1.get(target)!=null)
								find = map1.get(target);
//							System.out.println("find"+find);
							
							if(find!=-1)
							{
								Tuple combinedTuple = new Tuple(tuple2);
								combinedTuple.addAll(table1.get(find));
								tupleList.add(combinedTuple);
//								System.out.println("add tuple"+ combinedTuple);
								
							}
							
							
						
					}//end outer for
						return tupleList;
				}
				else{
//					System.out.println("1 to 2 "+tableName1+"join "+tableName2+" "+newSelectAttrs);

//					for(Tuple tuple1: table1){
					for(int j=0;j< table1.size();j++){
//						ArrayList<Integer> toBeJoinIndex = new ArrayList <Integer>();
						
//						for(Value target:joinColumn1)
//							{
						Tuple tuple1 = table1.get(j);
						Integer target = joinColumn1.get(j);
//								for(int i =0;i<joinColumn2.size();++i)
//								{
//									System.out.println(target.getInt());
									int find = -1;
									if(map2.get(target)!=null)
									find = map2.get(target);
									if(find!=-1)
									{
										Tuple combinedTuple = new Tuple(tuple1);
										combinedTuple.addAll(table2.get(find));
										tupleList.add(combinedTuple);
//										System.out.println("add tuple"+ combinedTuple);

									}

//								}
//							}
						
					}//end outer for

				}
			}
		}	

		
		//Product table1 with table2
		else//has no condition
		for(Tuple tuple1 : table1){
			for(Tuple tuple2 : table2){
				Tuple combinedTuple = new Tuple(tuple1);
				combinedTuple.addAll(tuple2);
				tupleList.add(combinedTuple);
			}
			//////////////////////////////////
			///   join is done right here  ///
			//////////////////////////////////
			//add for inner join
			if(condition!=null)
			{
				Exp selectExp = condition.getExp();
				condition.setJoinExp(selectExp);
				Exp joinExp = condition.getJoinExp();
				if(joinExp!= null)
				{
					Condition join =  new Condition (joinExp);
					//new tuple Stack
					tupleList = this.getTuplesBySelectedCond(join, tupleList);
				}
			}	
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("get cartesian value "+(endTime- startTime));

		return tupleList;
	}

	/**
	 * create new tuple Stack based on select attribute
	 * and condition attribute in one table
	 * also update the new stack's attribute list
	 * would contain table.XX
	 * @param table
	 * @param selectTableList
	 * @param tuples
	 * @param selectAttrList
	 * @return
	 */
	private TupleStack getNeededValuesTuples(
			Table table, 
			ArrayList<String>selectTableList,
			TupleStack tuples, 
			ArrayList<String> selectAttrList,
			Condition condition)
	{

		ArrayList<String> newAttrList = new ArrayList<String>();
		//needed index of attribute for tuple
		//then we know which index in tuple 
		//we want to insert into the new tuple
		ArrayList<Integer> neededAttrPos = new ArrayList<Integer>();
		
		//Save all attributes positions needed
		for(int i=0;i<selectAttrList.size();i++)
		{
			int attrPos;
			String attr = selectAttrList.get(i);
			//for ColExp
			if(attr.contains(".")==true)
			{
				String []parts = attr.split("\\.");
//				System.out.println("this attr is bug" +attr);
				attr = parts[1];
				if(selectTableList.get(i).equals(table.getTableName())
						   &&(attrPos = table.getAttrPos(attr)) != -1
						  )
				{
					newAttrList.add(table.getTableName()+"."+attr);
					neededAttrPos.add(attrPos);
				}
				
			}
			else if(selectTableList.get(i).equals(table.getTableName())
			   &&(attrPos = table.getAttrPos(attr)) != -1
			  )
			{
				newAttrList.add(attr);
				neededAttrPos.add(attrPos);
			}
		}
		
//		Multimap<Integer, Integer> index=ArrayListMultimap.create();
		
//		TreeMultimap<Integer, Integer> index2 = TreeMultimap.create();
	    
		//will set attrlistPos 
		//important to be used in condition checking
		//first time create new tupleStack in select O(n)
		//maybe can do indexing after here
		TupleStack newTupleList = new TupleStack(newAttrList,true);
		
		//Save all needed values in each tuple
//		Value key = null;
//		int tupleIndex=0;
		for(Tuple tuple : tuples){
			Tuple newTuple = new Tuple();
			for(Integer valuePos : neededAttrPos){
				newTuple.add(tuple.get(valuePos));
				//for indexing
//				if(tuple.get(valuePos).getType()==Type.INT);//in this demo only one
//					key = tuple.get(valuePos);
			}
			newTupleList.add(newTuple);
			//for indexing
//			if(key!=null)
//			{
//				index.put(key.getInt(),tupleIndex);
//				index2.put(key.getInt(), tupleIndex);
//				tupleIndex++;
//			}
			
		}
//		
//		if(condition !=null)
//		{
//		ArrayList<Exp> expList1= new ArrayList<Exp>(); 	
//		ArrayList<Exp> expList2= new ArrayList<Exp>(); 			 
//		condition.setHashExpList(expList1);
//		condition.setRangeExpList(expList2);
//		condition.setcompareExpList(condition.getExp());
//		expList1 =condition.getHashExpList();
//		ArrayList<Integer> hashSelectList = null;
//		for(Exp exp:expList1)
//		{
//			Exp left = ((BinaryExp) exp).getLeft();
//			Exp right = ((BinaryExp) exp).getRight();
//			if(	(
//				(left instanceof ColExp
//				&&((ColExp) left).getTableName().equals(table.getTableName()))
//				|
//				(left instanceof IdExp
//				&&(table.getAttrPos(((IdExp)left).getId())!=-1))
//				)
//				&&(right instanceof IntExp)
//			  )
//			{
//				//do hash Index here
//				int hashKey;
//				Collection<Integer> hashSelect= null;
//				if(right instanceof IntExp)
//					{
//					hashKey = ((IntExp) right).getInt();
//					hashSelect = index.get(hashKey);
//					}
//				hashSelectList= new ArrayList<Integer>(hashSelect);
//			}
//		}
//		if(hashSelectList!=null)
//		for(int i=0;i<hashSelectList.size();++i){
//			Tuple newTuple = new Tuple();
//			Tuple oldTuple = tuples.get(hashSelectList.get(i));
//			for(Integer valuePos : neededAttrPos){
//				newTuple.add(oldTuple.get(valuePos));
//				//for indexing
////				if(tuple.get(valuePos).getType()==Type.INT);//in this demo only one
////					key = tuple.get(valuePos);
//			}
//			newTupleList.add(newTuple);
//			//for indexin
//		}
//		
//		expList2 = condition.getRangeExpList();
//		for(Exp exp:expList2)
//		{
//			Exp left = ((BinaryExp) exp).getLeft();
//			Exp right = ((BinaryExp) exp).getRight();
//			if(
//				(
//				(left instanceof ColExp
//				&&((ColExp) left).getTableName().equals(table.getTableName()))
//				|
//				(left instanceof IdExp
//				&&(table.getAttrPos(((IdExp)left).getId())!=-1))
//				)
//				&&(right instanceof IntExp)
//				
//			 ){
//				//do range select here
//				if((((BinaryExp)exp).getOp()).equals(">"))
//				{
//					
//				}
//				else if((((BinaryExp)exp).getOp()).equals(">="))
//				{
//					
//				}
//				else if((((BinaryExp)exp).getOp()).equals("<"))
//				{
//					
//				}
//				else if((((BinaryExp)exp).getOp()).equals("<="))
//				{
//					
////					SortedMultiMap<Integer,Integer> nav=
////					index2.headMap(((IntExp) right).getInt(),true);
//				}
//				else if((((BinaryExp)exp).getOp()).equals("<>"))
//				{
//					
//				}
//			}//end if
//		}//end for
//		}
		return newTupleList;
	}

	
	/**
	 *  Check primary key value repeat, using equals()
	 * @param primaryList : primary attribute position table
	 * @param tupleList : tuple list of a table
	 * @param tuple : the tuple(valueList) to check if primary notRepeat in old
	 * @return 
	 *  true if not not Repeat, false if notRepeat
	 * @see Value
	 */
	private boolean checkPrimarys(
			ArrayList <Integer> primaryList, 
			TupleStack tupleList, 
			ArrayList <Value> tuple)
	{
		boolean notRepeat = false;
		
		//check every tuple
		LABEL_OUTTER:
		for (ArrayList <Value> tupleIterator: tupleList ) 
		{
			// in each tuple check primary key in new valueList whether is notRepeat  
			for (int primaryPosition : primaryList) 
			{
				// true if equals
				if (tuple.get(primaryPosition).equals(tupleIterator.get(primaryPosition))) 
				{
					this.printTuple(tuple);
					notRepeat = true;
					
					break LABEL_OUTTER;
				}
			}
		}
		return notRepeat;

	}
	
	/**
	 * Convert values from string to respect data type<br>
	 * <br>
	 * Check insert value list's columns numbers whether the same as tables<br>
	 * convert strings in values to different types of object in valueList<br>
	 * based on  attribute list <br>
	 * 
	 * @param tableDef : table from the table file
	 * @param values : valueList from the query
	 */
	private Tuple convertInsertValueType(Table table, ArrayList <String> values) throws Error
	{
		  Tuple tuple = new Tuple ();
		  ArrayList <Attribute> attrList = table.getAttrList();
		  String tableName = table.getTableName();
		  int attrSize = attrList.size();
		  
		  // check table's column amount with query's value amount
		  if (attrSize != table.getAttrList().size()) {
		  		throw new Error("INSERT: The number of Values is not matched, Table: "
		  				+ tableName + " has " +attrSize + " Values");
		  }

		  //iterate attribute list, convert values and input valueList
		  for (int i = 0; i < attrSize; ++i) {
		  	Attribute attribute = attrList.get(i);	//
		  	String strValue = values.get(i);

		  	try{
		  		Attribute.Type type = attribute.getType();
		  		
		  		if (type == Attribute.Type.INT) {
		  			Value value = new Value( Integer.parseInt(strValue) );
		  			tuple.add(value);
		  		}
		  		else if(type == Attribute.Type.CHAR){
		  			//check type and length 
		  			if (attribute.getLength() < strValue.length()) {
		  				throw new Error("INSERT: Value " + strValue + "length: "+attribute.getLength()+"<->"+strValue.length()+" mismatch");
		  			}
		  			Value charValue = new Value(strValue);
		  			tuple.add(charValue);
		  		}
		  	}
		  	catch(NumberFormatException ex){
		  		throw new Error("INSERT: Value " + strValue + " is wrong type or exceed length\n");
		  	}

		  }
		  return tuple;
	}
	
	/**
	 * used for creation<br>
	 * read in table from file
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Hashtable <String, Table> getTablePool(File tableFile)
			throws IOException, ClassNotFoundException
	{
		Hashtable <String, Table> tablePool = null;
		FileInputStream fileIn = new FileInputStream(tableFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		tablePool = (Hashtable<String, Table>) in.readObject();
		in.close();
		fileIn.close();

		return tablePool;
	}
	
	/**
	 * Write table object into binary file(.dat)<br>
	 * 
	 * @param tableFile : file
	 * @param tables : hash table of tables
	 * @throws IOException
	 */
	private void writeTableDef(File tableFile, Hashtable<String, Table>tables)throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(tableFile);
		ObjectOutputStream out = new ObjectOutputStream(outFile);
		out.writeObject(tables);
		out.close();
		outFile.close();
	}

	

	public ArrayList<String> getTableList() {
		return tableList;
	}

	public class AppendingObjectOutputStream extends ObjectOutputStream {

		  public AppendingObjectOutputStream(OutputStream out) throws IOException {
		    super(out);
		  }

		  @Override
		  protected void writeStreamHeader() throws IOException {
		    // do not write a header, but reset:
		    // this line added after another question
		    // showed a problem with the original
		    reset();
		  }

		}
	

	/**
	 * 
	 * called by exp.java
	 * then call exps of each type to accept 
	 * @param exp
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	public Object visit(
			Exp exp,  
			Hashtable<String, Integer> attrPosTable, 
			Tuple tuple)
	{
	
		if(exp instanceof BinaryExp){
			//System.err.println("Enter into visit binary");
			return ((BinaryExp) exp).accept(this, null, attrPosTable, tuple);
		}else if(exp instanceof StrExp){
			//System.err.println("Enter into visit str");
			return ((StrExp) exp).accept(this, null);
		}else if(exp instanceof IdExp){
			return ((IdExp) exp).accept(this, attrPosTable, tuple);
		}else if(exp instanceof ColExp){
			return ((ColExp) exp).accept(this, attrPosTable, tuple);
		}
		else if(exp instanceof IntExp){
			//System.err.println("Enter into visit int");
			return ((IntExp) exp).accept(this, null);
		}else{
			return Boolean.valueOf(true);
		}
	
	}
	
	/**
	 * called by Exp exp
	 * @param exp
	 * @param value
	 * @return
	 */
	public Object visit(
			Exp exp, 
			Value value)
	{
		if(exp instanceof BinaryExp){
			//System.err.println("Enter into visit binary");
			return ((BinaryExp) exp).accept(this, value, null, null);
		}else if(exp instanceof StrExp){
			//System.err.println("Enter into visit str");
			return ((StrExp) exp).accept(this, value);
		}else if(exp instanceof IdExp){
			return ((IdExp) exp).accept(this, value);
		}else if(exp instanceof ColExp){
			return ((ColExp) exp).accept(this, value);
		}else if(exp instanceof IntExp){
			//System.err.println("Enter into visit int");
			return ((IntExp) exp).accept(this, value);
		}else{
			return Boolean.valueOf(true);
		}
	}
	
	
	/**
	 * called by intExp accept
	 * @param exp
	 * @param value
	 * @return
	 */
	public Object visit(IntExp exp, Value value){
		//System.err.println("Enter into intExp ");
		return Integer.valueOf(exp.getInt());
	}
	
	/**
	 * called by strExp accept
	 * @param exp
	 * @param value
	 * @return
	 */
	public Object visit(StrExp exp, Value value){
		//System.err.println("Enter into StrExp ");
		return exp.getStr();
	}
	
	/**
	 * called by IdExp accept
	 * @param exp
	 * @param value
	 * @return
	 */
	public Object visit(IdExp exp, Value value){
		//System.err.println("Enter into idExp");
		if(value.getType() == Attribute.Type.INT){
			return Integer.valueOf(value.getInt());
		}
		else if(value.getType() == Attribute.Type.CHAR){
			return value.getChar();
		}else{
			throw new Error("IdExp error");
		}
	}
	/**
	 * also called by IdExp
	 * @param exp
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	public Object visit(IdExp exp, Hashtable<String, Integer> attrPosTable, Tuple tuple){
		String attrName = exp.getId();
		Value value;
		value = tuple.get(attrPosTable.get(attrName).intValue());
		return visit(exp, value);
	}
	
	/**
	 * called by ColExp accept
	 * @param exp
	 * @param value
	 * @return
	 */
	public Object visit(ColExp exp, Value value){
		//System.err.println("Enter into idExp");
		if(value.getType() == Attribute.Type.INT){
			return Integer.valueOf(value.getInt());
		}
		else if(value.getType() == Attribute.Type.CHAR){
			return value.getChar();
		}else{
			throw new Error("ColExp error");
		}
	}
	/**
	 * also called by ColExp
	 * @param exp
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	public Object visit(ColExp exp, Hashtable<String, Integer> attrPosTable, Tuple tuple){
		String attrName = exp.getColomnName();
		Value value;
		 // value = tuple.get(attrPosTable.get(attrName).intValue());
		value = tuple.get(attrPosTable.get(exp.getTableName()+"."+attrName).intValue());
		if(value ==null)
			value = tuple.get(attrPosTable.get(attrName).intValue());
		return visit(exp, value);
	}
	
	
	/**
	 * called by binaryExp's accept
	 * 
	 * @param bExp : binary expression
	 * @param value : 
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	@SuppressWarnings("unused")
	public Object visit(
			BinaryExp bExp, 
			Value value, 
			Hashtable<String, Integer> attrPosTable, 
			Tuple tuple)
	{
		//System.err.println("Enter into BinaryExp ");//
		String op = bExp.getOp();
		Object ret = null;
		
		
		//this is unused!!
		if(bExp == null){
			return Boolean.valueOf(true);
		}
	
		Exp left = bExp.getLeft();
		Exp right = bExp.getRight();
	
		Object leftOp = null;
		Object rightOp = null;
		
		if(left != null){
			//System.err.println("Left not null " + op);//
			if(tuple == null){
				leftOp = left.accept(this, value);
			}else{
				leftOp = left.accept(this, attrPosTable, tuple);
			}
			//System.err.println("Left visited ");//
		}
	
		if(right != null){
			//System.err.println("Right not null " + op);//
			if(tuple == null){
				rightOp = right.accept(this, value);
			}else{
				rightOp = right.accept(this, attrPosTable, tuple);
			}
		}
	
		//
		if( ((leftOp instanceof Integer) || (leftOp instanceof Double)) 
				&& ((rightOp instanceof Integer) || (rightOp instanceof Double)))
		{
			
			 double l, r;
			if(leftOp instanceof Integer){
				l = (double)((Integer) leftOp).intValue();
			}else{
				 l = ((Double) leftOp).doubleValue();
			}
			
			if(rightOp instanceof Integer){
				r = (double)((Integer) rightOp).intValue();
			}else{
				 r = ((Double) rightOp).doubleValue();
			}
	
			 if (op.equals("<")) {
	            		ret = (l < r);
	       		 } else if (op.equals("<=")) {
	            		ret = (l <= r);
	        		} else if (op.equals("=")) {
	        			ret = (l == r);
	        		}else if (op.equals("<>")) {
	            		ret = (l != r);
	        		} else if (op.equals(">")) {
	            		ret = (l > r);
	        		} else if (op.equals(">=")) {
	            		ret = (l >= r);
	        		} else if (op.equals("+")) {
	            		ret = (l + r);
	        		} else if (op.equals("-")) {
	            		ret = (l - r);
	        		} else if (op.equals("*")) {
	            		ret = (l * r);
	        		} else if (op.equals("/")) {
	            		ret = (l / r);
	        		}else{
	            		throw new Error("Implement BinaryExp for " + op);
			}
	
	    		return ret;
		}
	
		
		if((leftOp instanceof String) && (rightOp instanceof String)){
			if(op.equals("=")){
				if( ( (String)leftOp).equals((String) rightOp)){
					return Boolean.valueOf(true);
				}else{
					return Boolean.valueOf(false);
				}
			}else if (op.equals("!=")){
				if( ((String)leftOp).equals((String) rightOp)){
					return Boolean.valueOf(false);
				}else{
					return Boolean.valueOf(true);
				}
			}else{
				throw new Error("Condition error: String can only compare with \"=\" or \"!=\" operator"); 
			}
		}
	
		if( (leftOp instanceof Boolean) && (rightOp instanceof Boolean)){
			Boolean boolRet = false;
			if(op.toUpperCase().equals("AND")){
				boolRet = ((Boolean)leftOp).booleanValue() && ((Boolean)rightOp).booleanValue();
			}
	
			if(op.toUpperCase().equals("OR")){
				boolRet = ((Boolean)leftOp).booleanValue() || ((Boolean)rightOp).booleanValue();
			}
			return boolRet;
		}
	
		return leftOp;
		
	}
	
	
	
	public void cleanUp(){
		File tableFile = new File(databaseDefUrl);
		
		if (tableFile.exists()) {
			
			for(String tableName : this.tableList){
				File tupleFile = new File(tableName + ".db");
				if (tupleFile.exists()) {
					tupleFile.delete();
				}
			}
			
			tableFile.delete();
		}
	}
	
}//end DBExecutor

