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

public void dumpWhereClause(Exp e){
	if(e instanceof BinaryExp){
		BinaryExp t = (BinaryExp)e;
		dumpWhereClause(t.getLeft());
		System.out.print(t.getOp()+" ");
		dumpWhereClause(t.getRight());
	}else if(e instanceof ColExp){
		ColExp t = (ColExp)e;
		String dumpBuffer = (t.getTableName()!=null)?t.getTableName()+"."+t.getColomnName() :"";
		System.out.print(dumpBuffer+" ");
	}else if(e instanceof StrExp){
		StrExp t = (StrExp)e;
		System.out.print(t.getStr()+" ");
	}else if(e instanceof IntExp){
		IntExp t = (IntExp)e;
		System.out.print(t.toString()+" ");
	}else if(e instanceof IdExp){
		IdExp t = (IdExp)e;
		System.out.print(t.getId()+" ");
	}
}

public class TableStruct{
	public String tableName;
	public String aliasName;
	public TableStruct(String tname, String aname){
		this.tableName = tname;
		this.aliasName = aname;
	}
}

public class ColumnStruct{
	public String tableName;
	public String columnName;
	public ColumnStruct(String tname, String cname){
		this.tableName = tname;
		this.columnName = cname;
	}
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

/////////////////////////////////////////
//
//	Create instruction part
//
/////////////////////////////////////////

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
	column_name types
	{
			String _attrName = $column_name.value;
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
				
				DBMS.outConsole("fetch column name: " + _attrName+" "+$types.lengthToken);
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
	column_name types PRIMARY KEY
	{
		String _attrName = $column_name.value; // temporary local variable
		_attribute = new Attribute(
			$types.type,
			$column_name.value
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
				
				DBMS.outConsole("fetch column name: " + _attrName+" "+$types.lengthToken+"| PrimaryKey");
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
			DBMS.outConsole("CREATE TABLE: DUPLICATE PRIMARY KEY " + $column_name.value);
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

/////////////////////////////////////////
//
//	Insert instruction part
//
/////////////////////////////////////////

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
			
			// fetch table from hash
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

	column_declare VALUES LPARSE consts
	{
			if(!inValid){
				// pop attribute position
				tempPosition = $column_declare.attrPosition.get(i++);
				
				// set by position
				valueList.set(tempPosition, $consts.value); 
			}
		}

	(
		COMMA consts
		{
			if(!inValid){
				// pop attribute position
				tempPosition = $column_declare.attrPosition.get(i++);
				
				// set by position
				valueList.set(tempPosition, $consts.value); 
			} 
		}

	)* RPARSE
;

column_declare returns [List <Integer> attrPosition] 
@init {
	$attrPosition = new ArrayList<Integer>();
	Hashtable <String, Integer> attrPosTable = null;
	
	if( $insert_into::table!=null){
		attrPosTable = $insert_into::table.getAttrPosHashtable();
		DBMS.outConsole("check table: "+$insert_into::table.getTableName());
	}else {
		DBMS.outConsole("insert_into::table null");
	}
	
}
	: LPARSE column_name
	{
		if(attrPosTable!=null){
			int i;
			if(attrPosTable.containsKey($column_name.value)){
				i = attrPosTable.get($column_name.value);
				$attrPosition.add(i); 
				DBMS.outConsole("declare column: "+$column_name.value+" # "+i);
			}else{
				inValid = true;
				throw new Error("INSERT: NO SUCH ATTRIBUTE: "+$column_name.value);
			}
		}
	}
	( COMMA column_name
		{
	 		if(attrPosTable!=null){
				int i;
				if(attrPosTable.containsKey($column_name.value)){
					i = attrPosTable.get($column_name.value);
					$attrPosition.add(i); 
					DBMS.outConsole("declare column: "+$column_name.value+" # "+i);
				}else{
					inValid = true;
					throw new Error("INSERT: NO SUCH ATTRIBUTE: "+$column_name.value);
				}
			}
	 	}
	)* RPARSE
	;
/////////////////////////////////////////
//
//	Select instruction part
//
/////////////////////////////////////////

select_from returns [Query query]
locals [
	
	ArrayList<String> tableList, //we just have two table to compare
	Hashtable<String,String> aliasTalbe,
	Select.Aggregation aggregate
] 
@init {
	$tableList = new ArrayList<String> ();
	$aliasTalbe = new Hashtable<String,String>();
	$aggregate = Select.Aggregation.NON;
}
@after{
	DBMS.outConsole("---------------");
}
	: SELECT clist+=columns (COMMA clist+=columns)*
	  FROM tlist+=tables	(COMMA tlist+=tables)*
	  where_clause
	{
		// one table or two
		ArrayList<String> attrList = new ArrayList<String>();
		ArrayList<Integer> attrTableRelation = new ArrayList<Integer>();
		
		// collect elements, build table list
		for(TablesContext temp : $tlist){
			// check alias avalible, put to hashtable
			if(temp.table.aliasName!=null){
				$aliasTalbe.put(
					temp.table.aliasName, 
					temp.table.tableName
				);
			}
			$tableList.add(temp.table.tableName);
			DBMS.outConsole("target table:\t"+temp.table.tableName);
		}
		
		// collect columns, recognize correspect table
		int tableSelect;
		DBMS.outConsole("target columns:");
		for(ColumnsContext temp : $clist){
			tableSelect = 0;
			if(temp.col.tableName!=null){
				// search respect table
				if($aliasTalbe.size()!=0){ // have alias
					if($aliasTalbe.containsKey(temp.col.tableName)){
						String tname = $aliasTalbe.get(temp.col.tableName);
						tableSelect = $tableList.indexOf(tname);
					}
					else
					{
						throw new Error("Error alias name");
					}	
				}
				else // don't have alias
				{
					tableSelect = $tableList.indexOf(temp.col.tableName);
				}
			}
			
			attrList.add(temp.col.columnName);
			attrTableRelation.add(tableSelect);
			
			String dumpBuffer = (temp.col.tableName!=null)?temp.col.tableName+"." :"";
			DBMS.outConsole("\t\t"+dumpBuffer+temp.col.columnName+
					" _table #"+Integer.toString(tableSelect)
			);
		}
		$query = new Select(
			attrList,
			attrTableRelation, 
			$tableList, 
			$where_clause.cond
		);	
	 
	}
	| SELECT COUNT LPARSE column_content RPARSE
	  FROM tables 
	  where_clause
	{
		// only one table
		ArrayList<String> attrList = new ArrayList<String>();
		attrList.add($column_content.value);
		$tableList.add($tables.table.tableName);
		
		DBMS.outConsole("target table:\t"+$tables.table.tableName);
		DBMS.outConsole("target columns:");
		DBMS.outConsole("\t\tCOUNT("+$column_content.value+")");
		
		$query = new Select(
			attrList,
			$tableList, 
			$where_clause.cond,
			Select.Aggregation.COUNT
		);	
	}
	| SELECT SUM LPARSE column_content RPARSE
	  FROM tables 
	  where_clause
	{
		ArrayList<String> attrList = new ArrayList<String>();
		attrList.add($column_content.value);
		$tableList.add($tables.table.tableName);
		
		DBMS.outConsole("target table:\t"+$tables.table.tableName);
		DBMS.outConsole("target columns:");
		DBMS.outConsole("\t\tSUM("+$column_content.value+")");
		
		$query = new Select(
			attrList,
			$tableList, 
			$where_clause.cond,
			Select.Aggregation.SUM
		);	
	}

;


columns returns[ColumnStruct col]
@init{
	String tname = null;
}
	: (table_alias_name DOT	{tname = $table_alias_name.value;} )? column_content
	{
		$col = new ColumnStruct(tname, $column_content.value);
	}
	;

column_content returns [String value]
	: x = column_name {$value = $x.value;}
	| y = STAR {$value = $y.text;}
	;


tables returns[TableStruct table]
@init{
	String aname = null;
}
:
	table_name (AS table_alias_name	{aname = $table_alias_name.value;} )?
	{
		$table = new TableStruct(
			$table_name.value,
			aname
		);
	}
	;

where_clause returns [Condition cond]
	: WHERE bool_expr logical_op bool_expr2
	{
		// if only one restriction
		BinaryExp temp = new BinaryExp(
			$bool_expr.exp, 
			$logical_op.text, 
			$bool_expr2.exp
		);
		dumpWhereClause(temp);
		System.out.print("\n");
		$cond = new Condition(temp);
	}
	| WHERE bool_expr
	{
		dumpWhereClause($bool_expr.exp);
		System.out.print("\n");
		$cond = new Condition($bool_expr.exp);
	}
	| // empty where clause
	{
		$cond = null;
	}
	;

logical_op returns [String value]
	: x = (AND | OR) {$value = new String($x.text);}
	;

bool_expr2 returns [Exp exp]
	: bool_expr {$exp = $bool_expr.exp;}
	;

bool_expr returns [Exp exp]
	: operand compare operand2
	{ $exp = new BinaryExp($operand.exp, $compare.text, $operand2.exp);}
	;

operand2 returns [Exp exp]
	: operand{ $exp = $operand.exp;}
;

operand returns [Exp exp]
@init{
	String tableAlias = null; 
}
	: (table_alias_name
	{
		tableAlias = $table_alias_name.value;
	} 
	DOT)? column_name
	{
		if(tableAlias != null) 
			$exp = new ColExp(tableAlias, $column_name.value); 
		else
			$exp = new IdExp($column_name.value);
	}
	| x=type_int
	{
		$exp = new IntExp(Integer.parseInt($x.value));
	}
	| y=type_varchar
	{
		$exp = new StrExp($y.value);
	}
	;

consts returns [String value]
	: x = type_int	{$value = $x.value; }
	| z = type_varchar	{$value = $z.value;}
	|
;

compare
	: LT
	| GT
	| EQ
	| NOT_EQ
;

op
	: PLUS
	| MINUS
	| STAR
	| DIV
;

id returns [String value]
:	
	x = IDENTIFIER
	{
		$value = new String($x.text);
		DBMS.dump($x.text);
	}
;

column_name returns [String value]
:
	id {$value = $id.value;}
;

column_alias_name returns [String value]
:
	id {$value = $id.value;}
;

table_name returns [String value]
:
	id {$value = $id.value;}
;

table_alias_name returns [String value]
:
	id {$value = $id.value;}
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
