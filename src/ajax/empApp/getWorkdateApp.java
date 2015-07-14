package ajax.empApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import action.BaseAction;

public class getWorkdateApp extends BaseAction{
	
	private List list;
	
	public String execute(){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf1=new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat cf=new SimpleDateFormat("M月d日");
		Calendar c1=Calendar.getInstance(), c=Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -3);
		c1.add(Calendar.WEEK_OF_YEAR, 1);
		List result=new ArrayList();
		Map tmp;		
		
		List<Map<String,String>>list=df.sqlGet("SELECT w.wdate, w.set_in, w.set_out, " +
		"w.real_in, w.real_out, w.shift FROM AMS_Workdate w " +
		"WHERE w.set_in IS NOT NULL AND w.idno='"+request.getParameter("id")+"' " +
		"AND w.wdate>='"+sf.format(c.getTime())+"' AND w.wdate<='"+sf.format(c1.getTime())+"' ORDER BY w.wdate");
		
		for(int i=0; i<list.size(); i++){
			tmp=new HashMap();
			tmp.put("wdate", cf.format(list.get(i).get("wdate")));
			tmp.put("set_in", sf1.format(list.get(i).get("set_in")));
			tmp.put("set_out", sf1.format(list.get(i).get("set_out")));
			if(list.get(i).get("real_in")!=null){
				tmp.put("real_in", sf1.format(list.get(i).get("real_in")));
			}else{
				tmp.put("real_in", "");
			}
			
			if(list.get(i).get("real_out")!=null){
				tmp.put("real_out", sf1.format(list.get(i).get("real_out")));
			}else{
				tmp.put("real_out", "");
			}
			result.add(tmp);
		}
		
		setList(result);
		return SUCCESS;
	}
	
	@JSON(format="yyyy年M月d日")
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	
}
