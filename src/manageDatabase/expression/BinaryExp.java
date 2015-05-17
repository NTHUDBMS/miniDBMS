package manageDatabase.expression;

import java.util.*;
import structure.*;
import dbms.*;
/**
 * Binary Expression inherit Exp<br>
 * Two expression instead of "binary"
 * @see Exp
 */
public class BinaryExp extends Exp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1276455294L;
	/**
	 * Right Expression
	 */
	private Exp right;
	/**
	 * Left Expression
	 */
	private Exp left;
	/**
	 * Logical operation of two expression
	 */
	private String op;

	/**
	 * Constructor
	 * @param left
	 * @param op
	 * @param right
	 */
	public BinaryExp(Exp left, String op, Exp right){
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public Exp getLeft(){
		return this.left;
	}

	public Exp getRight(){
		return this.right;
	}

	public String getOp(){
		return this.op;
	}

	/**
	 * 
	 * @param visitor : 
	 * @param value : 
	 * @param attrPosTable : 
	 * @param tuple : 
	 * @return 
	 */
	public Object accept(
			DBExecutor visitor,
			Value value,
			Hashtable<String, 
			Integer> attrPosTable, 
			Tuple tuple) 
	{
		return visitor.visit(this, value, attrPosTable, tuple); 
	}
}
