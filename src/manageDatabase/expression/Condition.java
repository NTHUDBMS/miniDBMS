package manageDatabase.expression;


import java.util.ArrayList;
import java.util.List;
/**
 * Condition class which store the restrictions specified in where clause<br>
 */
public class Condition implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 865421466L;
	/**
	 * Expression
	 */
	private Exp exp;
	private List<String> idList;
	private List<String> tableList;
	private List<Exp> rangeExpList; // op is > <
	private List<Exp> hashExpList; //op is =
	private List<Exp> joinExpList; // op is = and left is ColExp right is ColExp 
	
	

	/**
	 * Constructor with expression
	 * @param exp
	 */
	public Condition(Exp exp){
	    this.rangeExpList = new ArrayList<>();
	    this.hashExpList = new ArrayList<>();
		this.tableList = new ArrayList<String>();
		this.idList =  new ArrayList<String>();
		this.joinExpList = new ArrayList<>();
		this.exp = exp;
		this.setCompareExpList(this.exp);
		this.setJoinExp(this.exp);
	}
	
	public boolean isJoin() {
	    if (exp instanceof BinaryExp) return ((BinaryExp) exp).isJoin();
	    else return false;
	}
	
	public void setJoinExp(Exp exp) {
        if(exp == null)
			return;
		else if (exp instanceof IdExp)
			return;
		else if(exp instanceof ColExp)
			return;
		if (exp instanceof BinaryExp) {
			Exp left = ((BinaryExp) exp).getLeft();
			Exp right = ((BinaryExp) exp).getRight();
			if( left instanceof ColExp && right instanceof ColExp ) {
			    String op = ((BinaryExp) exp).getOp();
			    if (op.equals("=")) {
			        this.joinExpList.add(exp);
			    }
			}
			else {
				setJoinExp(((BinaryExp) exp).getLeft());
				setJoinExp(((BinaryExp) exp).getRight());
			}
		}
	}

	// now we only have one join exp
	public Exp getJoinExp() {
	    return this.joinExpList.get(0);
	}
	

	public List<Exp> getRangeExpList() {
		return rangeExpList;
	}


	public void setRangeExpList(List<Exp> expList) {
		this.rangeExpList = expList;
	}

	public List<Exp> getHashExpList() {
		return hashExpList;
	}

	public void setHashExpList(List<Exp> expList) {
		this.hashExpList = expList;
	}
	
	
	public Exp getExp(){
		return this.exp;
	}

	/**
	 * 
	 * @return 
	 */
	public List<String> getIdList(){
		getIdList(this.idList, this.exp);
//		System.out.println("final Id list size is "+ idList.size());
		return this.idList;
	}
	
	public List<Exp> getCompareExpList() {
	    List<Exp> l = new ArrayList<>();
	    l.addAll(this.hashExpList);
	    l.addAll(this.rangeExpList);
	    return l;
	}
	

	/**
	 * compare with generic String or integer 
	 * can be used for the condition focusing on
	 * hashing index or range search
	 */
	public void setCompareExpList(Exp exp) {
		if(exp == null)
			return;
		else if (exp instanceof IdExp)
			return;
		else if(exp instanceof ColExp)
			return;
		if (exp instanceof BinaryExp) {
			Exp left = ((BinaryExp) exp).getLeft();
			Exp right = ((BinaryExp) exp).getRight();
			if((left instanceof IntExp || left instanceof StrExp) 
				|| (right instanceof IntExp || right instanceof StrExp)) {
				String op = ((BinaryExp) exp).getOp();
				if (op.equals(">") || op.equals("<")||op.equals("<>")) {
					this.rangeExpList.add(exp);
				}
				else if(op.equals("=")) {
					this.hashExpList.add(exp);
				}
			}
			else {
				setCompareExpList(((BinaryExp) exp).getLeft());
				setCompareExpList(((BinaryExp) exp).getRight());
			}
		}
	}
	

	/**
	 * 
	 * @param idList : identifier list
	 * @param exp : 
	 */
	private void getIdList(List<String> idList, Exp exp){
		if(exp == null)
			return;
		//attribute
		if(exp instanceof IdExp){
			this.idList.add(((IdExp) exp).getId());
			this.tableList.add("");
//			System.out.println("add "+((IdExp) exp).getId() +" succeed in IdList");
		}
		//tableName.attribute
		if(exp instanceof ColExp){
			this.tableList.add(((ColExp)exp).getTableName());
			this.idList.add(
				// (ColExp)exp).getTableName()+
				// "."+
				((ColExp)exp).getColomnName()
				)
				;
//			System.out.println("add "+
//				((ColExp)exp).getTableName()+"."
//				+((ColExp)exp).getColomnName()+" succeed in IdList");
		}
		
		if(exp instanceof BinaryExp){
			//boolean ret;
			getIdList(idList, ((BinaryExp) exp).getLeft());
			getIdList(idList, ((BinaryExp) exp).getRight());
		}
	}

	/**
	 * Overload method idCheck(String, Exp)<br>
	 * @param id
	 * @return
	 * 
	 */
	public boolean idCheck(String id){
		return idCheck(id, this.exp);
	}
	
	
	/**
	 * Check if attribute in this condition
	 * @param id
	 * @param exp
	 * @return
	 */
	private boolean idCheck(String id, Exp exp){
		if(exp != null && exp instanceof IdExp){
			if( ((IdExp) exp).getId().equals(id)){
				return true;
			}else{
				return false;
			}
		}

		if(exp != null && exp instanceof BinaryExp){
			boolean ret;
			ret = idCheck(id, ((BinaryExp) exp).getLeft() );
			if(ret != false)
				ret = idCheck(id, ((BinaryExp) exp).getRight() );
			return ret;
		}
	
		return true;
	}


	public List<String> getTableList() {
		return tableList;
	}


	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}

}
