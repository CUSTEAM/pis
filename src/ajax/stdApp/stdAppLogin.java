package ajax.stdApp;
 
import java.util.HashMap;
import java.util.Map;
import action.BaseAction; 

public class stdAppLogin extends BaseAction{ 
	
	private Map std;
	
	public String execute() {
		System.out.println("work?");
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		std=new HashMap();			
		
		try{
			std.putAll(df.sqlGetMap("SELECT s.student_no FROM stmd s, wwpass w WHERE " +
			"s.student_no=w.username AND w.username='"+username+"' AND password='"+password+"'"));
			std.put("result", "success");			
			return SUCCESS;
		}catch(Exception e){
			std.put("result", "error");
			std.put("msg", "驗證錯誤");
			//setStd(std);
			return SUCCESS;
		}		       
    }

	public Map getStd() {
		return std;
	}

	public void setStd(Map std) {
		this.std = std;
	} 
}