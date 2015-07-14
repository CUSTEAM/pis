package ajax.empApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import action.BaseAction;

public class getRollCallStds extends BaseAction{
	
	private List list;
	private Object dclass;
	
	public Object getDclass() {
		return dclass;
	}

	public void setDclass(Object dclass) {
		this.dclass = dclass;
	}

	Calendar c=Calendar.getInstance();//現在時間
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");	
	
	public String execute() throws ParseException{
		
		String Oid=request.getParameter("Oid");
		String date=request.getParameter("date");
		String week=request.getParameter("week");
		
		
		try{//立即寫入點名計錄
			df.exSql("INSERT INTO DilgLog(date, Dtime_oid,due)VALUES" +
			"('"+date+"','"+Oid+"',"+df.sqlGetInt("SELECT COUNT(*)FROM Seld WHERE Dtime_oid="+Oid)+")");
		}catch(Exception e){
			//重複
		}
		
		Map map=df.sqlGetMap("SELECT d.Oid, c.ClassName, cs.chi_name FROM " +
		"Dtime d, Class c, Csno cs WHERE cs.cscode=d.cscode AND d.depart_class=c.ClassNo  AND d.Oid="+Oid);
		map.put("week", week);
		map.put("date", date);
		request.setAttribute("info", map);
		
		
		//判斷取全班或取分組
		List stds;
		if(df.sqlGetInt("SELECT COUNT(*) FROM Dtime_teacher dt WHERE dt.Dtime_oid="+Oid+" AND dt.teach_id='"+request.getParameter("id")+"'")>0){
			stds=sam.Dilg_student_date_mulTeacher(Oid, date, week, request.getParameter("id"));//取分組
		}else{
			stds=sam.Dilg_student_date(Oid, date, week);//取全班
		}
		
		this.setList(stds);		
		this.setDclass(sam.Dtime_class(Oid, week));
		
		return SUCCESS;
	}
	
	

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
}
