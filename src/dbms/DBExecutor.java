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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
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
	private List<String> tableList;
	
	/**
	 * 
	 */
	Map <String, Table> tables;
	
	/**
	 * used to store tuples of each table, after insert complete store this to file 
	 */
	private List <TupleFile> tupleFilePool; //each table has one
	
	
	/**
	 * Constructor which initialize the private member
	 */
	public DBExecutor() {
		// clear databaseDefUrl
		this.tableList = new ArrayList<String>();
        this.tables = new HashMap<String, Table>();
//		this.tupleFilePool = new ArrayList <TupleFile>();
	}
	
	public static void pause() {
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
	public void execute(Query query) {
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
		}
		catch (Exception ex) {
		    System.out.println(ex.getMessage());
		}
		catch(Error ex){
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * The operation to perform SQL : Create<br>
	 * write table definition to databaseDef.data
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
	private void create(Create query) throws IOException, Error, ClassNotFoundException {
		// built hashTable by tableName as hash key
		
		// find table definition data
	    System.out.println("start to create table");
	    
		File tableFile = new File(databaseDefUrl);
		if (tableFile.exists()) {
		    System.out.println("table exist");
			tables = this.getTablePool(tableFile);
		}else{
		    System.out.println("create new tables");
//			tables = new HashMap<String, Table>();
		}
		
		// create the table
		if ( !tables.containsKey(query.getTableName())) {
		    System.out.println("put table inside list");
		    
			Table table = new Table(query.getTableName(), query.getAttributes(), query.getPrimary(), query.getAttrPosTalbe());
			
			//store table in hash table
			tables.put(query.getTableName(), table);
			tableList.add(query.getTableName());
			
			//write tables in tablefiles
//			this.writeTableDef(tableFile, tables);
			
			//dump message
			System.out.println("Table is created\n---------");
			
		}else {
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
	public void insert (Insert query) throws Exception { 
		///////////////////////////////////////////
		// get table from tableFile
		///////////////////////////////////////////
		Map <String, Table> tablePool = this.tables;
		
		//search Table from Memory first
		String tableName = query.getTableName();
		//search table from Disk
		if (tablePool == null || ! tablePool.containsKey(tableName))  {
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
		// Insert into tupleStack
		// 	 using hash structure
		///////////////////////////////////////////

		//tupleStack is used to store tuples in memory
		Table table = tablePool.get(query.getTableName());
		

		if (table != null) { 
			// check values integrity and input in tuple
			Tuple tuple = this.convertInsertValueType(table, query.getValueList());
			TupleStack tupleStack = table.getTuples();
			// check primary key null or notRepeat
			if (tuple != null) {
				boolean checkPrimaryKeyRepeated = this.checkPrimarys(table.getPrimaryList(), tupleStack, tuple); 
				if (!checkPrimaryKeyRepeated) {
					tupleStack.add(tuple);//will also save in columns but what if
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
//	private TupleStack getTupleList(String tableName) throws Exception {
//		TupleStack  tupleStackReturn = null;
//		Hashtable<String, Table> tablePool = null;
//		Table table = null;
//		File tableFile = new File(databaseDefUrl);
//		
//		//Check if database defined
//		if(tableFile.exists()){
//			tablePool = this.getTablePool(tableFile);
//		}else{
//			throw new Error("SELECT: No Table Defined");
//		}
//		
//		table = tablePool.get(tableName);
//		
//		//fetch tupleFile if still store in memroy
//        tupleStackReturn = getOrCreateTuples(table);
//		
//		//tupleFile not in memory, fetch from HardDisk
//		if (tupleStackReturn == null) {
//			File tupleFile = new File(tableName + ".db");
//			if (tupleFile.exists()) {
//				tupleStackReturn = this.getTupleStack(tupleFile);
//			}
//			else {
//				//not exist, create new one & put into pool
//				tupleStackReturn = new TupleStack(table.getAttrList());
//				TupleFile newTupleFile = new TupleFile(tableName, tupleStackReturn);
//				this.tupleFilePool.add(newTupleFile);
//			}
//		}		
//		return tupleStackReturn;
//
//	}

	
//	private TupleStack getOrCreateTuples(Table table) {
//	    for (TupleFile f: this.tupleFilePool) {
//	        if (f.getTableName().equals(table.getTableName()))
//	            return f.getTupleStack();
//	    }
//	    TupleStack ts = new TupleStack(table.getAttrList());
//	    this.tupleFilePool.add(new TupleFile(table.getTableName(), ts));
//	    return ts;
//	}
	
	/**
	 * get tuple from tuplefile (tupples)
	 * used for insertion
	 * @param tupleFile
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private TupleStack getTupleStack(File tupleFile)
			throws IOException, ClassNotFoundException,InvalidClassException {
		TupleStack  tupleStack = null;
		FileInputStream fileIn = new FileInputStream(tupleFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
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
	public void saveTupleList() throws ClassNotFoundException, IOException {
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
	public void printTuple(List <Value> tuple){
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
    int getTableIndex(List<Table> tableArrayList, String selectAttr) {
        int findTimes = 0;
        int tableIndex = -1;
        if (selectAttr.equals("*")) {
            return tableIndex;
        }
        for(Table table: tableArrayList) {
            int attrPos = table.getAttrPos(selectAttr);
            if(attrPos!= -1) {
                tableIndex = tableArrayList.indexOf(table); 
                findTimes++;
            }
        }
        if(findTimes==0 || findTimes>1)
            throw new Error("Select attribute has no responding table"
                    + " or can specify in which tables");
        return tableIndex;
    }
    
    
    boolean checkTablesInMemory(List<String> tableNames) {
		if(this.tables != null) {
		    for(String tableName : tableNames){
				if(!this.tables.containsKey(tableName)){
				    return false;
				}
			}
		}
		return true;
    }
   	
	/**
	 * Perform the Select action of the query
	 * @param query : the "Select_From" query
	 * @throws IOException
	 * @throws Error
	 * @throws ClassNotFoundException
	 */
	public void select(Select query) {

		List<String> tableNames = query.getTableNames();
		boolean selectTableInMemory = checkTablesInMemory(tableNames);
		
		//get table from file I/O
		File tableFile = new File(databaseDefUrl);
//		if(selectTableInMemory == false && tableFile.exists()){
//			tablePool = this.getTablePool(tableFile);
//		} else if(selectTableInMemory==false && (!tableFile.exists())){
//			throw new Error("SELECT: No Table Pool Defined");
//		}

		//////////////////////////////////////////
		// Hash table and arrayList to save table
		// Hash table to save tuples for each table
		//////////////////////////////////////////
		
		Hashtable<String, Table> tableList = new Hashtable<String, Table>();
		List<Table> tableArrayList = new ArrayList<Table>();
		//////////////////////////////////////////
		// Check if the TupleFile defined
		//////////////////////////////////////////
		
		//tableNames have type user1.userId bug
		for(String tableName : tableNames) {
			if (!this.tables.containsKey(tableName)) {
				throw new Error("SELECT: No table " + tableName + " Found");
			} else {
				tableList.put(tableName, this.tables.get(tableName));
				System.out.println("add table "+tableName+" okay");
				tableArrayList.add(this.tables.get(tableName));
			}
		}

		////////////////////////////////////////
		// Get conditional attributes if not null
		////////////////////////////////////////
		List<String> conditionTableList = new ArrayList<>();
		List<String> conditionAttributeList = new ArrayList<>();
		
		if(query.getCondition() != null) {
			Condition selectCond = query.getCondition();
//			System.out.println("add condition okay");
			//get IdList will setting Idlist and TableList in condition.java
			conditionAttributeList = selectCond.getIdList();
			conditionTableList = selectCond.getTableList();
		}
		////////////////////////////////////////
		// Get all attributes without duplicates
		////////////////////////////////////////
		List<Integer> attrTableRelation = query.getAttrTableRelation();
		List<String> targetAttrList = query.getAttrList();

		//used to check if select or conditional attrs in tables
		Map<String, String> attr2Table = new HashMap<>();
		
		//			userId will add twice because in diffrent table
        for(int i = 0; i < targetAttrList.size(); ++i) {
             if (query.getAggregateMode() == Select.Aggregation.COUNT)
                break;
            String selectAttr = targetAttrList.get(i);
            Integer tableIndex = attrTableRelation.get(i);
            //get what table
            
            if (tableIndex == -1) {
				//find which table for this condition attr
                tableIndex = this.getTableIndex(tableArrayList, selectAttr);
                attrTableRelation.set(i, tableIndex); //reset the related table
            }
            //add all attrs in selected table if it's *
            if (selectAttr.equals("*") && tableIndex == -1) {
                for (Table table: tableArrayList) {
                    for(Attribute attr : table.getAttrList()) {
                        attr2Table.put(table.getTableName() + "." + attr.getName(), table.getTableName());
                    } 
                }
            }
            else {
                Table table= tableArrayList.get(tableIndex);
                if (selectAttr.equals("*")) {
                    for(Attribute attr : table.getAttrList()) {
                        attr2Table.put(table.getTableName() + "." + attr.getName(), table.getTableName());
                    } 
                }
                else {
                    attr2Table.put(table.getTableName() + "." + targetAttrList.get(i), table.getTableName());
                }
            }
        }
        
        List<TupleFile> tfs = new ArrayList<>();
        for (Table table: tableArrayList) {
            TupleFile tf = new TupleFile(table.getTableName(), table.getTuples());
            tfs.add(tf);
        }
        checkConditionAttrExist(conditionAttributeList, conditionTableList, tableArrayList, tableList);

        this.applyCondition2Tables(tfs, attr2Table, query.getCondition());

        TupleStack combinedTable = this.combineTables(tfs, query.getCondition());
        printOut(attr2Table, combinedTable, query);
	}

    void checkConditionAttrExist(List<String>conditionAttributeList, List<String> conditionTableList, final List<Table> tableArrayList, Map<String, Table> tableList) {
		 if(!conditionAttributeList.isEmpty()) {
			for(int i =0; i < conditionAttributeList.size(); ++i) {
				String inputAttr = conditionAttributeList.get(i);
				String inputTableName = conditionTableList.get(i);
				String combine = inputTableName + "." + inputAttr;
					if(inputTableName == "") {
						//find which table for this condition attr
						int findTimes = 0;
						for (Table table: tableArrayList) {
						    if (table.containAttr(inputAttr)) {
						        findTimes ++;
                            }
						}
                        if(findTimes == 0 || findTimes > 1)
							throw new Error("Condtion attribute has no responding table"
									+ " or can specify in which tables");
					}
					else if (tableList.containsKey(inputTableName) && tableList.get(inputTableName).containAttr(inputAttr)){
					    continue;
					}
					else {
					    throw new Error("we don't have the condition attr: " + combine);
					}
			}
        }   
		
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
		if (this.tables.containsKey(name)) {
		    return this.tables.get(name);
		}
		else if(tableFile.exists()){
			tablePool = this.getTablePool(tableFile);
			return tablePool.get(name);
		}
		else return null;
		
	}
	
	private void printOut(Map<String, String> attr2Table, TupleStack combinedTable, Select query) {
	    if (combinedTable == null) System.out.println("the selected result is empty");

	    System.out.println("output table size is " + combinedTable.getLength());
	    
		if(query.getAggregateMode() == Select.Aggregation.NON) {
		    System.out.println("no mode");
		    
            TupleStack selectedValuesTable = this.projection(attr2Table, combinedTable);
            System.out.println(selectedValuesTable.size());
            
			printTable(selectedValuesTable);
		}
			
		else if(query.getAggregateMode() == Select.Aggregation.COUNT)
		{
			System.out.println("----------------select "+combinedTable.size()+" tuples");
		}
		else if(query.getAggregateMode() ==Select.Aggregation.SUM)
		{
			int sum = 0;
            for (String attr :attr2Table.keySet()) {
                System.out.println(attr);
                int pos = combinedTable.getAttrsName().indexOf(attr);
                if (pos == -1) {
                    pos = combinedTable.getAttrsName().indexOf(attr.split("\\.")[1]);
                }
                for(Tuple tuple: combinedTable) {
                    sum += tuple.get(pos).getInt();	
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
		
		List<String> selectattrs = tupleList.getAttrsName(); 
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
	private TupleStack projection(Map<String, String> attr2Table, TupleStack tupleStack) {
		List<Attribute> attrList = tupleStack.getAttrList();
		List<String> attrNames = tupleStack.getAttrsName();
		
		List<Integer> saveIndex = new ArrayList<Integer>();
		List<Attribute> newAttrList = new ArrayList<>();
		
		for (String selectAttrWtable: attr2Table.keySet()) {
		    
		    String[] tableAttr = selectAttrWtable.split("\\.");
		    String selectAttr = tableAttr[1];
		    
			if (attrNames.contains(selectAttr)) {
			    int index = attrNames.indexOf(selectAttr);
                saveIndex.add(index);
                newAttrList.add(new Attribute(attrList.get(index)));
            }
			else if(attrNames.contains(selectAttrWtable)) {
                int index = attrNames.indexOf(selectAttrWtable);
                saveIndex.add(index);
                newAttrList.add(new Attribute(attrList.get(index)));
            }
			else throw new Error("select Attribute not in condition tuple Stack");
		}
	
		if (attrList.size() == newAttrList.size()) {
		    System.out.println("column size is same");
			return tupleStack;
		}
		
		// create new TupleStack by selected columns
		TupleStack newTupleList = new TupleStack(newAttrList);
		for(Tuple tuple : tupleStack) {
			Tuple newOne = new Tuple();
			for (int index : saveIndex) {
				newOne.add(new Value(tuple.get(index)));
			}
			newTupleList.add(newOne);
		}
		
		return newTupleList;
	}

	// naively linear search all tuples match condition
	private void selectTuplesByCondition(Condition condition, TupleFile table) {
        if(condition != null) {
            if (condition.getCompareExpList().size() == 0) {
                return;
            }
//            List<Exp> exp = condition.getCompareExpList();
            Exp exp = condition.getExp();
            
            Hashtable<String, Integer> attrPos = new Hashtable<>();
            List<String> names = table.getTupleStack().getAttrsName();
            for (int i = 0; i < names.size(); i++) {
                attrPos.put(names.get(i), i);
            }
            List<Tuple> r = new ArrayList<>();
            for (Tuple tuple: table.getTupleStack()) {
                Object retBool = exp.accept(this, attrPos, tuple);
                
                if (retBool instanceof Boolean) {
                    if(((Boolean) retBool)) {
                        r.add(new Tuple(tuple));
                    }
                }
            }
            
            table.getTupleStack().clear();
            table.getTupleStack().addAll(r);
        }
	}
	
	/**
	 * create new tupleStack base on condition
	 * @param conde
	 * @param tuples
	 * @return
	 */
	private TupleStack getTuplesBySelectedCond(Condition cond, TupleStack tuples) {
		Hashtable<String, Integer> attrPos = null;
		TupleStack newTupleList = new TupleStack(tuples.getSelectattrList(),true);

		if(newTupleList!=null)
			attrPos = newTupleList.getAttrPosTable();
		Exp exp = cond.getExp();
		Object retBool;
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
	
	private void filterTableByCondition(List<TupleFile> tables, Condition condition) {
	    System.out.println("filter table by condition");
	    for(TupleFile table : tables){
			TupleStack tupleList = table.getTupleStack();
            if(condition != null) {
			    tupleList.filterByCondition(condition);
			}
		}
	    
	}
	
	private void applyCondition2Tables(List<TupleFile> tables, Map<String, String> attr2Table, Condition condition) {
	    if (condition == null) return;
	    filterTableByCondition(tables, condition);
		// Naive way to eliminate tuples by linear search and remove
	    // can not take join tables since we don't combine all tables first

	    if (tables.size() == 1) {
			for (TupleFile table : tables) {
//	         should use on join or all compare but the table must include all the attribute in two table
			// can only take one big table
                this.selectTuplesByCondition(condition, table);
            }        
	    }
	}
	
	

	/**
	 * 
	 * @param tables
	 * @param tupleHashtable
	 * @param selectAttrLsit 
	 * @param selectTableList 
	 * @param condition 
	 * @return
	 */
	private TupleStack combineTables(List<TupleFile> tables, Condition condition) {

	    if (tables.size() == 1) {
	        System.out.println("only one table no need to combine");
	        return tables.get(0).getTupleStack();
	    }
	    else if (tables.size() == 2) {
	        System.out.println("do cartesian product");
            return cartesianProduct(tables, condition);
	    }
	    else throw new Error("table size must <= 2 and >= 1");
	}

	
	private TupleStack cartesianProduct(List<TupleFile> tables, Condition condition){
	    TupleStack table1 = tables.get(0).getTupleStack();
	    TupleStack table2 = tables.get(1).getTupleStack();
	    String table1Name = tables.get(0).getTableName();
	    String table2Name = tables.get(1).getTableName();
	    
	    
		//set selectattrList
		if(condition != null && condition.isJoin()) {
//            TupleStack tupleList = new TupleStack(); 
            BinaryExp joinExp = (BinaryExp) condition.getJoinExp();
            System.out.println(joinExp);
            
            ColExp left = (ColExp)joinExp.getLeft();
            ColExp right =  (ColExp)joinExp.getRight();

            String leftAttr= left.getColomnName();
            String rightAttr = right.getColomnName();
//            String leftAttr= left.getTableName()+"."+left.getColomnName();
//            String rightAttr = right.getTableName()+ "." + right.getColomnName();
            System.out.print(leftAttr+ "   ");
            System.out.print(rightAttr + "\n");
            
            
            List<String> table1Attrs = table1.getAttrsName();
            List<String> table2Attrs = table2.getAttrsName();
            int posInTable1 = -1;
            int posInTable2 = -1;
            if (table1Name.equals(left.getTableName())) {
                posInTable1 = table1Attrs.indexOf(leftAttr);
                posInTable2 = table2Attrs.indexOf(rightAttr);
            }
            else {
                posInTable1 = table1Attrs.indexOf(rightAttr);
                posInTable2 = table2Attrs.indexOf(leftAttr);
            }
            if (posInTable1 == -1 || posInTable2 == -1) {
                throw new Error("join attribute not in table");
            }
            

            List <Value> joinColumn1 = table1.getColumnByPos(posInTable1);
            List <Value> joinColumn2 = table2.getColumnByPos(posInTable2);
            
            TupleStack tupleList = null;

            if (joinColumn1.size() < joinColumn2.size()) {
                tupleList = join(tables.get(0), tables.get(1), posInTable1, posInTable2);
            }
            else {
                tupleList = join(tables.get(1), tables.get(0), posInTable2, posInTable1);
            }
            
            return tupleList;
		}	
		//Product table1 with table2
        //has no condition
		else {
            table1.fullJoin(table2);
		    return table1;
		}
	}

    private Set<Value> intersection(Set<Value> st1, Set<Value> st2) {
        st1.retainAll(st2);
        System.out.println("total intersects are " + st1.size());
        return st1;
    }
	
    private TupleStack join(TupleFile table1, TupleFile table2, int posInTable1, int posInTable2) {
        TupleStack st1 = table1.getTupleStack();
        TupleStack st2 = table2.getTupleStack();
        
		List<Attribute> table1Attrs = st1.getAttrList();
		List<Attribute> table2Attrs = st2.getAttrList();
		        
		List<Attribute> combineAttr= new ArrayList<>();
		for (Attribute attr: table1Attrs) {
		    combineAttr.add(new Attribute(attr.getType(), table1.getTableName() + "." + attr.getName(), attr.getLength()));
		}
		for (Attribute attr: table2Attrs) {
		    combineAttr.add(new Attribute(attr.getType(), table2.getTableName() + "." + attr.getName(), attr.getLength()));
		}
		TupleStack tupleList = new TupleStack(combineAttr);
		
		for (Tuple t1 : st1) {
		    for (Tuple t2: st2) {
		        if (t1.get(posInTable1).equals(t2.get(posInTable2))) {
                    Tuple combine = new Tuple(t1);
                    combine.addAll(new Tuple(t2));
                    tupleList.add(combine);
		        }
		    }
		}
		return tupleList;
    }
	    
	/**
	 * create new tuple Stack based on select attribute
	 * also update the new stack's attribute list
	 * would contain table.XX
	 * @param table
	 * @param selectTableList
	 * @param tuples
	 * @param selectAttrList
	 * @return
	 */
	private TupleStack getNeededValuesTuples( Table table, List<String>selectTableList, TupleStack tuples, List<String> selectAttrList, Condition condition) {
		List<String> newAttrList = new ArrayList<String>();
		//needed index of attribute for tuple
		//then we know which index in tuple 
		//we want to insert into the new tuple
		List<Integer> neededAttrPos = new ArrayList<Integer>();
		//Save all attributes positions needed
		for(int i = 0; i < selectAttrList.size(); i++) {
			int attrPos;
			String attr = selectAttrList.get(i);
			//for ColExp
			if(attr.contains(".")==true) {
				String []parts = attr.split("\\.");
				attr = parts[1];
				if(selectTableList.get(i).equals(table.getTableName())
						   &&(attrPos = table.getAttrPos(attr)) != -1) {
					newAttrList.add(table.getTableName()+"."+attr);
					neededAttrPos.add(attrPos);
				}
			}
			else if(selectTableList.get(i).equals(table.getTableName())
			   &&(attrPos = table.getAttrPos(attr)) != -1) {
				newAttrList.add(attr);
				neededAttrPos.add(attrPos);
			}
		}
		//will set attrlistPos 
		//important to be used in condition checking
		//first time create new tupleStack in select O(n)
		//maybe can do indexing after here
		TupleStack newTupleList = new TupleStack(newAttrList,true);
		//Save all needed values in each tuple
		for(Tuple tuple : tuples){
			Tuple newTuple = new Tuple();
			for(Integer valuePos : neededAttrPos){
				newTuple.add(tuple.get(valuePos));
			}
			newTupleList.add(newTuple);
			//for indexing
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
	private boolean checkPrimarys(List <Integer> primaryList, TupleStack tupleList, List <Value> tuple) {
		boolean notRepeat = false;
		//check every tuple
		
		LABEL_OUTTER:
		for (List <Value> tupleIterator: tupleList ) 
		{
			// in each tuple check primary key in new valueList whether is notRepeat  
			for (int primaryPosition : primaryList) {
				// true if equals
				if (tuple.get(primaryPosition).equals(tupleIterator.get(primaryPosition))) {
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
	 * @return Tuple : one row in table
	 */
	private Tuple convertInsertValueType(Table table, List <String> values) throws Error
	{
		  Tuple tuple = new Tuple ();
		  List <Attribute> attrList = table.getAttrList();
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
	 * @return table name to table
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Hashtable <String, Table> getTablePool(File tableFile) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(tableFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Hashtable <String, Table> tablePool = (Hashtable<String, Table>) in.readObject();
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

	public List<String> getTableList() {
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
	public Object visit(
			BinaryExp bExp, 
			Value value, 
			Hashtable<String, Integer> attrPosTable, 
			Tuple tuple) {
//		System.err.println("Enter into BinaryExp ");//
		String op = bExp.getOp();
		Object ret = null;
		
		
		//this is unused!!
		if(bExp == null){
			return Boolean.valueOf(true);
		}
		
	
		Exp left = bExp.getLeft();
		Exp right = bExp.getRight();
		
		if (right == null) {
		    return false;
		}
	
		Object leftOp = null;
		Object rightOp = null;
		
		if(left != null){
//			System.err.println("Left not null " + op);//
			
			if(tuple == null){
				leftOp = left.accept(this, value);
			}else{
				leftOp = left.accept(this, attrPosTable, tuple);
			}
//			System.err.println("Left visited ");//
		}
	
		if(right != null){
//			System.err.println("Right not null " + op);//
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

