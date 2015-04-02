// Generated from Sql.g4 by ANTLR 4.4


package src;
import java.util.*;
import manageDatabase.*;
import structure.*;


import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ALTER=1, AS=2, CREATE=3, DATABASE=4, DELETE=5, DOUBLE=6, DROP=7, EXISTS=8, 
		FOREIGN=9, FROM=10, IF=11, IN=12, INSERT=13, INT=14, INTO=15, KEY=16, 
		NOT=17, NULL=18, ON=19, PRIMARY=20, REFERENCES=21, SELECT=22, SET=23, 
		VARCHAR=24, TABLE=25, UPDATE=26, USE=27, VALUES=28, WHERE=29, AND=30, 
		OR=31, IDENTIFIER=32, INT_IDENTI=33, DOUBLE_IDENTI=34, VARCHAR_IDENTI=35, 
		SINGLE_LINE_COMMENT=36, MULTILINE_COMMENT=37, SPACES=38, SCOL=39, DOT=40, 
		LPARSE=41, RPARSE=42, COMMA=43, STAR=44, PLUS=45, MINUS=46, TILDE=47, 
		PIPE2=48, DIV=49, MOD=50, LT2=51, GT2=52, AMP=53, PIPE=54, LT=55, LT_EQ=56, 
		GT=57, GT_EQ=58, EQ=59, NOT_EQ=60;
	public static final String[] tokenNames = {
		"<INVALID>", "ALTER", "AS", "CREATE", "DATABASE", "DELETE", "DOUBLE", 
		"DROP", "EXISTS", "FOREIGN", "FROM", "IF", "IN", "INSERT", "INT", "INTO", 
		"KEY", "NOT", "NULL", "ON", "PRIMARY", "REFERENCES", "SELECT", "SET", 
		"VARCHAR", "TABLE", "UPDATE", "USE", "VALUES", "WHERE", "AND", "OR", "IDENTIFIER", 
		"INT_IDENTI", "DOUBLE_IDENTI", "VARCHAR_IDENTI", "SINGLE_LINE_COMMENT", 
		"MULTILINE_COMMENT", "SPACES", "';'", "'.'", "'('", "')'", "','", "'*'", 
		"'+'", "'-'", "'~'", "'||'", "'/'", "'%'", "'<<'", "'>>'", "'&'", "'|'", 
		"'<'", "'<='", "'>'", "'>='", "'='", "'<>'"
	};
	public static final int
		RULE_start = 0, RULE_instructions = 1, RULE_create_table = 2, RULE_attribute_list = 3, 
		RULE_primary_key = 4, RULE_attribute = 5, RULE_types = 6, RULE_length = 7, 
		RULE_int_len = 8, RULE_insert_into = 9, RULE_select_from = 10, RULE_colomns = 11, 
		RULE_colomn_tail = 12, RULE_tables = 13, RULE_where_clause = 14, RULE_logical_op = 15, 
		RULE_bool_expr = 16, RULE_operand = 17, RULE_consts = 18, RULE_compare = 19, 
		RULE_op = 20, RULE_colomn_name = 21, RULE_colomn_alias_name = 22, RULE_table_name = 23, 
		RULE_table_alias_name = 24, RULE_type_int = 25, RULE_type_varchar = 26;
	public static final String[] ruleNames = {
		"start", "instructions", "create_table", "attribute_list", "primary_key", 
		"attribute", "types", "length", "int_len", "insert_into", "select_from", 
		"colomns", "colomn_tail", "tables", "where_clause", "logical_op", "bool_expr", 
		"operand", "consts", "compare", "op", "colomn_name", "colomn_alias_name", 
		"table_name", "table_alias_name", "type_int", "type_varchar"
	};

	@Override
	public String getGrammarFileName() { return "Sql.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public InstructionsContext instructions(int i) {
			return getRuleContext(InstructionsContext.class,i);
		}
		public TerminalNode EOF() { return getToken(SqlParser.EOF, 0); }
		public List<InstructionsContext> instructions() {
			return getRuleContexts(InstructionsContext.class);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CREATE) | (1L << INSERT) | (1L << SELECT))) != 0)) {
				{
				{
				setState(54); instructions();
				setState(55); match(SCOL);
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(62); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionsContext extends ParserRuleContext {
		public Insert_intoContext insert_into() {
			return getRuleContext(Insert_intoContext.class,0);
		}
		public Create_tableContext create_table() {
			return getRuleContext(Create_tableContext.class,0);
		}
		public Select_fromContext select_from() {
			return getRuleContext(Select_fromContext.class,0);
		}
		public InstructionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instructions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterInstructions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitInstructions(this);
		}
	}

	public final InstructionsContext instructions() throws RecognitionException {
		InstructionsContext _localctx = new InstructionsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instructions);
		try {
			setState(67);
			switch (_input.LA(1)) {
			case CREATE:
				enterOuterAlt(_localctx, 1);
				{
				setState(64); create_table();
				}
				break;
			case SELECT:
				enterOuterAlt(_localctx, 2);
				{
				setState(65); select_from();
				}
				break;
			case INSERT:
				enterOuterAlt(_localctx, 3);
				{
				setState(66); insert_into();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Create_tableContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode LPARSE() { return getToken(SqlParser.LPARSE, 0); }
		public TerminalNode RPARSE() { return getToken(SqlParser.RPARSE, 0); }
		public TerminalNode CREATE() { return getToken(SqlParser.CREATE, 0); }
		public TerminalNode TABLE() { return getToken(SqlParser.TABLE, 0); }
		public Attribute_listContext attribute_list() {
			return getRuleContext(Attribute_listContext.class,0);
		}
		public Create_tableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_table; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCreate_table(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCreate_table(this);
		}
	}

	public final Create_tableContext create_table() throws RecognitionException {
		Create_tableContext _localctx = new Create_tableContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_create_table);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69); match(CREATE);
			setState(70); match(TABLE);
			setState(71); table_name();
			setState(72); match(LPARSE);
			setState(73); attribute_list();
			setState(74); match(RPARSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attribute_listContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public Primary_keyContext primary_key() {
			return getRuleContext(Primary_keyContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlParser.COMMA); }
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(SqlParser.COMMA, i);
		}
		public Attribute_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterAttribute_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitAttribute_list(this);
		}
	}

	public final Attribute_listContext attribute_list() throws RecognitionException {
		Attribute_listContext _localctx = new Attribute_listContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_attribute_list);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(76); attribute();
					setState(77); match(COMMA);
					}
					} 
				}
				setState(83);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(84); primary_key();
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(85); match(COMMA);
				setState(86); attribute();
				}
				}
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primary_keyContext extends ParserRuleContext {
		public AttributeContext attribute() {
			return getRuleContext(AttributeContext.class,0);
		}
		public TerminalNode PRIMARY() { return getToken(SqlParser.PRIMARY, 0); }
		public TerminalNode KEY() { return getToken(SqlParser.KEY, 0); }
		public Primary_keyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_key; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterPrimary_key(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitPrimary_key(this);
		}
	}

	public final Primary_keyContext primary_key() throws RecognitionException {
		Primary_keyContext _localctx = new Primary_keyContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_primary_key);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); attribute();
			setState(93); match(PRIMARY);
			setState(94); match(KEY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public Colomn_nameContext colomn_name() {
			return getRuleContext(Colomn_nameContext.class,0);
		}
		public TypesContext types() {
			return getRuleContext(TypesContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); colomn_name();
			setState(97); types();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypesContext extends ParserRuleContext {
		public LengthContext length() {
			return getRuleContext(LengthContext.class,0);
		}
		public TerminalNode INT() { return getToken(SqlParser.INT, 0); }
		public TerminalNode VARCHAR() { return getToken(SqlParser.VARCHAR, 0); }
		public TypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_types; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTypes(this);
		}
	}

	public final TypesContext types() throws RecognitionException {
		TypesContext _localctx = new TypesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_types);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==VARCHAR) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(101);
			_la = _input.LA(1);
			if (_la==LPARSE) {
				{
				setState(100); length();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LengthContext extends ParserRuleContext {
		public Int_lenContext int_len() {
			return getRuleContext(Int_lenContext.class,0);
		}
		public TerminalNode LPARSE() { return getToken(SqlParser.LPARSE, 0); }
		public TerminalNode RPARSE() { return getToken(SqlParser.RPARSE, 0); }
		public LengthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_length; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterLength(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitLength(this);
		}
	}

	public final LengthContext length() throws RecognitionException {
		LengthContext _localctx = new LengthContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_length);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103); match(LPARSE);
			setState(104); int_len();
			setState(105); match(RPARSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Int_lenContext extends ParserRuleContext {
		public Object value;
		public Type_intContext x;
		public Type_intContext type_int() {
			return getRuleContext(Type_intContext.class,0);
		}
		public Int_lenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_int_len; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterInt_len(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitInt_len(this);
		}
	}

	public final Int_lenContext int_len() throws RecognitionException {
		Int_lenContext _localctx = new Int_lenContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_int_len);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107); ((Int_lenContext)_localctx).x = type_int();
			((Int_lenContext)_localctx).value =  ((Int_lenContext)_localctx).x.value;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Insert_intoContext extends ParserRuleContext {
		public TerminalNode RPARSE(int i) {
			return getToken(SqlParser.RPARSE, i);
		}
		public List<ConstsContext> consts() {
			return getRuleContexts(ConstsContext.class);
		}
		public List<Colomn_nameContext> colomn_name() {
			return getRuleContexts(Colomn_nameContext.class);
		}
		public TerminalNode LPARSE(int i) {
			return getToken(SqlParser.LPARSE, i);
		}
		public TerminalNode VALUES() { return getToken(SqlParser.VALUES, 0); }
		public ConstsContext consts(int i) {
			return getRuleContext(ConstsContext.class,i);
		}
		public List<TerminalNode> RPARSE() { return getTokens(SqlParser.RPARSE); }
		public TerminalNode COMMA(int i) {
			return getToken(SqlParser.COMMA, i);
		}
		public TerminalNode INTO() { return getToken(SqlParser.INTO, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlParser.COMMA); }
		public List<TerminalNode> LPARSE() { return getTokens(SqlParser.LPARSE); }
		public Colomn_nameContext colomn_name(int i) {
			return getRuleContext(Colomn_nameContext.class,i);
		}
		public TerminalNode INSERT() { return getToken(SqlParser.INSERT, 0); }
		public Insert_intoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert_into; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterInsert_into(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitInsert_into(this);
		}
	}

	public final Insert_intoContext insert_into() throws RecognitionException {
		Insert_intoContext _localctx = new Insert_intoContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_insert_into);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110); match(INSERT);
			setState(111); match(INTO);
			setState(112); table_name();
			setState(113); match(LPARSE);
			setState(114); colomn_name();
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(115); match(COMMA);
				setState(116); colomn_name();
				}
				}
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(122); match(RPARSE);
			setState(123); match(VALUES);
			setState(124); match(LPARSE);
			setState(125); consts();
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(126); match(COMMA);
				setState(127); consts();
				}
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(133); match(RPARSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_fromContext extends ParserRuleContext {
		public List<ColomnsContext> colomns() {
			return getRuleContexts(ColomnsContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(SqlParser.COMMA); }
		public Where_clauseContext where_clause() {
			return getRuleContext(Where_clauseContext.class,0);
		}
		public TerminalNode FROM() { return getToken(SqlParser.FROM, 0); }
		public List<TablesContext> tables() {
			return getRuleContexts(TablesContext.class);
		}
		public TerminalNode SELECT() { return getToken(SqlParser.SELECT, 0); }
		public TablesContext tables(int i) {
			return getRuleContext(TablesContext.class,i);
		}
		public ColomnsContext colomns(int i) {
			return getRuleContext(ColomnsContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(SqlParser.COMMA, i);
		}
		public Select_fromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_from; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSelect_from(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSelect_from(this);
		}
	}

	public final Select_fromContext select_from() throws RecognitionException {
		Select_fromContext _localctx = new Select_fromContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_select_from);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135); match(SELECT);
			setState(136); colomns();
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(137); match(COMMA);
				setState(138); colomns();
				}
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(144); match(FROM);
			setState(145); tables();
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(146); match(COMMA);
				setState(147); tables();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(154);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(153); where_clause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColomnsContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(SqlParser.DOT, 0); }
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Colomn_tailContext colomn_tail() {
			return getRuleContext(Colomn_tailContext.class,0);
		}
		public Table_alias_nameContext table_alias_name() {
			return getRuleContext(Table_alias_nameContext.class,0);
		}
		public ColomnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colomns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterColomns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitColomns(this);
		}
	}

	public final ColomnsContext colomns() throws RecognitionException {
		ColomnsContext _localctx = new ColomnsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_colomns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(156); table_name();
				}
				break;
			case 2:
				{
				setState(157); table_alias_name();
				setState(158); match(DOT);
				}
				break;
			}
			setState(162); colomn_tail();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Colomn_tailContext extends ParserRuleContext {
		public Object value;
		public Colomn_nameContext x;
		public Token y;
		public Colomn_nameContext colomn_name() {
			return getRuleContext(Colomn_nameContext.class,0);
		}
		public TerminalNode STAR() { return getToken(SqlParser.STAR, 0); }
		public Colomn_tailContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colomn_tail; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterColomn_tail(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitColomn_tail(this);
		}
	}

	public final Colomn_tailContext colomn_tail() throws RecognitionException {
		Colomn_tailContext _localctx = new Colomn_tailContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_colomn_tail);
		try {
			setState(169);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(164); ((Colomn_tailContext)_localctx).x = colomn_name();
				((Colomn_tailContext)_localctx).value =  ((Colomn_tailContext)_localctx).x.value;
				}
				break;
			case STAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(167); ((Colomn_tailContext)_localctx).y = match(STAR);
				((Colomn_tailContext)_localctx).value =  (((Colomn_tailContext)_localctx).y!=null?((Colomn_tailContext)_localctx).y.getText():null);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TablesContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode AS() { return getToken(SqlParser.AS, 0); }
		public Table_alias_nameContext table_alias_name() {
			return getRuleContext(Table_alias_nameContext.class,0);
		}
		public TablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tables; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTables(this);
		}
	}

	public final TablesContext tables() throws RecognitionException {
		TablesContext _localctx = new TablesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_tables);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171); table_name();
			setState(174);
			_la = _input.LA(1);
			if (_la==AS) {
				{
				setState(172); match(AS);
				setState(173); table_alias_name();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Where_clauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(SqlParser.WHERE, 0); }
		public List<Bool_exprContext> bool_expr() {
			return getRuleContexts(Bool_exprContext.class);
		}
		public Bool_exprContext bool_expr(int i) {
			return getRuleContext(Bool_exprContext.class,i);
		}
		public Logical_opContext logical_op() {
			return getRuleContext(Logical_opContext.class,0);
		}
		public Where_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_where_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterWhere_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitWhere_clause(this);
		}
	}

	public final Where_clauseContext where_clause() throws RecognitionException {
		Where_clauseContext _localctx = new Where_clauseContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_where_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176); match(WHERE);
			setState(177); bool_expr();
			setState(181);
			_la = _input.LA(1);
			if (_la==AND || _la==OR) {
				{
				setState(178); logical_op();
				setState(179); bool_expr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Logical_opContext extends ParserRuleContext {
		public Object value;
		public Token x;
		public TerminalNode AND() { return getToken(SqlParser.AND, 0); }
		public TerminalNode OR() { return getToken(SqlParser.OR, 0); }
		public Logical_opContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_op; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterLogical_op(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitLogical_op(this);
		}
	}

	public final Logical_opContext logical_op() throws RecognitionException {
		Logical_opContext _localctx = new Logical_opContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_logical_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			((Logical_opContext)_localctx).x = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==OR) ) {
				((Logical_opContext)_localctx).x = (Token)_errHandler.recoverInline(this);
			}
			consume();
			((Logical_opContext)_localctx).value =  new String((((Logical_opContext)_localctx).x!=null?((Logical_opContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Bool_exprContext extends ParserRuleContext {
		public List<OperandContext> operand() {
			return getRuleContexts(OperandContext.class);
		}
		public CompareContext compare() {
			return getRuleContext(CompareContext.class,0);
		}
		public OperandContext operand(int i) {
			return getRuleContext(OperandContext.class,i);
		}
		public Bool_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterBool_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitBool_expr(this);
		}
	}

	public final Bool_exprContext bool_expr() throws RecognitionException {
		Bool_exprContext _localctx = new Bool_exprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_bool_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186); operand();
			setState(190);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << GT) | (1L << EQ) | (1L << NOT_EQ))) != 0)) {
				{
				setState(187); compare();
				setState(188); operand();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperandContext extends ParserRuleContext {
		public ConstsContext consts() {
			return getRuleContext(ConstsContext.class,0);
		}
		public Colomn_nameContext colomn_name() {
			return getRuleContext(Colomn_nameContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SqlParser.DOT, 0); }
		public Table_alias_nameContext table_alias_name() {
			return getRuleContext(Table_alias_nameContext.class,0);
		}
		public OperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitOperand(this);
		}
	}

	public final OperandContext operand() throws RecognitionException {
		OperandContext _localctx = new OperandContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_operand);
		try {
			setState(199);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(195);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(192); table_alias_name();
					setState(193); match(DOT);
					}
					break;
				}
				setState(197); colomn_name();
				}
				break;
			case INT_IDENTI:
			case VARCHAR_IDENTI:
				enterOuterAlt(_localctx, 2);
				{
				setState(198); consts();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstsContext extends ParserRuleContext {
		public Object value;
		public Type_intContext x;
		public Type_varcharContext z;
		public Type_varcharContext type_varchar() {
			return getRuleContext(Type_varcharContext.class,0);
		}
		public Type_intContext type_int() {
			return getRuleContext(Type_intContext.class,0);
		}
		public ConstsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_consts; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterConsts(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitConsts(this);
		}
	}

	public final ConstsContext consts() throws RecognitionException {
		ConstsContext _localctx = new ConstsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_consts);
		try {
			setState(207);
			switch (_input.LA(1)) {
			case INT_IDENTI:
				enterOuterAlt(_localctx, 1);
				{
				setState(201); ((ConstsContext)_localctx).x = type_int();
				((ConstsContext)_localctx).value =  ((ConstsContext)_localctx).x.value; 
				}
				break;
			case VARCHAR_IDENTI:
				enterOuterAlt(_localctx, 2);
				{
				setState(204); ((ConstsContext)_localctx).z = type_varchar();
				((ConstsContext)_localctx).value =  ((ConstsContext)_localctx).z.value;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompareContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(SqlParser.LT, 0); }
		public TerminalNode NOT_EQ() { return getToken(SqlParser.NOT_EQ, 0); }
		public TerminalNode GT() { return getToken(SqlParser.GT, 0); }
		public TerminalNode EQ() { return getToken(SqlParser.EQ, 0); }
		public CompareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCompare(this);
		}
	}

	public final CompareContext compare() throws RecognitionException {
		CompareContext _localctx = new CompareContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_compare);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << GT) | (1L << EQ) | (1L << NOT_EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OpContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(SqlParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(SqlParser.MINUS, 0); }
		public TerminalNode STAR() { return getToken(SqlParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(SqlParser.DIV, 0); }
		public OpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitOp(this);
		}
	}

	public final OpContext op() throws RecognitionException {
		OpContext _localctx = new OpContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STAR) | (1L << PLUS) | (1L << MINUS) | (1L << DIV))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Colomn_nameContext extends ParserRuleContext {
		public String value;
		public Token x;
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public Colomn_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colomn_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterColomn_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitColomn_name(this);
		}
	}

	public final Colomn_nameContext colomn_name() throws RecognitionException {
		Colomn_nameContext _localctx = new Colomn_nameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_colomn_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213); ((Colomn_nameContext)_localctx).x = match(IDENTIFIER);
			((Colomn_nameContext)_localctx).value =  new String((((Colomn_nameContext)_localctx).x!=null?((Colomn_nameContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Colomn_alias_nameContext extends ParserRuleContext {
		public String value;
		public Token x;
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public Colomn_alias_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_colomn_alias_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterColomn_alias_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitColomn_alias_name(this);
		}
	}

	public final Colomn_alias_nameContext colomn_alias_name() throws RecognitionException {
		Colomn_alias_nameContext _localctx = new Colomn_alias_nameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_colomn_alias_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216); ((Colomn_alias_nameContext)_localctx).x = match(IDENTIFIER);
			((Colomn_alias_nameContext)_localctx).value =  new String((((Colomn_alias_nameContext)_localctx).x!=null?((Colomn_alias_nameContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_nameContext extends ParserRuleContext {
		public String value;
		public Token x;
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public Table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTable_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTable_name(this);
		}
	}

	public final Table_nameContext table_name() throws RecognitionException {
		Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219); ((Table_nameContext)_localctx).x = match(IDENTIFIER);
			((Table_nameContext)_localctx).value =  new String((((Table_nameContext)_localctx).x!=null?((Table_nameContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_alias_nameContext extends ParserRuleContext {
		public String value;
		public Token x;
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public Table_alias_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_alias_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTable_alias_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTable_alias_name(this);
		}
	}

	public final Table_alias_nameContext table_alias_name() throws RecognitionException {
		Table_alias_nameContext _localctx = new Table_alias_nameContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_table_alias_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222); ((Table_alias_nameContext)_localctx).x = match(IDENTIFIER);
			((Table_alias_nameContext)_localctx).value =  new String((((Table_alias_nameContext)_localctx).x!=null?((Table_alias_nameContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_intContext extends ParserRuleContext {
		public Integer value;
		public Token x;
		public TerminalNode INT_IDENTI() { return getToken(SqlParser.INT_IDENTI, 0); }
		public Type_intContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_int; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterType_int(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitType_int(this);
		}
	}

	public final Type_intContext type_int() throws RecognitionException {
		Type_intContext _localctx = new Type_intContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_type_int);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225); ((Type_intContext)_localctx).x = match(INT_IDENTI);
			((Type_intContext)_localctx).value =  new Integer((((Type_intContext)_localctx).x!=null?((Type_intContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_varcharContext extends ParserRuleContext {
		public String value;
		public Token x;
		public TerminalNode VARCHAR_IDENTI() { return getToken(SqlParser.VARCHAR_IDENTI, 0); }
		public Type_varcharContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_varchar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterType_varchar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitType_varchar(this);
		}
	}

	public final Type_varcharContext type_varchar() throws RecognitionException {
		Type_varcharContext _localctx = new Type_varcharContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_type_varchar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228); ((Type_varcharContext)_localctx).x = match(VARCHAR_IDENTI);
			((Type_varcharContext)_localctx).value =  new String((((Type_varcharContext)_localctx).x!=null?((Type_varcharContext)_localctx).x.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3>\u00ea\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\7\2<\n\2\f\2\16\2?\13\2\3\2"+
		"\3\2\3\3\3\3\3\3\5\3F\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\7\5"+
		"R\n\5\f\5\16\5U\13\5\3\5\3\5\3\5\7\5Z\n\5\f\5\16\5]\13\5\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\b\3\b\5\bh\n\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\7\13x\n\13\f\13\16\13{\13\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13\u0083\n\13\f\13\16\13\u0086\13\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\7\f\u008e\n\f\f\f\16\f\u0091\13\f\3\f\3\f\3\f\3\f\7\f\u0097"+
		"\n\f\f\f\16\f\u009a\13\f\3\f\5\f\u009d\n\f\3\r\3\r\3\r\3\r\5\r\u00a3\n"+
		"\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\5\16\u00ac\n\16\3\17\3\17\3\17\5\17"+
		"\u00b1\n\17\3\20\3\20\3\20\3\20\3\20\5\20\u00b8\n\20\3\21\3\21\3\21\3"+
		"\22\3\22\3\22\3\22\5\22\u00c1\n\22\3\23\3\23\3\23\5\23\u00c6\n\23\3\23"+
		"\3\23\5\23\u00ca\n\23\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00d2\n\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3"+
		"\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\34\2\2\35\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2\6\4\2\20\20\32\32\3\2 !\5\2"+
		"99;;=>\4\2.\60\63\63\u00e2\2=\3\2\2\2\4E\3\2\2\2\6G\3\2\2\2\bS\3\2\2\2"+
		"\n^\3\2\2\2\fb\3\2\2\2\16e\3\2\2\2\20i\3\2\2\2\22m\3\2\2\2\24p\3\2\2\2"+
		"\26\u0089\3\2\2\2\30\u00a2\3\2\2\2\32\u00ab\3\2\2\2\34\u00ad\3\2\2\2\36"+
		"\u00b2\3\2\2\2 \u00b9\3\2\2\2\"\u00bc\3\2\2\2$\u00c9\3\2\2\2&\u00d1\3"+
		"\2\2\2(\u00d3\3\2\2\2*\u00d5\3\2\2\2,\u00d7\3\2\2\2.\u00da\3\2\2\2\60"+
		"\u00dd\3\2\2\2\62\u00e0\3\2\2\2\64\u00e3\3\2\2\2\66\u00e6\3\2\2\289\5"+
		"\4\3\29:\7)\2\2:<\3\2\2\2;8\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>@\3"+
		"\2\2\2?=\3\2\2\2@A\7\2\2\3A\3\3\2\2\2BF\5\6\4\2CF\5\26\f\2DF\5\24\13\2"+
		"EB\3\2\2\2EC\3\2\2\2ED\3\2\2\2F\5\3\2\2\2GH\7\5\2\2HI\7\33\2\2IJ\5\60"+
		"\31\2JK\7+\2\2KL\5\b\5\2LM\7,\2\2M\7\3\2\2\2NO\5\f\7\2OP\7-\2\2PR\3\2"+
		"\2\2QN\3\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2TV\3\2\2\2US\3\2\2\2V[\5\n"+
		"\6\2WX\7-\2\2XZ\5\f\7\2YW\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\\t\3"+
		"\2\2\2][\3\2\2\2^_\5\f\7\2_`\7\26\2\2`a\7\22\2\2a\13\3\2\2\2bc\5,\27\2"+
		"cd\5\16\b\2d\r\3\2\2\2eg\t\2\2\2fh\5\20\t\2gf\3\2\2\2gh\3\2\2\2h\17\3"+
		"\2\2\2ij\7+\2\2jk\5\22\n\2kl\7,\2\2l\21\3\2\2\2mn\5\64\33\2no\b\n\1\2"+
		"o\23\3\2\2\2pq\7\17\2\2qr\7\21\2\2rs\5\60\31\2st\7+\2\2ty\5,\27\2uv\7"+
		"-\2\2vx\5,\27\2wu\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z|\3\2\2\2{y\3"+
		"\2\2\2|}\7,\2\2}~\7\36\2\2~\177\7+\2\2\177\u0084\5&\24\2\u0080\u0081\7"+
		"-\2\2\u0081\u0083\5&\24\2\u0082\u0080\3\2\2\2\u0083\u0086\3\2\2\2\u0084"+
		"\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0084\3\2"+
		"\2\2\u0087\u0088\7,\2\2\u0088\25\3\2\2\2\u0089\u008a\7\30\2\2\u008a\u008f"+
		"\5\30\r\2\u008b\u008c\7-\2\2\u008c\u008e\5\30\r\2\u008d\u008b\3\2\2\2"+
		"\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092"+
		"\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7\f\2\2\u0093\u0098\5\34\17\2"+
		"\u0094\u0095\7-\2\2\u0095\u0097\5\34\17\2\u0096\u0094\3\2\2\2\u0097\u009a"+
		"\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009c\3\2\2\2\u009a"+
		"\u0098\3\2\2\2\u009b\u009d\5\36\20\2\u009c\u009b\3\2\2\2\u009c\u009d\3"+
		"\2\2\2\u009d\27\3\2\2\2\u009e\u00a3\5\60\31\2\u009f\u00a0\5\62\32\2\u00a0"+
		"\u00a1\7*\2\2\u00a1\u00a3\3\2\2\2\u00a2\u009e\3\2\2\2\u00a2\u009f\3\2"+
		"\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\5\32\16\2\u00a5"+
		"\31\3\2\2\2\u00a6\u00a7\5,\27\2\u00a7\u00a8\b\16\1\2\u00a8\u00ac\3\2\2"+
		"\2\u00a9\u00aa\7.\2\2\u00aa\u00ac\b\16\1\2\u00ab\u00a6\3\2\2\2\u00ab\u00a9"+
		"\3\2\2\2\u00ac\33\3\2\2\2\u00ad\u00b0\5\60\31\2\u00ae\u00af\7\4\2\2\u00af"+
		"\u00b1\5\62\32\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\35\3\2"+
		"\2\2\u00b2\u00b3\7\37\2\2\u00b3\u00b7\5\"\22\2\u00b4\u00b5\5 \21\2\u00b5"+
		"\u00b6\5\"\22\2\u00b6\u00b8\3\2\2\2\u00b7\u00b4\3\2\2\2\u00b7\u00b8\3"+
		"\2\2\2\u00b8\37\3\2\2\2\u00b9\u00ba\t\3\2\2\u00ba\u00bb\b\21\1\2\u00bb"+
		"!\3\2\2\2\u00bc\u00c0\5$\23\2\u00bd\u00be\5(\25\2\u00be\u00bf\5$\23\2"+
		"\u00bf\u00c1\3\2\2\2\u00c0\u00bd\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1#\3"+
		"\2\2\2\u00c2\u00c3\5\62\32\2\u00c3\u00c4\7*\2\2\u00c4\u00c6\3\2\2\2\u00c5"+
		"\u00c2\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00ca\5,"+
		"\27\2\u00c8\u00ca\5&\24\2\u00c9\u00c5\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca"+
		"%\3\2\2\2\u00cb\u00cc\5\64\33\2\u00cc\u00cd\b\24\1\2\u00cd\u00d2\3\2\2"+
		"\2\u00ce\u00cf\5\66\34\2\u00cf\u00d0\b\24\1\2\u00d0\u00d2\3\2\2\2\u00d1"+
		"\u00cb\3\2\2\2\u00d1\u00ce\3\2\2\2\u00d2\'\3\2\2\2\u00d3\u00d4\t\4\2\2"+
		"\u00d4)\3\2\2\2\u00d5\u00d6\t\5\2\2\u00d6+\3\2\2\2\u00d7\u00d8\7\"\2\2"+
		"\u00d8\u00d9\b\27\1\2\u00d9-\3\2\2\2\u00da\u00db\7\"\2\2\u00db\u00dc\b"+
		"\30\1\2\u00dc/\3\2\2\2\u00dd\u00de\7\"\2\2\u00de\u00df\b\31\1\2\u00df"+
		"\61\3\2\2\2\u00e0\u00e1\7\"\2\2\u00e1\u00e2\b\32\1\2\u00e2\63\3\2\2\2"+
		"\u00e3\u00e4\7#\2\2\u00e4\u00e5\b\33\1\2\u00e5\65\3\2\2\2\u00e6\u00e7"+
		"\7%\2\2\u00e7\u00e8\b\34\1\2\u00e8\67\3\2\2\2\24=ES[gy\u0084\u008f\u0098"+
		"\u009c\u00a2\u00ab\u00b0\u00b7\u00c0\u00c5\u00c9\u00d1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}