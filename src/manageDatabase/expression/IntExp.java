package manageDatabase.expression;

import structure.Value;
import dbms.DBExecutor;

/**
 * Integer Expression inherit Expression
 * 
 */
public class IntExp extends Exp{
	private static final long serialVersionUID = 2543146112341L;
	private int intNum;

	public IntExp(int intNum){
		this.intNum = intNum;
	}

	public int getInt(){
		return this.intNum;
	}

	/**
	 * 
	 * @see Exp
	 */
	public Object accept(DBExecutor visitor, Value value) {
		return visitor.visit(this, value); 
	}
	
	public String toString(){
		return Integer.toString(intNum);
	}

}
