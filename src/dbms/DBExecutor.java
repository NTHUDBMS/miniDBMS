package dbms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import manageDatabase.expression.*;
import manageDatabase.query.*;
import structure.Attribute;
import structure.Table;
import structure.TupleFileTemp;
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
	
	public DBExecutor(){
		// clear databaseDefUrl
		this.tableList = new ArrayList<String>();
		this.tupleFileTemp = new ArrayList <TupleFileTemp>();
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
				select((Select) query);
			}
		}catch(IOException ex){
			System.err.println(ex.getMessage());
		}catch(ClassNotFoundException ex){
			System.err.println(ex.getMessage());
		}catch(Error ex){
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
		Hashtable<String, Table> tables = null;
		
		// find table definition data
		File tableFile = new File(databaseDefUrl);
		
		if (tableFile.exists()) {
			tables = this.getTableDef();
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
	 * used to store tuples, after insert complete store this to file 
	 */
	private ArrayList <TupleFileTemp> tupleFileTemp; //each table has one
	

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
		ArrayList <ArrayList <Value>> tupleListTemp = null;
		Table table;
		
		///////////////////////////////////
		// get table from tableFile
		///////////////////////////////////
		File tableFile = new File(databaseDefUrl);
		if (tableFile.exists()) {
			tables = this.getTableDef();
		}else{
			throw new Error("INSERT: No database defined");
		}
		
		///////////////////////////////////
		// Insert into tables
		// 	 using hash structure
		///////////////////////////////////
		if ((table = tables.get(query.getTableName()))!= null ) { 
			// check values integrity and input in tuple
			ArrayList <Value> tuple = this.convertInsertValueType(table, query.getValueList());
			tupleListTemp = this.getTupleListTemp(query.getTableName());
			
			// check primary key null or notRepeat
			if (tuple != null) {
				boolean checkPrimaryKeyRepeated = this.checkPrimarys(table.getPrimaryList(), tupleListTemp, tuple);
				if (!checkPrimaryKeyRepeated) {
					//change
					tupleListTemp.add(tuple);
					saveColumnListTemp(table, tuple);
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
	 * @param table
	 * @param tuple
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void saveColumnListTemp(Table table, ArrayList <Value> tuple) throws ClassNotFoundException, IOException
	{
		ArrayList<Attribute> attrList = table.getAttrList();
		
		for(int i =0;i< tuple.size();++i)
		{
			//not directly called from structure
			//check if it is already exist or still in disk
			//or not even created
			
			ArrayList <Value> columnList = getColumListTemp(table, attrList.get(i).getName());
			if(columnList!=null)
				columnList.add(tuple.get(i));
			else 
				{
					columnList = new ArrayList<Value>();
					columnList.add(tuple.get(i));
				}
		}
	}
	
	/**
	 * retrieve columnList
	 * if not found in memory could go to disk and set all column list
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private ArrayList <Value> getColumListTemp(Table table, String attrName) throws ClassNotFoundException, IOException
	{
		ArrayList<Attribute> attrList = table.getAttrList();
		ArrayList <Value> columnList;
		columnList = attrList.get(table.getAttrPos(attrName)).getColumnList();
		ArrayList<ArrayList <Value>> tupleList = getTupleListTemp(table.getTableName());

		//file I/O
		//transform all tuples into column not just one column we want
		//then next time we can directly take from memory
		if(columnList == null&& tupleList.size()>0)
		{
			for(int i =0;i< tupleList.size();++i)
			{
				ArrayList <Value> tuple = tupleList.get(i);
				if(tuple!=null)
					for(int j= 0;j< attrList.size();++j)
					{
						columnList = attrList.get(j).getColumnList();
						if(columnList==null)
							columnList = new ArrayList<Value> (); 
						columnList.add(tuple.get(j));
					}
			}
			columnList = attrList.get(table.getAttrPos(attrName)).getColumnList();
		}
		//if can not found in file initialize here
		else columnList = new ArrayList<Value>(); //won't pass this weird
		return columnList;
	}
	/**
	 * used to find tuplelist <br>
	 * if it is in memory, we take it,<br>
	 * else we look from HardDisk <br>
	 * it will call getTupleList if it doesn't find tuplelist in memory<br>
	 * if not in file and not in memory
	 * create a new one
	 * @param tableName
	 * @return tupleList
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	
	private ArrayList <ArrayList <Value>>  getTupleListTemp(String tableName) throws ClassNotFoundException, IOException{
		ArrayList <ArrayList <Value>>  tupleListTemp = null;
		for (TupleFileTemp temp:this.tupleFileTemp) {
			if (temp.getTableName().equals(tableName)) {
				tupleListTemp = temp.getTupleList();
			}
		}
		//can get from memory, look up from Disk
		File tupleFile = new File(tableName + ".db");
		if (tupleFile.exists()&&tupleListTemp==null) {
			tupleListTemp = this.getTupleList(tupleFile);
		}
		//all new 
		else if(!tupleFile.exists()&&tupleListTemp == null)
		{
			ArrayList <ArrayList <Value>> tupleListTemp1 = new ArrayList <ArrayList <Value>>();
			TupleFileTemp newTupleFile = new TupleFileTemp(tableName, tupleListTemp1);
			this.tupleFileTemp.add(newTupleFile);
			tupleListTemp = tupleListTemp1;
		}
		return tupleListTemp;
	}
	/*
	 * get tuple from tuplefile(tupples)
	 * used for insertion 
	 */
	
	@SuppressWarnings("unchecked")
	private ArrayList <ArrayList <Value>>  getTupleList(File tupleFile)throws IOException, ClassNotFoundException{
		ArrayList <ArrayList <Value>>  tupleList = null;
		FileInputStream fileIn = new FileInputStream(tupleFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		tupleList = (ArrayList <ArrayList <Value>> ) in.readObject();
		in.close();
		fileIn.close();

		return tupleList;
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
	public void saveTupleListTemp() throws ClassNotFoundException, IOException
	{
		ArrayList <ArrayList <Value>> tupleList= null;
		//save tupples in each table to related tupleFile
		for (TupleFileTemp temp:this.tupleFileTemp) {
			File tupleFile = new File(temp.getTableName() + ".db");
			if (tupleFile.exists()) {
				tupleList = this.getTupleList(tupleFile);
				tupleList.clear(); //see if we can not clear 
			}
			else{
				tupleList = new ArrayList <ArrayList <Value>> ();
			}
			//only append temp tupple list in memory
			//so when we have multiple not continuing insert
			// in sql, it will append the old but inserted ones
			// solution add clear()
			tupleList.addAll(temp.getTupleList());
			//store ALL inserted tupples into file
			this.saveTupleList(tupleFile, tupleList);
			System.out.println("Tuple inserted successfully\n---------");
		}		
	}
	
	/**
	 * 
	 * @param tupleFile
	 * @param tupleList
	 * @throws IOException
	 */
	private void saveTupleList(File tupleFile, ArrayList <ArrayList <Value>> tupleList)throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(tupleFile);
		ObjectOutputStream out = new ObjectOutputStream(outFile);
		//AppendingObjectOutputStream out = new AppendingObjectOutputStream(outFile);
		out.writeObject(tupleList);
		out.close();
		outFile.close();
	}
	
	
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

	public void select(Select query) throws IOException, Error, ClassNotFoundException{
		
		Hashtable<String, Table> tablePool = null;
		boolean isNormalUser = query.isNormalUser();
		File tableFile = new File(databaseDefUrl);
		
		//Check if database defined
		if(tableFile.exists()){
			tablePool = this.getTableDef();
		}else{
			throw new Error("SELECT: No Table Defined");
		}

		ArrayList<String> tableNames = query.getTableNames();
		ArrayList<String> columnList = query.getAttrStrList();
		Condition selectCond = query.getCondition();
		
		//Hash table and arraylist to save table
		Hashtable<String, Table> tableList = new Hashtable<String, Table>();
		ArrayList<Table> tableArrayList = new ArrayList<Table>();
		
		//Hash table to save tuples for each table
		Hashtable<String, ArrayList< ArrayList<Value> > > tupleHashTable = 
				new Hashtable<String, ArrayList<ArrayList<Value>> >();

		//Check if the table defined
		for(String tableName : tableNames){
			if(!tablePool.containsKey(tableName)){
				throw new Error("SELECT: No table " + tableName + " Found");
			}else{
				tableList.put(tableName, tablePool.get(tableName));
				tableArrayList.add(tablePool.get(tableName));
			}

			File tupleFile = new File(tableName + ".db");
			if(!tupleFile.exists()){
				throw new Error("SELECT: No data in the table: " + tableName); 
			}else{
				ArrayList< ArrayList<Value> > tupleList = this.getTupleList(tupleFile);
				tupleHashTable.put(tableName, tupleList);
			}
			
		}

		//Get conditional attributes if not null
		ArrayList<String> conditionAttributeList = null;
		if(selectCond != null){
			conditionAttributeList = selectCond.getIdList();
		}

		//Get all attributes without duplicates
		ArrayList<String> allAttributeList = null;
		if(!query.getSelectAll()){
			allAttributeList = new ArrayList<String>(columnList);

		}else{
			//If select all attributes
			allAttributeList = new ArrayList<String>();
			
			//Check if needs to check subschema
			if(!isNormalUser){
				for(String tableName : tableList.keySet()){
					Table table = tableList.get(tableName);
					for(Attribute attr : table.getAttrList()){
						allAttributeList.add(attr.getName());
						
					}
				}

			}else{
				//Put all attributes in the subschema
				for(String tableName : tableList.keySet()){
					Table table = tableList.get(tableName);
					ArrayList<String> subSchemaList = table.getSubschemaList();
					for(Attribute attr : table.getAttrList()){
						if(subSchemaList != null){
							if(subSchemaList.contains(attr.getName()) != false){
								allAttributeList.add(attr.getName());
							}
						}else{
							allAttributeList.add(attr.getName());
						}
					}
	
				}


			}

		}
			//Add condition attributes into all attributes if not added yet
			if(conditionAttributeList != null){
				for(String condStrAttr : conditionAttributeList){
					if(!allAttributeList.contains(condStrAttr)){
						allAttributeList.add(condStrAttr);
					}
				}
			}

		//Check if a selected attribute or conditional attribute in the table
		for(String attrName : allAttributeList){
			boolean containsAttr = false;
			for(String tableName : tableList.keySet()){
				Table table = tableList.get(tableName);
				ArrayList<String> subSchemaList = table.getSubschemaList();

				if(table.getAttrPos(attrName) != -1){
					containsAttr = true;

					//Check subschema for normal user
					if(isNormalUser && subSchemaList != null){
						if(subSchemaList.contains(attrName) == false){
							containsAttr = false;
						}
					}
				}
			}
			if(containsAttr == false){
				throw new Error("SELECT: Attribute " + attrName + " does not exists");
			}
		}

		//Start joining multiple tables to a single table that depends on all attributes needs to be in the new table
		TuplesWithNameTable combinedTable = combineTables(tableArrayList, tupleHashTable, allAttributeList, query.getSelectAll(), isNormalUser);

		//Evaluate condition
		if(selectCond != null){
			combinedTable = getTuplesBySelectedCond(selectCond, combinedTable);
		}
		
		//Obtain selected values tuples
		TuplesWithNameTable selectedValuesTable = null;

		if(!query.getSelectAll()){
			selectedValuesTable = this.getTuplesBySelectedValue(columnList, combinedTable);	
		}else{
			selectedValuesTable = combinedTable;
		}
		printTable(selectedValuesTable);
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
	 */
	public Table getTableByName(String name) throws ClassNotFoundException, IOException{
		Hashtable<String, Table> tables = this.getTableDef();
		return tables.get(name);
	}

	private void printTable(TuplesWithNameTable tuplesTable){
		System.out.println();
		ArrayList<ArrayList<Value>> tupleList = tuplesTable.getTupleList();
		Hashtable<String, Integer> nameTable = tuplesTable.getNameTable();
		if(tupleList.size()== 0){
			throw new Error("No tuple selected");
		}
		String[] orderedAttrNames = new String[nameTable.size()];

		//Get ordered attribute names
		for(String attrName : nameTable.keySet()){
			orderedAttrNames[nameTable.get(attrName).intValue()] = attrName;
		}

		//Print attribute names
		for(int i = 0; i < orderedAttrNames.length; i++){
			System.out.printf("%-20s", orderedAttrNames[i]);
		}

		System.out.println();

		for(ArrayList<Value> tuple : tupleList){
			for(Value value : tuple){
				System.out.printf("%-20s", value.toString());
			}
			System.out.println();
		}

		System.out.println(tupleList.size() + " tuples selected");
	}

	
	private TuplesWithNameTable getTuplesBySelectedValue(ArrayList<String> selectedList, TuplesWithNameTable tuples){
		Hashtable<String, Integer> nameTable = tuples.getNameTable();
		Hashtable<String, Integer> newNameTable = new Hashtable<String, Integer>();

		ArrayList< ArrayList<Value> > tupleList = tuples.getTupleList();
		ArrayList< ArrayList<Value> > newTupleList = new ArrayList< ArrayList<Value> >();

		int nameCount = 0;		
		for(String selectedValue : selectedList){
			newNameTable.put(selectedValue, nameCount);
			nameCount++;
		}

		for(ArrayList<Value> tuple : tupleList){
			ArrayList<Value> newTuple = new ArrayList<Value>();
			for(String selectedValue : selectedList){
				newTuple.add(tuple.get( nameTable.get(selectedValue).intValue() ) );
			}
			newTupleList.add(newTuple);
		}

		return new TuplesWithNameTable(newNameTable, newTupleList);

	}
	
	private TuplesWithNameTable getTuplesBySelectedCond(Condition cond, TuplesWithNameTable tuples){
		Hashtable<String, Integer> nameTable = tuples.getNameTable();

		ArrayList< ArrayList<Value> > tupleList = tuples.getTupleList();
		ArrayList< ArrayList<Value> > newTupleList = new ArrayList< ArrayList<Value> >();
		
		Exp exp = cond.getExp();
		Object retBool;

		for(ArrayList<Value> tuple : tupleList){
			retBool = exp.accept(this, nameTable, tuple);
			if(retBool instanceof Boolean){
				if( ((Boolean) retBool).booleanValue() == true){
					newTupleList.add(tuple);
				}
			}else{
				throw new Error("SELECT: Tuple select condition evaluation failed");
			}

		}
		return new TuplesWithNameTable(nameTable, newTupleList);
	}
	
	private TuplesWithNameTable combineTables(ArrayList<Table> tables, Hashtable<String, ArrayList< ArrayList<Value> > > tupleHashtable, ArrayList<String> allAttributes, boolean selectAll, boolean isNormalUser){

		//ArrayList<ArrayList<Value>> combinedTupleList = new ArrayList<ArrayList<Value>>();
		//Hashtable<String, Integer> combinedAttrNameList = new Hashtable<String, Integer>();		
 
		LinkedList<TuplesWithNameTable> allTables = new LinkedList<TuplesWithNameTable>();

		for(Table table : tables){
			ArrayList< ArrayList<Value> > tupleList = tupleHashtable.get(table.getTableName());

			//Get a table that contains all values needed
			TuplesWithNameTable neededValueTable = null;
			if(!selectAll){
				neededValueTable = this.getNeededValuesTuples(table, tupleList, allAttributes);
			}else{
				//Check if it is normal user and select only subschema values
				if(!isNormalUser){
					neededValueTable = new TuplesWithNameTable(table.getAttrPosHashtable(), tupleList);
				}else{
					neededValueTable = this.getNeededValuesTuples(table, tupleList, allAttributes);
				}
			}
			allTables.add(neededValueTable);
		}


		return cartesianProduct(allTables);
	}

	private TuplesWithNameTable cartesianProduct(LinkedList<TuplesWithNameTable> allTables){
		//LinkedList<TuplesWithNameTable> cloneAllTables = new LinkedList<TuplesWithNameTable>(allTables);

		while(allTables.size() >= 2){
			TuplesWithNameTable combinedTable = _cartesianProduct(allTables.get(0), allTables.get(1));
			allTables.removeFirst();
			allTables.removeFirst();
			allTables.addFirst(combinedTable);
		}
		
		return allTables.get(0);
	}

	private TuplesWithNameTable _cartesianProduct(TuplesWithNameTable table1, TuplesWithNameTable table2){
			Hashtable<String, Integer> nameTable;
			ArrayList< ArrayList<Value> > tupleList;

			nameTable = new Hashtable<String, Integer>(table1.getNameTable());
			tupleList = new ArrayList<ArrayList<Value>>();

			int table1Size = nameTable.size();
			Hashtable<String, Integer> table2NameTable = table2.getNameTable();

			//Update name table position
			for(String key : table2NameTable.keySet()){
				nameTable.put(key, table2NameTable.get(key) + table1Size);
			}

			//Product table1 with table2
			for(ArrayList<Value> tuple1 : table1.getTupleList()){
				
				for(ArrayList<Value> tuple2 : table2.getTupleList()){
					ArrayList<Value> combinedTuple = new ArrayList<Value>(tuple1);
					combinedTuple.addAll(tuple2);
					tupleList.add(combinedTuple);
				}
			}

			return new TuplesWithNameTable(nameTable, tupleList);

	}


	private TuplesWithNameTable getNeededValuesTuples(Table table, ArrayList< ArrayList<Value> > tuples, ArrayList<String> allAttributes){

			ArrayList<ArrayList<Value>> newTupleList = new ArrayList<ArrayList<Value>>();
			Hashtable<String, Integer> newAttrNamePos = new Hashtable<String, Integer>();	
			ArrayList<Integer> neededAttrPos = new ArrayList<Integer>();

			//Save all attributes positions needed
			for(String attrName : allAttributes){
				int attrPos;
				if( (attrPos = table.getAttrPos(attrName)) != -1){
					newAttrNamePos.put(attrName, neededAttrPos.size());
					neededAttrPos.add(attrPos);
				}
			}


			//Save all needed values in each tuple
			for(ArrayList<Value> tuple : tuples){
				ArrayList<Value> newTuple = new ArrayList<Value>();
				for(Integer valuePos : neededAttrPos){
					newTuple.add(tuple.get(valuePos));
				}
				newTupleList.add(newTuple);
			}

			return new TuplesWithNameTable(newAttrNamePos, newTupleList);
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
			ArrayList <ArrayList <Value>> tupleList, 
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
	private ArrayList <Value> convertInsertValueType(Table table, ArrayList <String> values) throws Error
	{
		  ArrayList <Value> valueList = new ArrayList <Value> ();
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
		  			valueList.add(value);
		  		}
		  		else if(type == Attribute.Type.CHAR){
		  			//check type and length 
		  			if (attribute.getLength() < strValue.length()) {
		  				throw new Error("INSERT: Value " + strValue + "length: "+attribute.getLength()+"<->"+strValue.length()+" mismatch");
		  			}
		  			Value charValue = new Value(strValue);
		  			valueList.add(charValue);
		  		}
		  	}
		  	catch(NumberFormatException ex){
		  		throw new Error("INSERT: Value " + strValue + " is wrong type or exceed length\n");
		  	}

		  }
		  return valueList;
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
	private Hashtable <String, Table> getTableDef()
			throws IOException, ClassNotFoundException
	{
		Hashtable <String, Table> tables = null;
		File tableFile = new File(databaseDefUrl);
		
		FileInputStream fileIn = new FileInputStream(tableFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		tables = (Hashtable<String, Table>) in.readObject();
		in.close();
		fileIn.close();

		return tables;
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
	 * @param bExp : binary expression
	 * @param value : 
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	public Object visit(
			BinaryExp bExp, 
			Value value, 
			Hashtable<String, Integer> attrPosTable, 
			ArrayList<Value> tuple)
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
	
	public Object visit(IntExp exp, Value value){
		//System.err.println("Enter into intExp ");
		return Integer.valueOf(exp.getInt());
	}
		
	public Object visit(StrExp exp, Value value){
		//System.err.println("Enter into StrExp ");
		return exp.getStr();
	}
	
	
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
	
	public Object visit(IdExp exp, Hashtable<String, Integer> attrPosTable, ArrayList<Value> tuple){
		String attrName = exp.getId();		
		Value value = tuple.get(attrPosTable.get(attrName).intValue());
	
		return visit(exp, value);
	}
	
	public Object visit(Exp exp,  Hashtable<String, Integer> attrPosTable, ArrayList<Value> tuple){
	
		if(exp instanceof BinaryExp){
			//System.err.println("Enter into visit binary");
			return ((BinaryExp) exp).accept(this, null, attrPosTable, tuple);
		}else if(exp instanceof StrExp){
			//System.err.println("Enter into visit str");
			return ((StrExp) exp).accept(this, null);
		}else if(exp instanceof IdExp){
			return ((IdExp) exp).accept(this, attrPosTable, tuple);
		}else if(exp instanceof DoubleExp){
			return ((DoubleExp) exp).accept(this, null);
		}else if(exp instanceof IntExp){
			//System.err.println("Enter into visit int");
			return ((IntExp) exp).accept(this, null);
		}else{
			return Boolean.valueOf(true);
		}
	
	}
	
	public Object visit(Exp exp, Value value){
		
		if(exp instanceof BinaryExp){
			//System.err.println("Enter into visit binary");
			return ((BinaryExp) exp).accept(this, value, null, null);
		}else if(exp instanceof StrExp){
			//System.err.println("Enter into visit str");
			return ((StrExp) exp).accept(this, value);
		}else if(exp instanceof IdExp){
			return ((IdExp) exp).accept(this, value);
		}else if(exp instanceof DoubleExp){
			return ((DoubleExp) exp).accept(this, value);
		}else if(exp instanceof IntExp){
			//System.err.println("Enter into visit int");
			return ((IntExp) exp).accept(this, value);
		}else{
			return Boolean.valueOf(true);
		}
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

/*

public void insert (Insert query)
			throws IOException, Error, ClassNotFoundException
	{
		Hashtable <String, Table> tables = null;
		//ArrayList <ArrayList <Value>> tupleList;
		Table table;
		
		///////////////////////////////////
		// get table from tableFile
		///////////////////////////////////
		File tableFile = new File(databaseDefUrl);
		if (tableFile.exists()) {
			tables = this.getTableDef();
		}else{
			throw new Error("INSERT: No database defined");
		}
		
		///////////////////////////////////
		// Insert into tables
		// 	 using hash structure
		///////////////////////////////////
		if ((table = tables.get(query.getTableName()))!= null ) { 
			// check values integrity and input in tuple
			ArrayList <Value> tuple = this.convertInsertValueType(table, query.getValueList());
			
			// get tuple list by table name
			File tupleFile = new File(query.getTableName() + ".db");
			if (tupleFile.exists()) {
				tupleList = this.getTupleList(tupleFile);
			}
			else{
				tupleList = new ArrayList <ArrayList <Value>> ();
			}
			
			// check primary key null or notRepeat
			if (tupleList != null && tuple != null) {
				
				boolean checkPrimaryKeyRepeated = this.checkPrimarys(table.getPrimaryList(), tupleList, tuple);
				if (!checkPrimaryKeyRepeated) {
					tupleList.add(tuple);
				}else{
					throw new Error ("INSERT: primary key is notRepeat or null\n");
				}
			}
			
			//see if we can just save one tuple instead of all tuples
			this.saveTupleList(tupleFile, tupleList);
			System.out.println("Tuple inserted successfully\n---------");

		}else{
			throw new Error ("INSERT: No Table "+ query.getTableName() + " Found\n");
		}
	}

*/
