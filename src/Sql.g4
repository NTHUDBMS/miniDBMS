grammar Sql;

@header{

package src;
import java.util.*;
import manageDatabase.*;
import structure.*;

}

//-----------------------Grammer Rules------------------------

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
	:	(attribute COMMA)* primary_key (COMMA attribute)*
	;


primary_key
	:	attribute PRIMARY KEY
	;

attribute
	:	colomn_name types
	;

types
	:	(INT|VARCHAR) length?
	;
	
length
	:	LPARSE int_len RPARSE
	;

int_len returns [Object value]
	:	x=type_int {$value = $x.value;}
	;
	

insert_into
	:	INSERT INTO table_name LPARSE colomn_name (COMMA colomn_name)* RPARSE
		VALUES LPARSE consts (COMMA consts)* RPARSE
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
	:	x=IDENTIFIER {$value = new String($x.text);}
	;
  
colomn_alias_name returns [String value]
	:	x=IDENTIFIER {$value = new String($x.text);}
	;
  
table_name returns [String value]
	:	x=IDENTIFIER {$value = new String($x.text);}
	;
  
table_alias_name returns [String value]
	:	x=IDENTIFIER {$value = new String($x.text);}
	;
  
type_int returns [Integer value] 
	:	x=INT_IDENTI {$value = new Integer($x.text);}
	;
  
type_varchar returns [String value] 
	:	x=VARCHAR_IDENTI {$value = new String($x.text);}
	;

ALTER   : A L T E R;
AS      : A S;
CREATE  : C R E A T E;
DATABASE: D A T A B A S E;
DELETE  : D E L E T E;
DOUBLE  : D O U B L E;
DROP    : D R O P;
EXISTS  : E X I S T S;
FOREIGN : F O R E I G N;
FROM    : F R O M;
IF      : I F;
IN      : I N;
INSERT  : I N S E R T;
INT     : I N T;
INTO    : I N T O;
KEY     : K E Y;
NOT     : N O T;
NULL    : N U L L;
ON      : O N;
PRIMARY : P R I M A R Y;
REFERENCES : R E F E R E N C E S;
SELECT  : S E L E C T;
SET     : S E T;
VARCHAR : V A R C H A R;
TABLE   : T A B L E;
UPDATE  : U P D A T E;
USE     : U S E;
VALUES  : V A L U E S;
WHERE   : W H E R E;
AND 	: A N D;
OR  	: O R;

IDENTIFIER
	: [a-zA-Z_][a-zA-Z_0-9]*;
	
INT_IDENTI
    : [0-9]+;
    
DOUBLE_IDENTI
    :  ([0-9]+('.'[0-9])?)|(([0-9])*'.'[0-9]);
    
VARCHAR_IDENTI
    : '\''[^']*'\'';

SINGLE_LINE_COMMENT
	: '--' ~[\r\n]* -> channel(HIDDEN);

MULTILINE_COMMENT
	: '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN);

SPACES
	: [ \t\u000B\t\r\n] -> channel(HIDDEN);

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

SCOL   : ';';
DOT    : '.';
LPARSE : '(';
RPARSE : ')';
COMMA  : ',';
STAR   : '*';
PLUS   : '+';
MINUS  : '-';
TILDE  : '~';
PIPE2  : '||';
DIV    : '/';
MOD    : '%';
LT2    : '<<';
GT2    : '>>';
AMP    : '&';
PIPE   : '|';
LT     : '<';
LT_EQ  : '<=';
GT     : '>';
GT_EQ  : '>=';
EQ     : '=';
NOT_EQ : '<>';