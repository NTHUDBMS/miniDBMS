package manageDatabase.expression;


import java.util.ArrayList;
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
	

	private ArrayList<String> idList;
	private ArrayList<String> tableList;
	/**
	 * Constructor with expression
	 * @param exp
	 */
	public Condition(Exp exp){
		this.exp = exp;
	}

	
	public Exp getExp(){
		return this.exp;
	}

	/**
	 * 
	 * @return 
	 */
	public ArrayList<String> getIdList(){
//		ArrayList<String> idList = new ArrayList<String>();
		this.idList =  new ArrayList<String>();
		this.tableList = new ArrayList<String>();
		getIdList(this.idList, this.exp);
//		System.out.println("final Id list size is "+ idList.size());
		return this.idList;
	}

	/**
	 * 
	 * @param idList : identifier list
	 * @param exp : 
	 */
	private void getIdList(ArrayList<String> idList, Exp exp){
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
			getIdList(idList, ((BinaryExp) exp).getLeft() );
			getIdList(idList, ((BinaryExp) exp).getRight() );
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


	public ArrayList<String> getTableList() {
		return tableList;
	}


	public void setTableList(ArrayList<String> tableList) {
		this.tableList = tableList;
	}

	


}
