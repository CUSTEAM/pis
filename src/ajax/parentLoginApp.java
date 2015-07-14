package ajax;
 
import java.util.HashMap;
import java.util.Map;
import action.BaseAction; 

public class parentLoginApp extends BaseAction{ 
	
	private Map std;
	
	public String execute() {
		String idno=request.getParameter("idno");
		String birthday=request.getParameter("password");
		
		std=new HashMap();		
		
		try{
			birthday=bl.getBirthday(birthday);
		}catch(Exception e){
			
			std.put("result", "error");
			std.put("msg", "生日應為YYMMDD, 如90年1月12日為:900112");
			//setStd(std);
			return SUCCESS;
		}
		
		
		std.putAll(df.sqlGetMap("SELECT s.student_no, IFNULL(s.parent_name,'')as parent_name FROM stmd s WHERE s.idno='"+idno+"' AND s.birthday='"+birthday+"'"));
		std.put("result", "success");
		if(String.valueOf(std.get("parent_name")).equals("")){
			std.put("msg", "家長姓名未填報, 請輸入空白即可");
		}else{
			std.put("msg", "家長姓名中有「 "+String.valueOf(std.get("parent_name")).substring(1, 2)+"」字");
		}
		return SUCCESS;       
    }

	public Map getStd() {
		return std;
	}

	public void setStd(Map std) {
		this.std = std;
	} 
}