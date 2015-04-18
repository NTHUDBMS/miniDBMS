package manageDatabase;

import structure.Value;
import dbms.DBExecutor;

public class StrExp extends Exp{
	private static final long serialVersionUID = 147286827298234L;
	private String str;

	public StrExp(String str){
		this.str = str;
	}

	public String getString(){
		return this.str;
	}
	public Object accept(DBExecutor visitor, Value value) { return visitor.visit(this, value); }

}
