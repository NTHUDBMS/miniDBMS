grammar Sql;

@header{
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //!!!                                                            !!!
    //!!! THIS CODE IS AUTOMATICALLY GENERATED! DO NOT MODIFY!       !!!
    //!!! Please refer to file Example.g4 for grammar documentation. !!!
    //!!!                                                            !!!
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
import java.util.*;
import java.io.*;
import manageDatabase.*;
import structure.*;
import dbms.*;

}

@members{
	
	private final static int maxTuple = 100;
	private final static int maxAttr = 5;
	private DBExecutor DBE;
	
	public static void pause(){
		System.out.println("Press Enter to continue.");
		try{
			System.in.read();
		}catch(Exception e){};
	}
	
	
}

//-----------------------Grammar Rules------------------------

start
	:	(instructions ';')* EOF
	;

instructions
	:	create_table
	|	select_from
	|	insert_into	
	;


create_table
	:	CREATE TABLE table_name LPARSE attribute_list RPARSE
	;
		
attribute_list
	locals[int i=0]
	:	(attribute COMMA {$i++;})* primary_key {$i++;} (COMMA attribute {$i++;})*
		{$i<=maxAttr}?<fail={"Query exceeded max attribute of "+maxAttr}> // grammar prediction
		{
			
		}
	|	attribute {$i++;} (COMMA attribute {$i++;})*{
			
		}
	;


primary_key
	:	attribute PRIMARY KEY{}
	;

attribute
	:	colomn_name types
	;

types
	:	(INT|VARCHAR) length?{
		
	};
	
length
	:	LPARSE int_len RPARSE{
		
	};

int_len returns [Object value]
	:	x=type_int {$value = $x.value;}
	;
	

insert_into
	locals[int i=1]
	:	INSERT INTO table_name colomn_declare?
		VALUES LPARSE consts (COMMA consts)* RPARSE
	;

colomn_declare
	:	LPARSE colomn_name (COMMA colomn_name)* RPARSE
	;

select_from
	:	SELECT colomns (COMMA colomns)* 
		FROM tables (COMMA tables)*
		where_clause?
	;

colomns
    :	(table_name|table_alias_name DOT)? colomn_tail
    ;
    
colomn_tail returns [Object value]
	:	x = colomn_name {$value = $x.value;}
	|	y = STAR {$value = $y.text;}
	;

tables
	:	table_name (AS table_alias_name)?
	;
	
where_clause
	:	WHERE bool_expr (logical_op bool_expr)?
	;
	
logical_op returns [Object value]
	:	x=(AND|OR) {$value = new String($x.text);}
	;

bool_expr
	:	operand (compare operand)?
    ;
    
operand
	:	(table_alias_name DOT)? colomn_name
	|	consts
	;

consts returns [Object value]
	:	x = type_int {$value = $x.value; }
	|	z = type_varchar {$value = $z.value;}
	;

compare
	:	LT
	|	GT
	|	EQ
	|	NOT_EQ
	;
	
op
	:	PLUS
	|	MINUS
	|	STAR
	|	DIV
	;


colomn_name returns [String value]
	:	x=IDENTIFIER {
		$value = new String($x.text);
		DBMS.dump($x.text);
	};
  
colomn_alias_name returns [String value]
	:	x=IDENTIFIER {
		$value = new String($x.text);
		DBMS.dump($x.text);
	};
  
table_name returns [String value]
	:	x=IDENTIFIER {
		$value = new String($x.text);
		DBMS.dump($x.text);
	};
  
table_alias_name returns [String value]
	:	x=IDENTIFIER {
		$value = new String($x.text);
		DBMS.dump($x.text);
	};
  
type_int returns [Integer value] 
	:	x=INT_IDENTI {
		$value = new Integer($x.text);
		DBMS.dump($x.text);
	};
  
type_varchar returns [String value] 
	:	x=VARCHAR_IDENTI {
		$value = new String($x.text);
		DBMS.dump($x.text);
	};

ALTER   : A L T E R;
AS      : A S              { DBMS.dump("AS");};
CREATE  : C R E A T E      { DBMS.dump("CREATE");};
DATABASE: D A T A B A S E;
DELETE  : D E L E T E;
DOUBLE  : D O U B L E;
DROP    : D R O P;
EXISTS  : E X I S T S;
FOREIGN : F O R E I G N;
FROM    : F R O M          { DBMS.dump("FROM");};
IF      : I F;
IN      : I N;
INSERT  : I N S E R T      { DBMS.dump("INSERT");};
INT     : I N T            { DBMS.dump("INT");};
INTO    : I N T O          { DBMS.dump("INTO");};
KEY     : K E Y            { DBMS.dump("KEY");};
NOT     : N O T;
NULL    : N U L L;
ON      : O N;
PRIMARY : P R I M A R Y    { DBMS.dump("PRIMARY");};
REFERENCES: R E F E R E N C E S;
SELECT  : S E L E C T      { DBMS.dump("SELECT");};
SET     : S E T;
VARCHAR : V A R C H A R    { DBMS.dump("VARCHAR");};
TABLE   : T A B L E        { DBMS.dump("TABLE");};
UPDATE  : U P D A T E;
USE     : U S E;
VALUES  : V A L U E S      { DBMS.dump("VALUES");};
WHERE   : W H E R E        { DBMS.dump("WHERE");};
AND 	: A N D            { DBMS.dump("AND");};
OR  	: O R              { DBMS.dump("OR");};

IDENTIFIER
	: [a-zA-Z_][a-zA-Z_0-9]*;
	
INT_IDENTI
    : DIGIT+;
    
DOUBLE_IDENTI
    :	(DIGIT+('.'DIGIT)?)
    |	((DIGIT)*'.'DIGIT);
    
VARCHAR_IDENTI
    : ('\'')~[\r\n]*('\'');

SINGLE_LINE_COMMENT
	: '--' ~[\r\n]* -> channel(HIDDEN);

MULTILINE_COMMENT
	: '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN);

TABS
	: [\t\u000B] {DBMS.dump("\t");} -> channel(HIDDEN);
	
SPACE
	: [ ] {DBMS.dump(" ");} -> channel(HIDDEN);
	
NEWLINE
	: '\r'?'\n'  {DBMS.dump("\n");} -> channel(HIDDEN);

fragment DIGIT : [0-9];
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

SCOL   : ';' {DBMS.dump(";");};
DOT    : '.' {DBMS.dump(".");};
LPARSE : '(' {DBMS.dump("(");};
RPARSE : ')' {DBMS.dump(")");};
COMMA  : ',' {DBMS.dump(",");};
STAR   : '*' {DBMS.dump("*");};
PLUS   : '+';
MINUS  : '-';
TILDE  : '~';
PIPE2  : '||';
DIV    : '/';
MOD    : '%';
LSHIFT : '<<';
RSHIFT : '>>';
AMP    : '&';
PIPE   : '|';
LT     : '<' {DBMS.dump("<");};
LT_EQ  : '<=';
GT     : '>' {DBMS.dump(">");};
GT_EQ  : '>=';
EQ     : '=' {DBMS.dump("=");};
NOT_EQ : '<>' {DBMS.dump("<>");};