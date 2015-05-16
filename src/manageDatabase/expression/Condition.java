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
		ArrayList<String> idList = new ArrayList<String>();
		getIdList(idList, this.exp);
		return idList;
	}

	/**
	 * 
	 * @param idList : identifier list
	 * @param exp : 
	 */
	private void getIdList(ArrayList<String> idList, Exp exp){
		if(exp == null)
			return;
		
		if(exp instanceof IdExp){
			idList.add(((IdExp) exp).getId() );
		}
		
		if(exp instanceof ColExp){
			idList.add(
				((ColExp)exp).getTableName()+
				"."+
				((ColExp)exp).getColomnName()
			);
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

	


}
