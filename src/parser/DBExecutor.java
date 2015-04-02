
package parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import manageDatabase.Query;
import manageDatabase.Create;
import manageDatabase.Insert;
import structure.Table;
import structure.Value;
import structure.Attribute;

public class DBExecutor{
	private static final String databaseDefUrl = "databaseDef.dat";
	public void execute(Query query){
		try{
			if (query instanceof Create) 
				create((Create)query);
			else if (query instanceof Insert) 
				insert((Insert)query);
			
		}catch(IOException ex){
			System.err.println(ex.getMessage());
		}catch(ClassNotFoundException ex){
			System.err.println(ex.getMessage());
		}catch(Error ex){
			System.err.println(ex.getMessage());
		}
	}

	private void create(Create query)throws IOException, Error, ClassNotFoundException 
	{
		Hashtable<String, Table> tables = null;
		File tableFile = new File(databaseDefUrl);
		if (tableFile.exists()) {
			tables = this.getTableDef();
		}
		else
			tables = new Hashtable<String, Table>();
		if (tables.get(query.getTableName())!= null) {
			throw new Error("CREATE: Table: table" + query.getTableName()+ "is already exist");
		}

		//create table now
		tables.put(query.getTableName(), query.getTable());
		this.writeTableDef(tableFile, tables);
		System.out.println("Table is created");

	}

	public void insert(Insert query)throws IOException, Error, ClassNotFoundException{
		Hashtable <String, Table> tables = null;
		ArrayList <Value> valueList = null;
		ArrayList <ArrayList <Value>> tupleList;
		Table table;
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
	//cannot be null is not yet implemented
	//primary key cannot be null or repeated!!!!
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



	private ArrayList <Value> convertInsertValueType(Table tableDef, ArrayList <String> values) throws Error
	{
		  ArrayList <Value> valueList = new ArrayList <Value> ();
		  ArrayList <Attribute> attrList = tableDef.getAttrList();
		  String tableName = tableDef.getTableName();
		  int attrSize = attrList.size();
		  if (attrSize != valueList.size()) {
		  		throw new Error("INSERT: The number of Values is not matched, Table"
		  				+ tableName + "has " +attrSize + "Values");
		  }

		  //converting values and input valueList
		  for (int i = 0; i < attrSize; ++i) {
		  	Attribute attribute = attrList.get(i);
		  	String strValue = values.get(i);

		  	try{
		  		Attribute.Type type = attribute.getType();

		  		if (type == Attribute.Type.INT) {
		  			int intValue = Integer.parseInt(strValue);
		  			Value value = new Value(intValue);
		  			valueList.add(value);
		  		}
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


	//used for creation

	@SuppressWarnings("unchecked")
	private Hashtable <String, Table> getTableDef()throws IOException, ClassNotFoundException{
		Hashtable <String, Table> tables = null;
		File tableFile = new File(databaseDefUrl);
		FileInputStream fileIn = new FileInputStream(tableFile);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		tables = (Hashtable<String, Table>) in.readObject();
		in.close();
		fileIn.close();

		return tables;
	}

	private void writeTableDef(File tableFile, Hashtable<String, Table>tables)throws IOException
	{
		FileOutputStream outFile = new FileOutputStream(tableFile);
		ObjectOutputStream out = new ObjectOutputStream(outFile);
		out.writeObject(tables);
		out.close();
		outFile.close();
	}


	//used for insertion

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


}//end DBExecutor

