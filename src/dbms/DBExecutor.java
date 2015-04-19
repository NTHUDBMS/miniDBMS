package dbms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import manageDatabase.BinaryExp;
import manageDatabase.Condition;
import manageDatabase.Create;
import manageDatabase.DoubleExp;
import manageDatabase.Exp;
import manageDatabase.IdExp;
import manageDatabase.Insert;
import manageDatabase.IntExp;
import manageDatabase.Query;
import manageDatabase.Select;
import manageDatabase.StrExp;
import manageDatabase.TuplesWithNameTable;
import structure.Attribute;
import structure.Table;
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
	
	
	public DBExecutor(){
		// clear databaseDefUrl
		File tableFile = new File(databaseDefUrl);
		tableFile.delete();
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
		
		// save table as a file
		File tableFile = new File(databaseDefUrl);
		
		//
		if (tableFile.exists()) {
			tables = this.getTableDef();
		}else{
			tables = new Hashtable<String, Table>();
		}
		
		//
		if (!tables.containsKey(query.getTableName())) {
			//store table in hash table
			tables.put(query.getTableName(), query.getTable());
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
	 * check primary whether is repeated
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
		ArrayList <Value> valueList = null;
		ArrayList <ArrayList <Value>> tupleList;
		Table table;
		/*
		 * get table from tableFile
		 */
		
		File tableFile = new File(databaseDefUrl);
		if (tableFile.exists()) {
			tables = this.getTableDef();
		}else{
			throw new Error("INSERT: No database defined");
		}
		
		//do the hash
		if ((table = tables.get(query.getTableName()))!= null ) {
			//exist table
			//check values integrity and input in valuelist
			valueList = this.convertInsertValueType(table, query.getValueList());
			
			File tupleFile = new File(query.getTableName() + ".db");
			if (tupleFile.exists()) {
				tupleList = this.getTupleList(tupleFile);
			}
			else
				tupleList = new ArrayList <ArrayList <Value>> ();
			if (tupleList != null && valueList != null) {
				boolean primarykeysNotRepeat = this.checkPrimarys(table.getPrimaryList(), tupleList, valueList);

				if (primarykeysNotRepeat) {
					tupleList.add(valueList);
				}else{
					throw new Error ("INSERT: primary key is repeated or null");
				}

			}
			//see if we can just save one tuple instead of all tuples
			this.saveTupleList(tupleFile, tupleList);
			System.out.println("Tuple inserted successfully");

		}else{
			throw new Error ("INSERT: No Table "+ query.getTableName() + " Found");
		}


	}
	
	

	public void select(Select query) throws IOException, Error, ClassNotFoundException{
		Hashtable<String, Table> tables = null;
		boolean isNormalUser = query.isNormalUser();
		File tableFile = new File(databaseDefUrl);
		
		//Check if database defined
		if(tableFile.exists()){
			tables = this.getTableDef();
		}else{
			throw new Error("SELECT: No Database Defined");
		}

		ArrayList<String> tableNames = query.getTableNames();
		ArrayList<String> attrStrList = query.getAttrStrList();
		Condition selectCond = query.getCondition();
		//Hash table and arraylist to save table
		Hashtable<String, Table> tableList = new Hashtable<String, Table>();
		ArrayList<Table> tableArrayList = new ArrayList<Table>();
		//Hash table to save tuples for each table
		Hashtable<String, ArrayList< ArrayList<Value> > > tupleHashTable = new Hashtable<String, ArrayList<ArrayList<Value>> >();

		//Check if the table defined
		for(String tableName : tableNames){
			if(!tables.containsKey(tableName)){
				throw new Error("SELECT: No table " + tableName + " Found");
			}else{
				tableList.put(tableName, tables.get(tableName));
				tableArrayList.add(tables.get(tableName));
			}

			File tupleFile = new File(tableName + ".db");
			if(!tupleFile.exists()){
				throw new Error("SELECT: No tuple in the table " + tableName); 
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
		if(!query.isSelectAll()){
			allAttributeList = new ArrayList<String>(attrStrList);

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
		TuplesWithNameTable combinedTable = combineTables(tableArrayList, tupleHashTable, allAttributeList, query.isSelectAll(), isNormalUser);

		//Evaluate condition
		if(selectCond != null){
			combinedTable = getTuplesBySelectedCond(selectCond, combinedTable);
		}
		
		//Obtain selected values tuples
		TuplesWithNameTable selectedValuesTable = null;

		if(!query.isSelectAll()){
			selectedValuesTable = this.getTuplesBySelectedValue(attrStrList, combinedTable);	
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

	
	//cannot be null is not yet implemented
	//primary key cannot be null or repeated!!!!
	/**
	 * 
	 * @param primaryList
	 * store primary attribute position in table
	 * @param tupleList
	 * old tuple list
	 * @param newValueList
	 * new tuple list to check if primary repeated in old
	 * @return
	 */
	private boolean checkPrimarys(ArrayList <Integer> primaryList, ArrayList <ArrayList <Value>> tupleList, ArrayList <Value> newValueList)
	{
		//check every tuple
		for (ArrayList <Value> tuple: tupleList ) {
			boolean isCorrect = false; //the flag is set to false every time
			// in each tuple check primary key in newvaluelist whether is repeated  
			for (Integer primaryPos : primaryList) {
				if (!newValueList.get(primaryPos).equals(tuple.get(primaryPos))) {
					isCorrect = true;
				}
			}
			if (isCorrect == false) { //end the loop no need to check each
				return false;
			}

		}
		return true;

	}
	
	/**
	 * Check insert value list's columns numbers whether the same as tables<br>
	 * convert strings in values to different types of object in valueList<br>
	 * based on  attribute list <br>
	 * 
	 * @param tableDef : table from the table file
	 * @param values : valueList from the query

	 */
	private ArrayList <Value> convertInsertValueType(Table tableDef, ArrayList <String> values) throws Error
	{
		  ArrayList <Value> valueList = new ArrayList <Value> ();
		  ArrayList <Attribute> attrList = tableDef.getAttrList();
		  String tableName = tableDef.getTableName();
		  int attrSize = attrList.size();
		  
		  // check column amount with query value
		  if (attrSize != valueList.size()) {
		  		throw new Error("INSERT: The number of Values is not matched, Table"
		  				+ tableName + "has " +attrSize + "Values");
		  }

		  //iterate attribute list, convert values and input valueList
		  for (int i = 0; i < attrSize; ++i) {
		  	Attribute attribute = attrList.get(i);	//
		  	String strValue = values.get(i);

		  	try{
		  		Attribute.Type type = attribute.getType();

		  		
		  		if (type == Attribute.Type.INT) {
		  			int intValue = Integer.parseInt(strValue);
		  			Value value = new Value(intValue);
		  			valueList.add(value);
		  		}
		  		/*
		  		 * need to check if varchar type string length exceed the create scheme defined
		  		 */
		  		else if(type == Attribute.Type.CHAR){
		  			//check type and length  //' ' +length of string
		  			if (attribute.getLength() +2 < strValue.length()|| strValue.charAt(0)!= '\'') {
		  				throw new NumberFormatException();
		  			}
		  			Value charValue = new Value(strValue);
		  			valueList.add(charValue);
		  		}

		  	}
		  	catch(NumberFormatException ex){
		  		throw new Error("INSERT: Value " + strValue + "is wrong type or exceed length");
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

	private void saveTupleList(File tupleFile, ArrayList <ArrayList <Value>> tupleList)throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(tupleFile);
		ObjectOutputStream out = new ObjectOutputStream(outFile);
		out.writeObject(tupleList);
		out.close();
		outFile.close();
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
		return exp.getString();
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
	
}//end DBExecutor





