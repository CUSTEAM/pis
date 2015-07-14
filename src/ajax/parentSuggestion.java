package ajax;
 
import java.util.Map;
import action.BaseAction; 

public class parentSuggestion extends BaseAction{
	
	private String sugg;	
	
	public String execute() {
		
		try{
			Map<String, String>map=df.sqlGetMap("SELECT student_name, parent_name FROM stmd WHERE idno='"+request.getParameter("idno")+"'");
			String parentName=map.get("parent_name");
			if(parentName==null||parentName.equals("")){
				parentName="未填報家長代表, 欄位請空白即可";
			}else{
				//parentName="○"+parentName.substring(1, 2)+"○";
				parentName="家長姓名中有「 "+parentName.substring(1, 2)+"」字";
			}
			
			if(map==null){
				setSugg("無此學生資料, 請檢查身分證號");
				return SUCCESS;
			}else{
				setSugg(map.get("student_name")+"同學, "+parentName);
				return SUCCESS;
			}
		}catch(Exception e){
			e.printStackTrace();
			setSugg("無此學生資料, 請確認");
			return SUCCESS;
		}               
    }

	public String getSugg() {
		return sugg;
	}

	public void setSugg(String sugg) {
		this.sugg = sugg;
	} 
}