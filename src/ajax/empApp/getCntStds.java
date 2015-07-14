package ajax.empApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

/**
 * 取學生列表
 * @author John
 *
 */
public class getCntStds extends BaseAction{
	
	private List list;
	
	public String execute(){
		
		System.out.println("SELECT student_no, student_name FROM stmd WHERE depart_class='"+request.getAttribute("ClassNo")+"'");
		this.setList(df.sqlGet("SELECT student_no, student_name FROM stmd WHERE depart_class='"+request.getParameter("ClassNo")+"'"));
		
		
		
		return SUCCESS;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	
}
