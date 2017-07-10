package manageDatabase.expression;

import structure.*;
import dbms.DBExecutor;

import java.util.*;
/**
 * 
 */
public class Exp implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 931745767652285L;
	
	/**
	 * 
	 * @param visitor
	 * @param value
	 * @return
	 */
	public Object accept(DBExecutor visitor, Value value) {
		return visitor.visit(this, value); 
	}
	
	/**
	 * 
	 * @param visitor
	 * @param attrPosTable
	 * @param tuple
	 * @return
	 */
	public Object accept( DBExecutor visitor, Hashtable<String, Integer> attrPosTable, Tuple tuple) {
		return visitor.visit(this, attrPosTable, tuple); 
	}

}
