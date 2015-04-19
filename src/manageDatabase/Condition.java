package manageDatabase;


import java.util.ArrayList;
/**
 * 
 */
public class Condition implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 865421466L;
	/**
	 * 
	 */
	private Exp exp;
	
	/**
	 * 
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
	 * @param idList
	 * @param exp
	 */
	private void getIdList(ArrayList<String> idList, Exp exp){
		if(exp != null && (exp instanceof IdExp)){
			idList.add(((IdExp) exp).getId() );
		}

		if(exp != null && (exp instanceof BinaryExp)){
			//boolean ret;
			getIdList(idList, ((BinaryExp) exp).getLeft() );
			getIdList(idList, ((BinaryExp) exp).getRight() );
		}
	}

	/**
	 * 
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
