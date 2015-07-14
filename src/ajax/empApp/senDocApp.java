package ajax.empApp;

import java.util.Date;
import java.util.Map;

import model.DilgApply;
import model.DilgApplyHist;
import action.BaseAction;

/**
 * 教師核假
 * @author John
 */
public class senDocApp extends BaseAction{

	public String execute() {
		String result=request.getParameter("result");
		String reply=request.getParameter("reply");
		String Oid=request.getParameter("Oid");
		
		if(!result.equals("")){				
			//不核準的情況，無需後送直接寫為不核準
			if(result.equals("2")){
				df.exSql("UPDATE Dilg_apply SET result='2', reply='"+reply+"' WHERE Oid="+Oid);//假單狀態改為不核	
				df.exSql("UPDATE Dilg SET abs='2' WHERE Dilg_app_oid="+Oid);//假單中的課均改為缺課
			}else{					
				//核准的情況
				DilgApply d=(DilgApply)df.hqlGetListBy("FROM DilgApply WHERE Oid="+Oid).get(0);					
				if(d.getDefaultLevel().equals(d.getRealLevel())){
					//若預設層級與目前層級相同
					d.setResult("1");//可以判斷結果
					d.setReply(reply);
					df.update(d);//寫入結果並結案
					df.exSql("UPDATE Dilg SET abs='"+d.getAbs()+"' WHERE Dilg_app_oid="+Oid);//假單中的課均改為假別
				}else{
					//預設層級與目前層級不同時需後送，不寫入結果但要寫入歷程
					d.setResult(null);//後送待審中
					d.setReply(reply);
					d.setRealLevel(String.valueOf(Integer.parseInt(d.getRealLevel())+1));
					//尋找下一層審核者
					d.setAuditor((df.sqlGetStr("SELECT d.idno FROM stmd s, Dilg_charge d, Class c WHERE " +
					"s.depart_class=c.ClassNo AND d.CampusNo=c.CampusNo AND d.SchoolType=c.SchoolType AND " +
					"d.level='"+d.getRealLevel()+"' AND s.student_no='"+d.getStudent_no()+"'")));
					df.update(d);//後送
					df.exSql("UPDATE Dilg SET abs='"+d.getAbs()+"' WHERE Dilg_app_oid="+Oid);//假單中的課均改為假別
					//建立歷程
					DilgApplyHist dah=new DilgApplyHist();
					dah.setAuditor((String)getSession().getAttribute("userid"));
					dah.setDate(new Date());
					dah.setDilg_app_oid(d.getOid());
					df.update(dah);//寫入歷程
				}					
			}				
		}			
		
		return SUCCESS;
    } 
}