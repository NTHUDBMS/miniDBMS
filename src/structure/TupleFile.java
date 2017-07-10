package structure;

import java.io.Serializable;

/**
 * 
 *
 */
public class TupleFile implements Serializable {
	@Override
    public String toString() {
        return "TupleFile [tupleStack=" + tupleStack + ", tableName="
                + tableName + "]";
    }


    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 2-D array of Value<br>
	 * array of tuples<br>
	 */
	private TupleStack tupleStack;
	
	
	/**
	 * Table name of this tuple stack<br>
	 * File will named as "tableName".db
	 */
	private String tableName;
	
	/**
	 * 
	 * @return name of table name of this tuple stack
	 */
	public String getTableName() {
		return this.tableName;
	}


	/**
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	/**
	 * Constructor with table name, tuple stack
	 * @param tableName
	 * @param tupleStack
	 */
	public TupleFile(String tableName, TupleStack tupleStack) {
		this.tableName = tableName;
		this.setTupleStack(tupleStack);
	}


	public TupleStack getTupleStack() {
		return tupleStack;
	}


	public void setTupleStack(TupleStack tupleStack) {
	    // do full copy
	    this.tupleStack = new TupleStack(tupleStack.getAttrList());
	    for (Tuple t: tupleStack) {
	        this.tupleStack.add(new Tuple(t));
	    }
	}

}
