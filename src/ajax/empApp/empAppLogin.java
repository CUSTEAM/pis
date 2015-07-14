package ajax.empApp;
 
import java.util.HashMap;
import java.util.Map;
import action.BaseAction; 

public class empAppLogin extends BaseAction{ 
	
	private Map emp;
	
	public String execute() {
		
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		
		emp=new HashMap();			
		
		
		try{
			emp.putAll(df.sqlGetMap("SELECT e.idno FROM empl e, wwpass w WHERE e.idno=w.username AND" +
			"(w.username='"+username+"' OR w.freename='"+username+"') AND password='"+password+"'"));
			emp.put("result", "success");			
			return SUCCESS;
		}catch(Exception e){
			emp.put("result", "error");
			emp.put("msg", "驗證錯誤");
			//setStd(std);
			return SUCCESS;
		}		       
    }

	public Map getEmp() {
		return emp;
	}

	public void setEmp(Map emp) {
		this.emp = emp;
	}

	
}