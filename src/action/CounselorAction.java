package action;

import model.Message;

public class CounselorAction extends BaseAction{
	
	public String execute() {
		
		return SUCCESS;
	}
	
	public String student_name, cell_phone, DeptNo, note;
	
	public String confirm() {
		Message msg=new Message();
		if(df.sqlGetInt("SELECT COUNT(*)FROM Counselor_stmd WHERE cell_phone='"+cell_phone+"'AND DeptNo='"+DeptNo+"'AND reply IS NULL")>0) {
			msg.setMsg("該系所正由專人準備與您連繫..");
			this.savMessage(msg);
			return SUCCESS;
		}
		
		
		
		try {
			df.exSql("INSERT INTO Counselor_stmd(student_name,cell_phone,DeptNo,note)VALUES('"+student_name+"','"+cell_phone+"','"+DeptNo+"','"+note+"')");
			msg.setMsg("咨詢發送成功，專人將儘快與您連繫");
		}catch(Exception e) {
			e.printStackTrace();
			msg.setMsg("咨詢發送不成功，請檢查必要欄位和內容");
		}		
		this.savMessage(msg);
		return SUCCESS;
	}
}
