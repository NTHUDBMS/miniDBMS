package structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import manageDatabase.expression.BinaryExp;
import manageDatabase.expression.ColExp;
import manageDatabase.expression.Condition;
import manageDatabase.expression.Exp;
import manageDatabase.expression.IdExp;
import manageDatabase.expression.IntExp;
import manageDatabase.expression.StrExp;

import com.google.common.collect.Multimap;

/**
 * The space to store all tuples, which have column view * row view
 */
public class TupleStack extends ArrayList<Tuple> {
	
	/**
	 * Serialize ID to implement Serializable interface
	 */
	private static final long serialVersionUID = 7016417759679515823L;
	
		
	/**
	 * Data in column view
	 * Column is arrayList here
	 */
	private List<Multimap<Value, Integer>> columnList; 
	

	//	ArrayList<Multimap<Value, Integer>> hashIndex;
	/**
	 * Ordered attribute name of columns 
	 */
	private List<Attribute> attrList;
    private List<String> names;
	private List<String> selectattrList;
	
	
	private  Map<String, Map<Value, List<Tuple>>> value2Tuple;
	private  Map<String, Map<Value, List<Tuple>>> sortedValue2Tuple;
	

	/**
	 * Position of attribute  attribute to tableId
	 */
	private Hashtable<String,Integer> attrPosTable;

	/**
	 * 
	 * @param tupleStack
	 * @param attrList
	 */
	public TupleStack(List<Tuple> tupleStack, List<Attribute> attrList) {
		super(tupleStack);
	    this.names = new ArrayList<>();
		this.setAttrList(attrList); // build attrPosTable as the same time
        this.setAttrName();
		this.value2Tuple = new HashMap<>();
		this.sortedValue2Tuple= new TreeMap<>();
	}
	

	
	/**
	 * used in select get Tuple by select column<br>
	 * 							   select condition<br>
	 * 							   needed value tuple
	 * Normal Constructor by using super class
	 * input String attrList because of select
	 * @param selectattrList is attrlist selected 
	 * @param bool select is useless, distinguish constructor
	 */
	public TupleStack(List<String> selectattrList,boolean select) {
		super();
	    this.names = new ArrayList<>();
		this.setselectattrList(selectattrList); // build attrPosTable as the same time
        this.setAttrName();
		this.value2Tuple = new HashMap<>();
		this.sortedValue2Tuple = new HashMap<>();
	}
	
	
	public void setColumnList(ArrayList<Multimap<Value, Integer>> columnList) {
		this.columnList = columnList;
	}
    public List<Multimap<Value, Integer>> getColumnList() {
		return columnList;
	}

	/**
	 * Normal Constructor by using super class
	 * used in getTupleList in DBexecutor
	 */
	public TupleStack(List<Attribute> attrList) {
		super();
	    this.names = new ArrayList<>();
		this.setAttrList(attrList); // build attrPosTable as the same time
        this.setAttrName();
		this.value2Tuple = new HashMap<>();
		this.sortedValue2Tuple = new HashMap<>();
	}
	
	boolean containIndex() {
	    return ! this.value2Tuple.isEmpty();
	}
	
	private int getAttrPos(Attribute attr) {
	    return this.attrList.indexOf(attr);
	}
	
	public void fullJoin(TupleStack t2) {
	    this.attrList.addAll(t2.attrList);
	    for (Tuple tuple1: this) {
	        for (Tuple tuple2 : t2) {
	            tuple1.addAll(tuple2);
	        }
	    }
	}
	
	private void filterByHash(Exp exp, List<Tuple> tuples) {
	    System.out.println("filter by hash");
        Exp left = ((BinaryExp) exp).getLeft();
        Exp right = ((BinaryExp) exp).getRight();
        if((left instanceof ColExp && this.names.contains(((ColExp) left).getColomnName())
            || (left instanceof IdExp && this.names.contains(((IdExp)left).getId())))){
            
            String attrName = "";
            if (left instanceof ColExp) {
                ColExp attr = (ColExp) left;
                attrName = attr.getColomnName();
            }
            else if (left instanceof IdExp) {
                attrName = ((IdExp)left).getId();
            }
            this.buildHashIndex(attrName);
            Exp val = right;
            Value v = null;
            if (val instanceof StrExp) {
                v = new Value(((StrExp) val).getStr());
            }
            else if (val instanceof IntExp) {
                v = new Value(((IntExp) val).getInt());
            }
            
            if (v != null && this.value2Tuple.get(attrName).containsKey(v)) {
                List<Tuple> temp= this.value2Tuple.get(attrName).get(v);
                for (Tuple t: temp) {
                    tuples.add(new Tuple(t));
                }
            }
        }
	}
	public void filterByCondition(Condition condition) {
	    List<Tuple> tuples = new ArrayList<>();
	    this.filterByHashCondition(condition, tuples);
	    this.filterByCompareCondition(condition, tuples);
	    if (tuples.size() != 0) {
            this.clear();
            this.addAll(tuples);
	    }
	}

	public void filterByHashCondition(Condition condition, List<Tuple> tuples) {
	    List<Exp> expList = condition.getHashExpList();
	    if (expList.size() == 0) return;
	    for (Exp exp: expList) {
	        filterByHash(exp, tuples);
	    }
   }
	
	public void filterByCompareCondition(Condition condition, List<Tuple> tuples) {
	    System.out.println("filter by compare");
	    List<Exp> expList = condition.getRangeExpList();
	    if (expList.size() == 0) return;
	    
	    for (Exp exp: expList) {
            Exp left = ((BinaryExp) exp).getLeft();
            Exp right = ((BinaryExp) exp).getRight();

            if((left instanceof ColExp && this.names.contains(((ColExp) left).getColomnName())
                || (left instanceof IdExp &&(this.names.contains(((IdExp)left).getId()))))){

                String attrName = "";
                if (left instanceof ColExp) {
                    ColExp attr = (ColExp) left;
                    attrName = attr.getColomnName();
                }
                else if (left instanceof IdExp) {
                    attrName = ((IdExp)left).getId();
                }
                

                Exp val = right;
                Value v = null;
                if (val instanceof IdExp) {
                    v = new Value(((IdExp) val).getId());
                }
                else if (val instanceof IntExp) {
                    v = new Value(((IntExp) val).getInt());
                }
                
                String op = ((BinaryExp) exp).getOp();
                this.buildTreeIndex(attrName);
                

                Map<Value, List<Tuple>> m = null;
                Map<Value, List<Tuple>> n = null;
                if (op.equals("<>")) {
                    m = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).headMap(new Value(v));
                    n = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).tailMap(new Value(v), false);
                }
                if (op.equals("<")) {
                    m = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).headMap(new Value(v));
                }
                else if (op.equals(">")) {
                    m = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).tailMap(new Value(v), false);
                }
                else if (op.equals(">=")) {
                    m = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).tailMap(new Value(v));
                }
                else if (op.equals("<=")) {
                    m = ((TreeMap<Value, List<Tuple>>) this.sortedValue2Tuple.get(attrName)).headMap(new Value(v), true);
                }
                if (m != null) {
                    for (List<Tuple> temp : m.values()) {
                        for (Tuple t: temp) {
                            Tuple u = new Tuple(t);
                            tuples.add(u);
                        }
                    }
                }
                if (n != null) {
                     for (List<Tuple> temp : n.values()) {
                        for (Tuple t: temp) {
                            Tuple u = new Tuple(t);
                            tuples.add(u);
                        }
                    }
                }
            }
	    }
	    
	    
	    

	}
	
	boolean buildIndex(String attr, Map<String, Map<Value, List<Tuple>>> m, boolean isHash) {
	    System.out.println("build index");
	    
        Map<Value, List<Tuple>> index;
	    if (isHash) {
	       index = new HashMap<>();
	    }
	    else index = new TreeMap<>();
	    if (this.names.contains(attr)) {
            int attrPos = this.names.indexOf(attr);
	        for (int i = 0; i < this.size(); i++) {
	            Value v = this.get(i).get(attrPos);
	            if (index.containsKey(v)) {
                    index.get(v).add(this.get(i));
	            }
	            else {
	                List<Tuple> temp = new ArrayList<>();
	                temp.add(this.get(i));
	                index.put(v, temp);
	            }
	        }
	        
//	        assert(this.size() == this.value2Tuple.size());
	        m.put(attr, index);
	        return true;
	    }
	    return false;
	}
	
	boolean buildHashIndex(String attr) {
	    if (value2Tuple.containsKey(attr))
	        return true;
	    else 
            return buildIndex(attr, this.value2Tuple, true);
	}
	
	boolean buildTreeIndex(String attr) {
	    if (sortedValue2Tuple.containsKey(attr)) 
	        return true;
	    else  
            return buildIndex(attr, this.sortedValue2Tuple, false);
	}

    public List <Value> getColumnByPos(int position) {
        List <Value> selectColumn = new ArrayList <>();
        for(Tuple tuple: this) {
            selectColumn.add(new Value(tuple.get(position)));
        }
        return selectColumn;
    }	
	
	
	//used in cartesianProduct for DBexecutor
	public TupleStack(){
		super();//call arrayList<Tuple> constructor
	}

	public void setAttrName() {
	    for (Attribute attr : this.attrList) {
	        names.add(attr.getName());
	    }
	}

	public List<String> getAttrsName() {
	    return this.names;
	}

	@Override
    public String toString() {
        return "TupleStack [width=" + this.attrList.size() + ", length=" + this.size() 
                + ", attrList=" + attrList + "]";
    }
	
	public int getLength() {
	    return this.size();
	}



    /**
	 * This method also build up attribute position table
	 * @param attrList
	 */
	public void setAttrList(List<Attribute> attrList) {
	    this.attrList = new ArrayList<>();
	    this.attrList.addAll(attrList);
//		 construct attrPosTable
		 this.attrPosTable = new Hashtable<String,Integer>();
		 int i = 0;
		 for(Attribute attr : attrList){
		 	this.attrPosTable.put(attr.getName(), i);
		 	i++;
		 }
	}
	
	
	/**
	 * set the name of each each attributes first
	 * then construct TupleStack(ArrayList<Attribute> attrList)
	 * 
	 * @param list of columnNames
	 * @return new tupleStacks
	 */
	public TupleStack newStackByColumns(ArrayList<String> columnNames){
		ArrayList<Attribute> newAttrList = new ArrayList<Attribute>();
		int i=0;
		for(Attribute a : this.attrList){
			//just return true or false not assign
//			a.getName().equals(columnNames.get(i));
			a.setName(columnNames.get(i));
			newAttrList.add(a);
			i++;
		}
		
		TupleStack newOne = new TupleStack(newAttrList);
		
		return newOne;
	}
	

	/**
	 * 
	 * For insert in DBexecutor
	 * 
	 * Override super method: boolean add(E e).<br>
	 * Store tuple and insert into column view of TupleStack.<br>
	 */
	public boolean add(Tuple tuple){
		
		// Insert into column view
//		int i=0;
//		for(Value v : tuple){
//			if(attrList.get(i).getType() == v.getType()
//					&&v.getType() == Type.INT)
//			{
//				(columnList.get(i)).add(v);
//				i++;
//			}
//			else throw new Error("INSERT: wrong data type.");
//		}
		boolean ans = super.add(tuple);
//		++this.length; //add length
		return ans;
	}
	
//	/**
//	 * Override super method: void add(int index, E e).<br>
//	 * Store tuple and insert into column view of TupleStack.<br>
//	 */
//	public void add(int index, Tuple tuple){
//		super.add(index, tuple);
//		
//		// Insert into column view
//		
//	}
//	
//	/**
//	 * Override super method: E remove(int).<br>
//	 * Also remove tuple form column view
//	 */
//	public Tuple remove(int index){
//		Tuple ret = this.remove(index);
//		for(Column c : columnList){
//			c.remove(index);
//		}
//		
//		return ret;
//	}
//


	public Hashtable<String,Integer> getAttrPosTable() {
		return attrPosTable;
	}


	public void setAttrPosTable(Hashtable<String,Integer> attrPosTable) {
		this.attrPosTable = attrPosTable;
	}

	public List<Attribute> getAttrList() {
		return attrList;
	}


	/**
	 * This method also build up attribute position table
	 * @param attrList
	 */
	public void setselectattrList(List<String> selectattrList) {
		this.setSelectattrList(selectattrList);
		// construct attrPosTable
		this.attrPosTable = new Hashtable<String,Integer>();
		 int i = 0;
		 for(String attr : selectattrList){
		 	this.attrPosTable.put(attr, i);
		 	i++;
		 }
	}


	public List<String> getSelectattrList() {
		return selectattrList;
	}

	public void setSelectattrList(List<String> selectattrList) {
		this.selectattrList = selectattrList;
	}

//	public ArrayList<String> getSelectattrListWithTable() {
//		return selectattrListWithTable;
//	}
//
//	public void setSelectattrListWithTable(ArrayList<String> selectattrListWithTable) {
//		this.selectattrListWithTable = selectattrListWithTable;
//	}
	
	
}
