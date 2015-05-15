grammar Sql;

@header {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!                                                            !!!
    //!!! THIS CODE IS AUTOMATICALLY GENERATED! DO NOT MODIFY!       !!!
    //!!! Please refer to file Example.g4 for grammar documentation. !!!
    //!!!                                                            !!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
import java.util.*;
import java.io.*;
import manageDatabase.query.*;
import manageDatabase.expression.*;
import structure.*;
import dbms.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

}

@parser::members {
	
private final static int maxTuple = 100;
private final static int maxAttr = 5;
private final static DBExecutor executor = new DBExecutor();
private boolean inValid;

/**
 * Execute single SQL command. Won't execute if null
 * @param query : the SQL command
 */
public static void execute(Query query){
	try{
		if (query != null) {
			DBMS.outConsole("execute: "+query.queryName);
			DBMS.executor.execute(query);
		}
		else DBMS.outConsole("query fail\n----------");
	}
	catch (Error ex)
	{
		System.err.println(ex.getMessage());
		return;
	}
}   	

public void endParse(){
	this.executor.cleanUp();
}

public static void pause(){
	DBMS.outConsole("Press Enter to continue.");
	try{
		System.in.read();
	}catch(Exception e){};
}
	
	
}

start
:
	(
		instructions SCOL
	)* EOF
;

instructions @init { inValid = false;}
:
	create_table
	{execute($create_table.query);}

	| insert_into
	{execute($insert_into.query);}

	| select_from
	{//execute($select_from.query);
	}

;

create_table returns [Query query]
locals [
	 	String tableName,
		String attrName,
		int lengthToken
	]
:
	CREATE TABLE table_name
	{ 
			$tableName = $table_name.value;
			DBMS.outConsole("create "+$tableName);
		}

	LPARSE attribute_list RPARSE
	{
			//DBMS.outConsole("query: create_table start");
			if(!inValid){
				$query = new Create(
						$tableName, 
						$attribute_list.r_attrList, 
						$attribute_list.r_primaryList, 
						$attribute_list.r_attrPosTable
				);
				/* DEBUG
				Hashtable <String, Integer> tempP = $attribute_list.r_attrPosTable;
				ArrayList <Attribute> temp = $attribute_list.r_attrList;
				for(Attribute s : temp){
					DBMS.outConsole("pos: "+s.getName()+"\t->"+tempP.get(s.getName()).toString());
				}	
				 */
			}
		}

;

attribute_list returns [
		ArrayList <Attribute> r_attrList,
		Hashtable <String, Integer> r_attrPosTable,
		ArrayList <Integer> r_primaryList 
	]
	locals [
		Attribute _attribute,
		ArrayList <Attribute> attrList, 
		Hashtable <String, Integer> attrPosTable, // attribute position 
		ArrayList <Integer> primaryList 
	] 
	@init {
		$attrList = new ArrayList <Attribute>();
		$attrPosTable = new Hashtable <String, Integer>();
		$primaryList = new ArrayList <Integer> ();
	}
	@after {
		$r_attrList = $attrList;
		$r_attrPosTable = $attrPosTable;
		$r_primaryList = $primaryList;
	}
:
	attribute
	(
		COMMA attribute
	)*
	|
	(
		attribute COMMA
	)* primary_key
	(
		COMMA attribute
	)*
;

attribute @init {
		Attribute _attribute = $attribute_list::_attribute;
	}
:
	colomn_name types
	{
			String _attrName = $colomn_name.value;
			_attribute = new Attribute(
				$types.type,
				_attrName,
				$types.lengthToken
			);
			
			
			// put attributes
			if (!$attribute_list::attrList.contains(_attribute)) {
				// add attribute
				$attribute_list::attrList.add(_attribute);
				
				// save position of attribute with ArrayList<String, Integer> 
				$attribute_list::attrPosTable.put(
					_attrName, 
					Integer.valueOf($attribute_list::attrList.size())-1
				);
				
				DBMS.outConsole("fetch colomn name: " + _attrName+" "+$types.lengthToken);
				if(!$attribute_list::attrPosTable.containsKey(_attrName))
					DBMS.outConsole("X");
			}else{
				inValid=true;
				DBMS.outConsole("CREATE TABLE: DUPLICATED ATTRIBUTES");
			}
		}

;

primary_key @init {
		Attribute _attribute = $attribute_list::_attribute;
	}
:
	colomn_name types PRIMARY KEY
	{
		String _attrName = $colomn_name.value; // temporary local variable
		_attribute = new Attribute(
			$types.type,
			$colomn_name.value
		);
		
		// check attribute list not empty
		if ($attribute_list::attrList== null) {	
			$attribute_list::attrList = new ArrayList<Attribute>();
		}
		
		// put attributes
		if (!$attribute_list::attrList.contains(_attribute)){
			// add into attribute list
			$attribute_list::attrList.add(_attribute);
			
			// save position of attribute with ArrayList<String, Integer> 
			if($attribute_list::attrPosTable!= null)
			{
				$attribute_list::attrPosTable.put(
					_attrName,
					Integer.valueOf($attribute_list::attrList.size())-1
				);
				
				DBMS.outConsole("fetch colomn name: " + _attrName+" "+$types.lengthToken+"| PrimaryKey");
				if(!$attribute_list::attrPosTable.containsKey(_attrName))
					DBMS.outConsole("X");
			}
			else DBMS.outConsole("#### save attr position fail");
		}
		else{
			inValid = true;
			DBMS.outConsole("CREATE TABLE: DUPLICATED ATTRIBUTES");
		}
		
		// deal with primary key
		Integer position = $attribute_list::attrPosTable.get(_attrName);
		//DBMS.outConsole("PKey position: "+Integer.toString(position));
		
		if (!$attribute_list::primaryList.contains(position)) {
			//save position of attribute in primary list
			$attribute_list::primaryList.add(position);
			DBMS.outConsole("Add Primary Key: "+_attrName);
		}
		else{
			 inValid = true;
			DBMS.outConsole("CREATE TABLE: DUPLICATE PRIMARY KEY " + $colomn_name.value);
		}
		
	}

;

types returns [
		Attribute.Type type,
		int lengthToken
	]
:
	INT
	{
			$type = Attribute.Type.INT;
			$lengthToken = 0;
		}

	| VARCHAR length
	{
			$type = Attribute.Type.CHAR;
			if($length.lengthToken >0)
				$lengthToken = $length.lengthToken;
		}

;

length returns [int lengthToken]
:
	LPARSE INT_IDENTI
	{ $lengthToken = $INT_IDENTI.int;}

	RPARSE
;

insert_into returns [Query query]
	locals [
		Table table,
	] @init {
		 //iterator for List <int> attrPosition 
		int i = 0;
		int tempPosition;
		ArrayList <String> valueList = new ArrayList <String>();
		String tableName = "";
	}
	@after {
		if(!inValid){
			// check primary key not null
			ArrayList<Integer> pList = $table.getPrimaryList();
			boolean legal = true;
			for(i=0; i<pList.size(); i++){
				//DBMS.outConsole("PrimaryKey: "+Integer.toString(pList.get(i))+"-"+valueList.get(pList.get(i)));
				if(valueList.get(pList.get(i))==null){
					legal = false;
					DBMS.outConsole("PrimaryKey null: "+Integer.toString(i)+"-"+valueList.get(pList.get(i)));
					break;
				}
			}
			// create query
			if(legal){
				$query = new Insert(
					tableName,
					valueList
				);
				DBMS.outConsole("Insert Into "+tableName);
				DBMS.outConsole("Value:");
				for(int j=0; j<valueList.size(); j++){
					DBMS.outConsole(Integer.toString(j)+": "+valueList.get(j));
				}	
			}
		}
	}
:
	INSERT INTO // insert without column declare
	table_name
	{
			tableName = $table_name.value;
			
			// fetch table from hash
			try{
				$table = executor.getTableByName(tableName);
			}catch(Exception e){}
			
			if($table==null){
				inValid = true;
				DBMS.outConsole("INSERT: NO SUCH TABLE");
			}
		}

	VALUES LPARSE consts
	{	valueList.add($consts.value);}

	(
		COMMA consts
		{	valueList.add($consts.value);}

	)* RPARSE
	| INSERT INTO table_name
	{ // insert with column declare
			tableName = $table_name.value;
			
			// fetch table form hash
			try{
				$table = executor.getTableByName(tableName);
			}catch(Exception e){}
			
			// check table exist, and initialize valueList to collect values
			if($table==null){
				inValid = true;
				DBMS.outConsole("INSERT: NO SUCH TABLE");
			}else{
				// initialize list with size space
				for(int K=0; K < $table.getAttrList().size(); K++){
					valueList.add(K,"");
				}
				//DBMS.outConsole(Integer.toString(valueList.size()));
			}
		}

	colomn_declare VALUES LPARSE consts
	{
			if(!inValid){
				// pop attribute position
				tempPosition = $colomn_declare.attrPosition.get(i++);
				
				// set by position
				valueList.set(tempPosition, $consts.value); 
			}
		}

	(
		COMMA consts
		{
			if(!inValid){
				// pop attribute position
				tempPosition = $colomn_declare.attrPosition.get(i++);
				
				// set by position
				valueList.set(tempPosition, $consts.value); 
			} 
		}

	)* RPARSE
;

colomn_declare returns [
		List <Integer> attrPosition //return manual input position of values
	] @init {
		$attrPosition = new ArrayList<Integer>();
		Hashtable <String, Integer> attrPosTable = null;
		
		if( $insert_into::table!=null){
			attrPosTable = $insert_into::table.getAttrPosHashtable();
			DBMS.outConsole("check table: "+$insert_into::table.getTableName());
		}else {
			DBMS.outConsole("insert_into::table null");
		}
		
	}
:
	LPARSE colomn_name
	{
			if(attrPosTable!=null){
				int i;
				if(attrPosTable.containsKey($colomn_name.value)){
					i = attrPosTable.get($colomn_name.value);
					$attrPosition.add(i); 
					DBMS.outConsole("declare column: "+$colomn_name.value+" # "+i);
				}else{
					inValid = true;
					throw new Error("INSERT: NO SUCH ATTRIBUTE: "+$colomn_name.value);
				}
			}
		}

	(
		COMMA colomn_name
		{
	 		if(attrPosTable!=null){
				int i;
				if(attrPosTable.containsKey($colomn_name.value)){
					i = attrPosTable.get($colomn_name.value);
					$attrPosition.add(i); 
					DBMS.outConsole("declare column: "+$colomn_name.value+" # "+i);
				}else{
					inValid = true;
					throw new Error("INSERT: NO SUCH ATTRIBUTE: "+$colomn_name.value);
				}
			}
	 	}

	)* RPARSE
;

select_from returns [Query query]
	locals [
		Map<String, ArrayList<String>> tableNameToAttrList,
		
//		AliasToReal used to transform Alias Table name to real table name
//		Select S.studentId  S is the Alias 
//		but S defines at From clause 
//		how can we infer the real name before we parse to From clause?
//		also Where clause will call it again
//		and how to call this table
	
		Map<String, String> RealToAlias, /*used to look up real table name */
		Multimap <String, String> tableAndAttr, /*store table(alias or true) name with attribute */
		ArrayList<String> attrNameList, //for first or not specify which
		ArrayList<String> attrNameList2, //for second table
		Condition cond,
		ArrayList<String> tableList //we just have two table to compare
	] 
	@init {
		$tableNameToAttrList = new HashMap<String, ArrayList<String>> ();
		$RealToAlias = new HashMap<String, String> ();  
		$tableAndAttr = ArrayListMultimap.create();
		$cond = null;
		$attrNameList = new ArrayList<String> ();/*for first table */
		$attrNameList2 = new ArrayList<String> ();/*for first table */
		$tableList = new ArrayList<String> ();/*first table is tableList[0]  */ 
		boolean selectAll = false;
	}
: //one table or two
	SELECT colomns (COMMA colomns)*
	
	// if From parse first then we know table and table_alias first
	//then select columns would know which table attributes to put

	FROM tables	(COMMA tables)*
	{
		//store tableAndAttr's attribute to attrlist
		int tableSize = $tableList.size();
		while(tableSize >0)
		{
			String tableName = $tableList.get(tableSize-1);
			ArrayList <String > attrlist = $tableNameToAttrList.get(tableName);
			String alias = $RealToAlias.get(tableName);
			
			if($select_from::tableAndAttr.get(tableName)!=null)
				System.out.println($select_from::tableAndAttr.get(tableName));
			
				
			pause();
			
			attrlist.addAll($select_from::tableAndAttr.get(tableName)); //add collection 
			attrlist.addAll($select_from::tableAndAttr.get(alias));
			tableSize --;//for while condition
		}

	}

	where_clause?
	{
			/*
			 * if just one table attributes  store in first
			 * if two table attributes could all store in first if no alias specify
			 * but if alias specify then we store attributes in first or two based on tableName
			 */
			 $query = new Select($attrNameList, $attrNameList2, $tableList, $cond);	 
			 
		}

	|
	//only one table
	SELECT COUNT LPARSE colomn_tail RPARSE
	{
		$attrNameList.add($colomn_tail.value);
	}
	/*
	 *value could be 1 attribute or Star
	 */
	FROM tables where_clause?
	{
		$cond = $where_clause.cond;
	}

	{
		if($colomn_tail.value.equals("*")){
			$query = new Select($tableList,$cond, true,0);
		}
		else{
			$query = new Select($attrNameList,$tableList, $cond,0); //list will only store one attribute
		}
			 
	}

	|
	SELECT SUM LPARSE colomn_tail RPARSE
	{
		$attrNameList.add($colomn_tail.value);
	}

	FROM tables where_clause?
	{$cond = $where_clause.cond;}

	{
		if($colomn_tail.value.equals("*")){
			$query = new Select($tableList,$cond, true,1);
		}
		else //list will only store one attribute
		{
			$query = new Select($attrNameList,$tableList, $cond,1);
		}
	}

;

/* store attributes based on table name 
	 if not specify which table store in the attrNameList*/
colomns
	locals [	
		//String tableName,
		//String tableAliasName, 
		//String colomnName
	]
	@init{
		String tableName = null;
		String tableAliasName = null; 
		String colomnName; 
		}
:
	(
		table_name
		| table_alias_name DOT
		{
			System.out.println(tableName+ tableAliasName);
			tableName = $table_name.value;
			tableAliasName = $table_alias_name.value;
		}
	)? colomn_tail
	{
   			colomnName = $colomn_tail.value;
   			if(tableName ==null && tableAliasName == null)
   			   	$select_from::attrNameList.add(colomnName); //attribute w/o table or alias Name
   			else if(tableName!=null)
   				$select_from::tableAndAttr.put(tableName, colomnName);
   			else if(tableAliasName!= null)
   				$select_from::tableAndAttr.put(tableAliasName, colomnName);

   		}

;

colomn_tail returns [String value]
:
	x = colomn_name
	{$value = $x.value;}

	| y = STAR
	{$value = $y.text;}

;

tables
:
	table_name{
		$select_from::tableList.add($table_name.value);
	}
	(AS table_alias_name {
			$select_from::RealToAlias.put($table_name.value,$table_alias_name.value);
			$select_from::tableList.add($table_name.value);
			if($select_from::tableNameToAttrList.size()==0){
				$select_from::tableNameToAttrList.put($table_name.value, $select_from::attrNameList);
			}
			else if( !$select_from::tableNameToAttrList.containsKey($table_name.value)
					&& $select_from::tableNameToAttrList.size()==1)
			{
				$select_from::tableNameToAttrList.put($table_name.value, $select_from::attrNameList2);
			}
			
		}
	)?
	{
		//if($table_alias_name.value != null)
	}

;

where_clause returns [Condition cond]
	locals [
	 	Exp left,
	 	Exp right
	 ] @init {
	 	$left = null;
	 	$right = null;
	 }
	@after {
	 	$cond = new Condition($left);
	 }
:
	WHERE bool_expr
	{$left = $bool_expr.exp;}

	(
		logical_op bool_expr
		{$right = $bool_expr.exp;}

	)?
	{
			//only 1 bool_expr
			if($right != null)
				$left =  new BinaryExp($left, $logical_op.text,$right);
	}

;

logical_op returns [String value]
:
	x =
	(
		AND
		| OR
	)
	{$value = new String($x.text);}

;

/*
 * take out ()? from (compare operand )?
 * we don't know the value type is string or integer yet
 */
bool_expr returns [Exp exp]
locals [
		Exp leftExp,
		Exp rightExp
	] @after {
		$exp = $leftExp;
	}
:
	operand compare operand
	{	
		$leftExp = $operand.exp;
		$rightExp = $operand.exp;
		$leftExp = new BinaryExp($leftExp, $compare.text, $rightExp);
	}

;


//operand's table_alias_name is not necessary to be alias
//could be table real name 


operand returns [Exp exp]
locals [String tableAlias, String colomnName] @after {	
	}
:
	(
		table_alias_name DOT
	)? colomn_name
	{
		$tableAlias = $table_alias_name.value;
		$colomnName = $colomn_name.value;
		if($tableAlias != null) // alias will not used in this demo

//fail to call select_from:: here
//tableAlias could be real table name, maybe transform it in executor?
			
			$exp = new ColExp($tableAlias, $colomnName); 
		else
		$exp = new StrExp($colomnName);
	}

	| type_int
	{$exp = new IntExp(Integer.parseInt($type_int.value));}

;

consts returns [String value]
:
	x = type_int
	{$value = $x.value; }

	| z = type_varchar
	{$value = $z.value;}

	|
	{
		// null value
		
	}

;

compare
:
	LT
	| GT
	| EQ
	| NOT_EQ
;

op
:
	PLUS
	| MINUS
	| STAR
	| DIV
;

colomn_name returns [String value]
:
	x = IDENTIFIER
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}

;

colomn_alias_name returns [String value]
:
	x = IDENTIFIER
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}

;

table_name returns [String value]
:
	x = IDENTIFIER
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}

;

table_alias_name returns [String value]
:
	x = IDENTIFIER
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}

;

type_int returns [String value]
:
	x = INT_IDENTI
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}

;

type_varchar returns [String value]
:
	x = VARCHAR_IDENTI
	{
		String temp = new String($x.text);
		if(temp.equals("''")){
			$value = "";
			DBMS.dump($value);
		}
		else
		{
			String[] split = temp.split("\'");
			$value = split[1];
			DBMS.dump($value);
		}
		
	}

;

ALTER
:
	A L T E R
;

AS
:
	A S
	{ DBMS.dump("AS");}

;

CREATE
:
	C R E A T E
	{ DBMS.dump("CREATE");}

;

COUNT
:
	C O U N T
	{ DBMS.dump("COUNT");}

;

DATABASE
:
	D A T A B A S E
;

DELETE
:
	D E L E T E
;

DOUBLE
:
	D O U B L E
;

DROP
:
	D R O P
;

EXISTS
:
	E X I S T S
;

FOREIGN
:
	F O R E I G N
;

FROM
:
	F R O M
	{ DBMS.dump("FROM");}

;

IF
:
	I F
;

IN
:
	I N
;

INSERT
:
	I N S E R T
	{ DBMS.dump("INSERT");}

;

INT
:
	I N T
	{ DBMS.dump("INT");}

;

INTO
:
	I N T O
	{ DBMS.dump("INTO");}

;

KEY
:
	K E Y
	{ DBMS.dump("KEY");}

;

NOT
:
	N O T
;

NULL
:
	N U L L
;

ON
:
	O N
;

PRIMARY
:
	P R I M A R Y
	{ DBMS.dump("PRIMARY");}

;

REFERENCES
:
	R E F E R E N C E S
;

SELECT
:
	S E L E C T
	{ DBMS.dump("SELECT");}

;

SET
:
	S E T
;

SUM
:
	S U M
	{DBMS.dump("SUM");}

;

VARCHAR
:
	V A R C H A R
	{ DBMS.dump("VARCHAR");}

;

TABLE
:
	T A B L E
	{ DBMS.dump("TABLE");}

;

UPDATE
:
	U P D A T E
;

USE
:
	U S E
;

VALUES
:
	V A L U E S
	{ DBMS.dump("VALUES");}

;

WHERE
:
	W H E R E
	{ DBMS.dump("WHERE");}

;

AND
:
	A N D
	{ DBMS.dump("AND");}

;

OR
:
	O R
	{ DBMS.dump("OR");}

;

IDENTIFIER
:
	[a-zA-Z_] [a-zA-Z_0-9]*
;

INT_IDENTI
:
	DIGIT+
;

DOUBLE_IDENTI
:
	(
		DIGIT+
		(
			'.' DIGIT
		)?
	)
	|
	(
		(
			DIGIT
		)* '.' DIGIT
	)
;

VARCHAR_IDENTI
:
	(
		'\''
	) ~[\r\n'\'']*
	(
		'\''
	)
;

SINGLE_LINE_COMMENT
:
	'--' ~[\r\n]* -> skip
;

MULTILINE_COMMENT
:
	'/*' .*?
	(
		'*/'
		| EOF
	) -> skip
;

TABS
:
	[\t\u000B]
	{DBMS.dump("\t");}

	-> skip
;

SPACE
:
	[ ]
	{DBMS.dump(" ");}

	-> skip
;

NEWLINE
:
	'\r'? '\n'
	{DBMS.dump("\n");}

	-> skip
;

fragment
DIGIT
:
	[0-9]
;

fragment
A
:
	[aA]
;

fragment
B
:
	[bB]
;

fragment
C
:
	[cC]
;

fragment
D
:
	[dD]
;

fragment
E
:
	[eE]
;

fragment
F
:
	[fF]
;

fragment
G
:
	[gG]
;

fragment
H
:
	[hH]
;

fragment
I
:
	[iI]
;

fragment
J
:
	[jJ]
;

fragment
K
:
	[kK]
;

fragment
L
:
	[lL]
;

fragment
M
:
	[mM]
;

fragment
N
:
	[nN]
;

fragment
O
:
	[oO]
;

fragment
P
:
	[pP]
;

fragment
Q
:
	[qQ]
;

fragment
R
:
	[rR]
;

fragment
S
:
	[sS]
;

fragment
T
:
	[tT]
;

fragment
U
:
	[uU]
;

fragment
V
:
	[vV]
;

fragment
W
:
	[wW]
;

fragment
X
:
	[xX]
;

fragment
Y
:
	[yY]
;

fragment
Z
:
	[zZ]
;

APOS
:
	'\''
;

SCOL
:
	';'
	{DBMS.dump(";");}

;

DOT
:
	'.'
	{DBMS.dump(".");}

;

LPARSE
:
	'('
	{DBMS.dump("(");}

;

RPARSE
:
	')'
	{DBMS.dump(")");}

;

COMMA
:
	','
	{DBMS.dump(",");}

;

STAR
:
	'*'
	{DBMS.dump("*");}

;

PLUS
:
	'+'
;

MINUS
:
	'-'
;

TILDE
:
	'~'
;

PIPE2
:
	'||'
;

DIV
:
	'/'
;

MOD
:
	'%'
;

LSHIFT
:
	'<<'
;

RSHIFT
:
	'>>'
;

AMP
:
	'&'
;

PIPE
:
	'|'
;

LT
:
	'<'
	{DBMS.dump("<");}

;

LT_EQ
:
	'<='
;

GT
:
	'>'
	{DBMS.dump(">");}

;

GT_EQ
:
	'>='
;

EQ
:
	'='
	{DBMS.dump("=");}

;

NOT_EQ
:
	'<>'
	{DBMS.dump("<>");}

;
