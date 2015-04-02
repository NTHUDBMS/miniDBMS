// Generated from Sql.g4 by ANTLR 4.4


package src;
import java.util.*;
import manageDatabase.*;
import structure.*;


import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SqlParser}.
 */
public interface SqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SqlParser#instructions}.
	 * @param ctx the parse tree
	 */
	void enterInstructions(@NotNull SqlParser.InstructionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#instructions}.
	 * @param ctx the parse tree
	 */
	void exitInstructions(@NotNull SqlParser.InstructionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(@NotNull SqlParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(@NotNull SqlParser.Where_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#compare}.
	 * @param ctx the parse tree
	 */
	void enterCompare(@NotNull SqlParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#compare}.
	 * @param ctx the parse tree
	 */
	void exitCompare(@NotNull SqlParser.CompareContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(@NotNull SqlParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(@NotNull SqlParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#consts}.
	 * @param ctx the parse tree
	 */
	void enterConsts(@NotNull SqlParser.ConstsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#consts}.
	 * @param ctx the parse tree
	 */
	void exitConsts(@NotNull SqlParser.ConstsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void enterTables(@NotNull SqlParser.TablesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#tables}.
	 * @param ctx the parse tree
	 */
	void exitTables(@NotNull SqlParser.TablesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#type_varchar}.
	 * @param ctx the parse tree
	 */
	void enterType_varchar(@NotNull SqlParser.Type_varcharContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#type_varchar}.
	 * @param ctx the parse tree
	 */
	void exitType_varchar(@NotNull SqlParser.Type_varcharContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(@NotNull SqlParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(@NotNull SqlParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#select_from}.
	 * @param ctx the parse tree
	 */
	void enterSelect_from(@NotNull SqlParser.Select_fromContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#select_from}.
	 * @param ctx the parse tree
	 */
	void exitSelect_from(@NotNull SqlParser.Select_fromContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#type_int}.
	 * @param ctx the parse tree
	 */
	void enterType_int(@NotNull SqlParser.Type_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#type_int}.
	 * @param ctx the parse tree
	 */
	void exitType_int(@NotNull SqlParser.Type_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(@NotNull SqlParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(@NotNull SqlParser.OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#types}.
	 * @param ctx the parse tree
	 */
	void enterTypes(@NotNull SqlParser.TypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#types}.
	 * @param ctx the parse tree
	 */
	void exitTypes(@NotNull SqlParser.TypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(@NotNull SqlParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(@NotNull SqlParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#length}.
	 * @param ctx the parse tree
	 */
	void enterLength(@NotNull SqlParser.LengthContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#length}.
	 * @param ctx the parse tree
	 */
	void exitLength(@NotNull SqlParser.LengthContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#colomn_tail}.
	 * @param ctx the parse tree
	 */
	void enterColomn_tail(@NotNull SqlParser.Colomn_tailContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#colomn_tail}.
	 * @param ctx the parse tree
	 */
	void exitColomn_tail(@NotNull SqlParser.Colomn_tailContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#primary_key}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_key(@NotNull SqlParser.Primary_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#primary_key}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_key(@NotNull SqlParser.Primary_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#colomn_name}.
	 * @param ctx the parse tree
	 */
	void enterColomn_name(@NotNull SqlParser.Colomn_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#colomn_name}.
	 * @param ctx the parse tree
	 */
	void exitColomn_name(@NotNull SqlParser.Colomn_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#insert_into}.
	 * @param ctx the parse tree
	 */
	void enterInsert_into(@NotNull SqlParser.Insert_intoContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#insert_into}.
	 * @param ctx the parse tree
	 */
	void exitInsert_into(@NotNull SqlParser.Insert_intoContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr(@NotNull SqlParser.Bool_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr(@NotNull SqlParser.Bool_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_list(@NotNull SqlParser.Attribute_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#attribute_list}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_list(@NotNull SqlParser.Attribute_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#create_table}.
	 * @param ctx the parse tree
	 */
	void enterCreate_table(@NotNull SqlParser.Create_tableContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#create_table}.
	 * @param ctx the parse tree
	 */
	void exitCreate_table(@NotNull SqlParser.Create_tableContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#int_len}.
	 * @param ctx the parse tree
	 */
	void enterInt_len(@NotNull SqlParser.Int_lenContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#int_len}.
	 * @param ctx the parse tree
	 */
	void exitInt_len(@NotNull SqlParser.Int_lenContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#colomn_alias_name}.
	 * @param ctx the parse tree
	 */
	void enterColomn_alias_name(@NotNull SqlParser.Colomn_alias_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#colomn_alias_name}.
	 * @param ctx the parse tree
	 */
	void exitColomn_alias_name(@NotNull SqlParser.Colomn_alias_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#colomns}.
	 * @param ctx the parse tree
	 */
	void enterColomns(@NotNull SqlParser.ColomnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#colomns}.
	 * @param ctx the parse tree
	 */
	void exitColomns(@NotNull SqlParser.ColomnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#operand}.
	 * @param ctx the parse tree
	 */
	void enterOperand(@NotNull SqlParser.OperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#operand}.
	 * @param ctx the parse tree
	 */
	void exitOperand(@NotNull SqlParser.OperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#logical_op}.
	 * @param ctx the parse tree
	 */
	void enterLogical_op(@NotNull SqlParser.Logical_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#logical_op}.
	 * @param ctx the parse tree
	 */
	void exitLogical_op(@NotNull SqlParser.Logical_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link SqlParser#table_alias_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias_name(@NotNull SqlParser.Table_alias_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SqlParser#table_alias_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias_name(@NotNull SqlParser.Table_alias_nameContext ctx);
}