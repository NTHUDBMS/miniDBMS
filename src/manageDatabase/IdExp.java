package manageDatabase;
import dbms.DBExecutor;
import structure.Value;
import java.util.*;
public class IdExp extends Exp{
	private String id;
	private static final long serialVersionUID = 184721213445224L;

	public IdExp(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public Object accept(DBExecutor visitor, Value value) { return visitor.visit(this, value); }

	public Object accept(DBExecutor visitor, Hashtable<String, Integer> attrPosTable, ArrayList<Value> tuple) { return visitor.visit(this, attrPosTable, tuple); }
}
