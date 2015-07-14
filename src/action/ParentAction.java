package action;

import java.util.Map;
import model.Message;

public class ParentAction extends BaseAction{
	
	public String idno;
	public String birthday;
	public String parent;	
	public String execute(){
		
		return SUCCESS;
	}
	
	public String login(){	
		
		try{
			birthday=bl.getBirthday(birthday);
		}catch(Exception e){
			Message msg=new Message();
			msg.setError("生日應為YYMMDD, 如90年1月12日為:900112");
			savMessage(msg);
			return SUCCESS;
		}
		
		Map std=sam.getDataFinder().sqlGetMap("SELECT s.student_no, s.depart_class, s.student_name, c.ClassName, s.curr_post,s.curr_addr," +
		"s.perm_post, s.perm_addr, s.telephone, s.CellPhone, s.Email FROM stmd s, Class c WHERE " +
		"c.ClassNo=s.depart_class AND s.idno='"+idno+"' AND s.birthday='"+birthday+"'");	
		
		if(std==null){
			Message msg=new Message();
			msg.setError("驗證錯誤");
			savMessage(msg);
			return SUCCESS;
		}else{
			
			request.setAttribute("std", std);
			request.setAttribute("tutor", sam.getDataFinder().sqlGetMap("SELECT e.cname, e.CellPhone, " +
			"e.Email, cd.name as deptName,cc.name as schoolName,cc.address, cd.location, " +
			"cd.phone,cd.fax FROM empl e, Class c, CODE_DEPT cd, CODE_CAMPUS cc " +
			"WHERE cc.id=c.CampusNo AND cd.id=c.DeptNo AND e.idno=c.tutor AND " +
			"c.ClassNo='"+std.get("depart_class")+"'"));
		}
		
		request.setAttribute("stdNo", std.get("student_no"));
		request.setAttribute("stdInfo", sam.getDataFinder().sqlGetMap("SELECT " +
		"student_name, schl_name, telephone, CellPhone, curr_addr, perm_addr FROM stmd " +
		"WHERE student_no='"+std.get("student_no")+"'"));
		request.setAttribute("allClass", sam.getCsTable(std.get("student_no").toString(), 
		getContext().getAttribute("school_term").toString()));
		
		return SUCCESS;
	}
}