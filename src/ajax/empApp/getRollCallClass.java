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

public class getRollCallClass extends BaseAction{
	
	private List list;
	
	Calendar c=Calendar.getInstance();//現在時間
	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");	
	
	public String execute() throws ParseException{
		
		
		Date rollcall_begin=sf.parse((String)getContext().getAttribute("rollcall_begin"));//取得點名開始日期
		Date rollcall_end=sf.parse((String)getContext().getAttribute("rollcall_end"));//取得䵞名結束日期	
		
		if(c.getTimeInMillis()>rollcall_end.getTime()){//若今天>學期結束時間			
			if((c.getTimeInMillis()-1296000000)<rollcall_end.getTime()){//今天扣1星期<學期結束時間
				c.setTime(rollcall_end);
			}
		}
		
		//每周點名模版
		List cls=df.sqlGet("SELECT d.Oid as dOid, cl.CampusNo,  cl.SchoolNo, " +
		"dc.*, cl.ClassName, cs.chi_name FROM Dtime d, Csno cs, Class cl, Dtime_class dc " +
		"WHERE d.cscode=cs.cscode AND d.depart_class=cl.ClassNo AND d.Oid=dc.Dtime_oid AND " +
		"d.Sterm='"+getContext().getAttribute("school_term")+"' AND d.techid='"+request.getParameter("id")+"' ORDER BY dc.begin");
		//1科目多教師
		cls.addAll(df.sqlGet("SELECT d.Oid as dOid, cl.CampusNo,  cl.SchoolNo, dc.*, cl.ClassName, cs.chi_name FROM " +
		"Dtime d, Csno cs, Class cl, Dtime_class dc, Dtime_teacher dt WHERE dt.Dtime_oid=d.Oid AND " +
		"d.cscode=cs.cscode AND d.depart_class=cl.ClassNo AND d.Oid=dc.Dtime_oid AND " +
		"d.Sterm='"+getContext().getAttribute("school_term")+"' AND dt.teach_id='"+request.getParameter("id")+"' ORDER BY dc.begin"));
		
		
		this.setList(bm.sortListByKey(getCallInfo(rollcall_begin, rollcall_end, cls, 8), "sdate", true));
		
		
		return SUCCESS;
	}
	
	
	
	
	private List getCallInfo(Date rollcall_begin, Date rollcall_end, List<Map>list, int day) throws ParseException{
		
		Map map;
		int week;		
		
		String DilgLog_date;
		String Dtime_oid;
		int DilgLog_date_due;	
		
		SimpleDateFormat cf=new SimpleDateFormat("M月d日");	
		
		List myCs=new ArrayList();
		
		List dilguneed=(List) getContext().getAttribute("holiday");//不需
		String nd;
		
		//要執行的天數
		for(int i=1; i<=day; i++){			
			//學期前後日子不點名
			if(c.getTimeInMillis()<rollcall_begin.getTime() || c.getTimeInMillis()>rollcall_end.getTime()){
				continue;
			}
			
			//星期與排課星期同步
			week=c.get(Calendar.DAY_OF_WEEK)-1;
			if(week==0){
				week=7;
			}	
			
			//所有課程中排課星期相同的課程加入點名
			for(int j=0; j<list.size(); j++){				
				if(!sam.Dilg_uneed(dilguneed, sf.format(c.getTime()))){
					continue;
				}
				
				if((int)list.get(j).get("week")==week){
					map=new HashMap();
					map.putAll(list.get(j));					
					DilgLog_date=sf.format(c.getTime());
					Dtime_oid=map.get("dOid").toString();					
					try{
						//當日應到
						DilgLog_date_due=sam.DilgLog_date_due(Dtime_oid, DilgLog_date);
					}catch(Exception e){
						DilgLog_date_due=0;
					}					
					map.put("odate", DilgLog_date);		
					map.put("date", cf.format(sf.parse(DilgLog_date)));
					map.put("sdate", c.getTimeInMillis());
					
					//檔日有點名記錄
					if(DilgLog_date_due>0){
						map.put("select", DilgLog_date_due);
						map.put("info", sam.Dilg_info(Dtime_oid, DilgLog_date));
						map.put("log", true);
					}else{
						map.put("select", sam.Seld_count(Dtime_oid));
						map.put("info", null);
						map.put("log", false);
					}
					
					myCs.add(map);
				}		
								
			}
			//1天完畢下1天
			c.add(Calendar.DAY_OF_YEAR, -1);
		}
		return myCs;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
}
