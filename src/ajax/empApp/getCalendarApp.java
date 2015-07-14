package ajax.empApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

public class getCalendarApp extends BaseAction{
	
	private List list;
	
	public String execute(){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat cf=new SimpleDateFormat("M月d日HH時mm分");
		List<Map>cal=df.sqlGet("SELECT c.name, c.place, c.begin, c.end FROM Calendar c WHERE c.account='"+request.getParameter("id")+"' AND c.begin>='"+sf.format(new Date())+"'ORDER BY c.begin");
		for(int i=0; i<cal.size(); i++){
			cal.get(i).put("begin", cf.format((Date)cal.get(i).get("begin")));
			cal.get(i).put("end", cf.format((Date)cal.get(i).get("end")));
		}
		this.setList(cal);
		return SUCCESS;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	
}
