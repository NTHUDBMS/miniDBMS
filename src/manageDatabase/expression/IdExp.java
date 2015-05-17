package manageDatabase.expression;
import dbms.DBExecutor;
import structure.*;
import java.util.*;

/**
 * Identifier Expression inherit Expression
 * @see Exp
 */
public class IdExp extends Exp{
	/**
	 * Identifier
	 */
	private String id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 184721213445224L;

	public IdExp(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	
	/**
	 * @see Exp
	 */
	public Object accept(DBExecutor visitor, Value value) { 
		return visitor.visit(this, value); 
	}
	/**
	 * @see Exp
	 */
	public Object accept(
			DBExecutor visitor, 
			Hashtable<String, Integer> attrPosTable, 
			Tuple tuple) 
	{
		return visitor.visit(this, attrPosTable, tuple); 
	}
}
