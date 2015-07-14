package ajax.empApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

/**
 * 取未審假單
 * @author John
 *
 */
public class getDocApp extends BaseAction{
	
	private List list;
	
	public String execute(){
		SimpleDateFormat sf=new SimpleDateFormat("M月d日");
		List<Map>docs=df.sqlGet("SELECT dr.name, d.*, s.student_no, s.student_name, c.ClassName FROM Dilg_apply d, stmd s, Class c, Dilg_rules dr WHERE " +
				"dr.id=d.abs AND c.ClassNo=s.depart_class AND s.student_no=d.student_no AND d.result IS NULL AND d.auditor='"+request.getParameter("id")+"'");
				
				
				for(int i=0; i<docs.size(); i++){
					docs.get(i).put("abss", 
					df.sqlGet("SELECT c.chi_name, s.dilg_period, DATE_FORMAT(d.date,'%m月%d日')as date, d.cls, d.abs FROM Dilg d, Seld s, Csno c, Dtime dt " +
					"WHERE dt.cscode=c.cscode AND dt.Oid=d.Dtime_oid AND s.student_no=d.student_no AND s.Dtime_oid=d.Dtime_oid AND " +
					"d.Dilg_app_oid="+docs.get(i).get("Oid")));
				}
		
		this.setList(docs);
		
		
		
		
		return SUCCESS;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	
}
