package dbms;

import java.io.*;
import java.util.*;

import manageDatabase.expression.*;
import manageDatabase.query.*;
import structure.*;

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
				System.out.println("Completed");
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
		Hashtable <String, Table> tables = null;
		//tupleStack is used to store tuples in memory
		TupleStack tupleStack = null;
		Table table;
		
		///////////////////////////////////////////
		// get table from tableFile
		///////////////////////////////////////////
		tables = this.tables;
		if (tables==null)  
		{
			File tableFile = new File(databaseDefUrl);
			if (tableFile.exists()) {
				//get table from disk 
				//will also get the inserted columns!! because we store 
				//column data in attribute
				tables = this.getTablePool(tableFile);
			}else{
				throw new Error("INSERT: No database defined");
			}
		}
		///////////////////////////////////////////
		// Insert into tables
		// 	 using hash structure
		///////////////////////////////////////////
		
		if ((table = tables.get(query.getTableName()))!= null ) { 
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
					
					saveColumnList(table, tuple); //... save tuple in column again = =
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
			
			ArrayList <Value> columnList = getColumn(table, attrList.get(i).getName());
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
		
//		boolean isNormalUser = query.isNormalUser();
		
		//////////////////////////////////////////
		// Check if TablePool defined
		//////////////////////////////////////////
					
		File tableFile = new File(databaseDefUrl);
		//get table from file I/O
		if(selectTableInMemory == false && tableFile.exists()){
			tablePool = this.getTablePool(tableFile);
		}else{
			throw new Error("SELECT: No Table Defined");
		}

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
//			File tupleFile = new File(tableName + ".db");
//			if(!tupleFile.exists()){
//				throw new Error("SELECT: No data in the table: " + tableName); 
//			}
			
//			else{
//				tupleHashTable.put(tableName, this.getTupleStack(tupleFile));
//			}
		}
//		this.getColumn(tableList.get(tableNames.get(0)), tableList.get(tableNames.get(0)).getAttrList().get(0).getName());
		
		////////////////////////////////////////
		// Get conditional attributes if not null
		////////////////////////////////////////
		System.out.println("processed here for debug");
		Condition selectCond=null;
		if(query.getCondition()!=null)
			selectCond = query.getCondition();
		System.out.println("add condition okay");

		ArrayList<String> conditionAttributeList = null;
		if(selectCond != null){
			conditionAttributeList = selectCond.getIdList();
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
		ArrayList<String> allAttrList = null;
		//used for cartesianProduct 
		ArrayList<String> selectAttrList = new ArrayList<String>();
		
		//select all can only distinguish all tables are selected
		//follower codes are used to add select attrs frm
		//tables to one allAttrList
		
		// add attributes into "allAttrList"
//		if(query.getSelectAll()==false){
			// If not select all, take out target attribute list
			allAttrList = new ArrayList<String>();
			
			//input target list attr and convert * to attrs
			
			for(int i =0; i<targetAttrList.size();++i)
			{
				//add all attrs in selected table if it's *
				if(targetAttrList.get(i).equals("*"))
				{
					//get what table
					Integer tableIndex = attrTableRelation.get(i);
					Table temp = tableArrayList.get(tableIndex);
					for(Attribute attr : temp.getAttrList())
					{
						allAttrList.add(attr.getName());
						System.out.println("add allattrlist "+attr.getName());
					}
				}
				else
				{
					allAttrList.add(targetAttrList.get(i));
					System.out.println("add allattrlist "+targetAttrList.get(i));
				}
			}
			
//		}else{
//			// If select all attributes
//			allAttrList = new ArrayList<String>();
//				for(String tableName : tableList.keySet()){
//					Table table = tableList.get(tableName);
//					for(Attribute attr : table.getAttrList())
//					{
//						allAttrList.add(attr.getName());
//					}
//				}
//		}// end if selectAll
		selectAttrList.addAll(allAttrList);//used for cartesianProduct 
			
		////////////////////////////////////////
		// Add condition attributes into 
		//	 "allAttrList" which will be
		//	 used at inner joint function
		////////////////////////////////////////
		
		if(conditionAttributeList != null){
			for(String attr : conditionAttributeList){
				if(!allAttrList.contains(attr)){
					allAttrList.add(attr);
				}
			}
		}
		
		////////////////////////////////////////
		// Check if a selected attribute or 
		//	 conditional attribute in the table
		////////////////////////////////////////
		
		
		for(String attrName : allAttrList){
			boolean containsAttr = false;
			for(String tableName : tableList.keySet()){
//				Hashtable<String, Integer> attrPosTable = tableList.get(tableName).getAttrPosHashtable();
//				for(String attr:attrPosTable.keySet())
//				{
//					System.out.println("debug used attr are"+attr);
//				}
//				for(Integer i: attrPosTable.values())
//				{
//					System.out.println("debug used integer are  "+i);
//				}
				if(tableList.get(tableName).getAttrPos(attrName) != -1|attrName.equals("*")){	
					containsAttr = true;
				}
			}
			if(containsAttr == false)
			{
				throw new Error("SELECT: Attribute " + attrName +" does not exists");
			}			
		}
		
		//////////////////////////////////////////
		// Start joining multiple tables to a single 
		//	 table that depends on all attributes 
		//	 needs to be in the new table
		// get cartesianProduct 
		//////////////////////////////////////////
		TupleStack combinedTable = 
				this.combineTables(
						tableArrayList, 
						tupleHashTable, 
						selectAttrList, 
						query.getSelectAll()
				);
		
		if(query.getCondition()==null)
		{
			System.out.println("print combined table now~~~~~~~~~~~~~~~~~~~~~~~~~");
			printTable(combinedTable);
		}
			
		////////////////////////////////////////
		// Evaluate condition
		////////////////////////////////////////
		
		if(selectCond != null&&combinedTable!=null){
			combinedTable = getTuplesBySelectedCond(selectCond, combinedTable);
		}
		
		////////////////////////////////////////
		// Obtain selected values tuples
		////////////////////////////////////////
		TupleStack selectedValuesTable = null;

		if(!query.getSelectAll()){
			selectedValuesTable = combinedTable;
//			selectedValuesTable = this.getTuplesBySelectedColumns(targetAttrList, combinedTable);	
		}else{
			selectedValuesTable = combinedTable;
		}
		if(selectedValuesTable!=null)
		printTable(selectedValuesTable);
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

	/**
	 * Print the table which finally match the query instruction<br>
	 * @param tupleList 
	 */
	private void printTable(TupleStack tupleList){
		System.out.println("Printing table now====================");
		
		Hashtable<String, Integer> attrPosTable = tupleList.getAttrPosTable();
		ArrayList<String> selectattrs = tupleList.getSelectattrList(); 
		if(tupleList.size()== 0){
			throw new Error("No tuple selected");
		}
		
		if(attrPosTable!=null)
		{
//			String[] orderedAttrNames = new String[attrPosTable.size()];
//	
//			//Get ordered attribute names
//			for(String attrName : attrPosTable.keySet()){
//				
//				if(attrPosTable.get(attrName).intValue()<attrPosTable.size())//debug found out of bound
//					orderedAttrNames[attrPosTable.get(attrName).intValue()] = attrName;
//			}
//		
//			//Print attribute names
//			for(int i = 0; i < orderedAttrNames.length; i++){
//				System.out.printf("%-20s", orderedAttrNames[i]);
//			}
			
			for(String attrName:selectattrs){
				System.out.printf("%-20s", attrName);
			}
			
			
		}
		System.out.println();

		for(Tuple tuple : tupleList){
			for(Value value : tuple){
				System.out.printf("%-20s", value.toString());
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
			ArrayList<String> selectedList,
			TupleStack tupleStack)
	{
		ArrayList<Attribute> attrList = tupleStack.getAttrList();
		ArrayList<Attribute> newAttrList = new ArrayList<Attribute>();
		Hashtable<String, Integer> attrPos = tupleStack.getAttrPosTable();
		
		// fetch select column name
		for(String selectedColumn : selectedList){
			newAttrList.add(attrList.get(attrPos.get(selectedColumn)));
		}

		// create new TupleStack by selected columns
		TupleStack newTupleStack = new TupleStack(newAttrList);
		for(Tuple tuple : tupleStack){
			Tuple newTuple = new Tuple();
			for(String selectedValue : selectedList)
			{
				// get selected column from old tuple, add to new one
				newTuple.add(tuple.get( attrPos.get(selectedValue).intValue() ) );
			}
			newTupleStack.add(newTuple);
		}

		return newTupleStack;

	}
	
	/**
	 * 
	 * @param cond
	 * @param tuples
	 * @return
	 */
	private TupleStack getTuplesBySelectedCond(
			Condition cond, 
			TupleStack tuples)
	{
		Hashtable<String, Integer> attrPos = null;
		if(tuples!=null&&tuples.getAttrPosTable()!=null)///////////////////////////////bug here
			attrPos = tuples.getAttrPosTable();
		TupleStack newTupleList = new TupleStack();
		
		Exp exp = cond.getExp();
		Object retBool;
//		if(tuples!=null&& tuples.getAttrPosTable()!=null)//debug used
		for(Tuple tuple : tuples){
			retBool = exp.accept(this, attrPos, tuple);
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
			boolean selectAll)

	{

		//TupleStack combinedTupleList = new TupleStack();
		//Hashtable<String, Integer> combinedAttrNameList = new Hashtable<String, Integer>();		
 
		LinkedList<TupleStack> allTables = new LinkedList<TupleStack>();
//		ArrayList<Attribute> attrList;
		
		for(Table table : tables){
			TupleStack tupleList = tupleHashtable.get(table.getTableName());
//			attrList = table.getAttrList();

			//Get a table that contains all values needed
			TupleStack neededValueTable = null;
			
			if(!selectAll){
				 neededValueTable = this.getNeededValuesTuples(table, tupleList, selectAttrList);
			}else{
				 neededValueTable = this.getNeededValuesTuples(table, tupleList, selectAttrList);

			}
			allTables.add(neededValueTable);
		}
		//would be result if no Where clause
		return cartesianProduct(allTables);
	}

	/**
	 * Cartesian Product<br>
	 * 
	 * @param allTables
	 * @return
	 */
	private TupleStack cartesianProduct(LinkedList<TupleStack> allTables){
		//LinkedList<TuplesWithNameTable> cloneAllTables = new LinkedList<TuplesWithNameTable>(allTables);

		while(allTables.size() >= 2){
			TupleStack combinedTable = cartesianProduct(allTables.get(0), allTables.get(1));
			allTables.removeFirst();
			allTables.removeFirst();
			allTables.addFirst(combinedTable);
		}
		
		return allTables.get(0);
	}

	
	private TupleStack cartesianProduct(TupleStack table1, TupleStack table2){
		Hashtable<String, Integer> newAttrPos;
		TupleStack tupleList;

		newAttrPos = new Hashtable<String, Integer>(table1.getAttrPosTable());
		tupleList = new TupleStack();

		int table1Size = newAttrPos.size();
		Hashtable<String, Integer> table2NameTable = table2.getAttrPosTable();

		//Update name table position
		for(String key : table2NameTable.keySet()){
			newAttrPos.put(key, table2NameTable.get(key) + table1Size);
		}
		tupleList.setAttrPosTable(newAttrPos);
		//Product table1 with table2
		for(Tuple tuple1 : table1){
			for(Tuple tuple2 : table2){
				Tuple combinedTuple = new Tuple(tuple1);
				combinedTuple.addAll(tuple2);
				tupleList.add(combinedTuple);
			}
		}

		return tupleList;
	}

	/**
	 * 
	 * @param table
	 * @param tuples
	 * @param allAttributes
	 * @return
	 */
	private TupleStack getNeededValuesTuples(
			Table table, 
			TupleStack tuples, 
			ArrayList<String> selectAttrList)
	{

		ArrayList<String> newAttrList = new ArrayList<String>();
		//needed index of attribute for tuple
		//then we know which index in tuple 
		//we want to insert into the new tuple
		ArrayList<Integer> neededAttrPos = new ArrayList<Integer>();

		//Save all attributes positions needed
		int attrPos;
		for(String attr : selectAttrList){
			if( (attrPos = table.getAttrPos(attr)) != -1){
				newAttrList.add(attr);
				neededAttrPos.add(attrPos);
			}
		}

		//will set attrlistPos
		TupleStack newTupleList = new TupleStack(newAttrList,true);
		//Save all needed values in each tuple
		for(Tuple tuple : tuples){
			Tuple newTuple = new Tuple();
			for(Integer valuePos : neededAttrPos){
				newTuple.add(tuple.get(valuePos));
			}
			newTupleList.add(newTuple);
		}

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
		}else if(exp instanceof IntExp){
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
//		if(!attrName.equals("*"))
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
	        		} else if (op.equals("!=")) {
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

