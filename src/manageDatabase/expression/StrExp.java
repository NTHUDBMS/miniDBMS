package manageDatabase.expression;

import structure.Value;
import dbms.DBExecutor;
/**
 * String Expression inherit Expression
 *
 */
public class StrExp extends Exp{
	@Override
    public String toString() {
        return "[str=" + str + "]";
    }

    /**
	 * 
	 */
	private static final long serialVersionUID = 147286827298234L;
	/**
	 * String expression
	 */
	private String str;
	
	
	public StrExp(String str){
		this.str = str;
	}
	
	public String getStr(){
		return this.str;
	}
	
	/**
	 * 
	 * @see Exp
	 */
	public Object accept(DBExecutor visitor, Value value) {
		return visitor.visit(this, value); 
	}

}
