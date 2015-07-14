package ajax.empApp;
 
import java.util.List;
import java.util.Map;
import action.BaseAction;

public class getClsEvent extends BaseAction{
	
	private List list;	

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String execute(){
		
		setList(df.sqlGet("SELECT DATE_FORMAT(d.date,'%Y,%m,%d')as date,COUNT(*)as cnt,(SELECT COUNT(*)FROM Dilg WHERE "
		+ "student_no=st.student_no AND Dilg.date<=d.date AND Dilg.abs='2')as tot, "
		+ "st.student_no, st.student_name FROM Dilg d, stmd st, Class c WHERE "
		+ "d.student_no=st.student_no AND st.depart_class=c.ClassNo AND "
		+ "d.abs='2' AND c.ClassNo='"+request.getParameter("ClassNo")+"' GROUP BY st.student_no, d.date ORDER BY d.date DESC, st.student_no"));		
		
		/*for(int i=0; i<tmp.size(); i++){
			list[i]=tmp.get(i).get("idno")+","+tmp.get(i).get("cname")+" - "+tmp.get(i).get("edunit")+", "+tmp.get(i).get("unit");
		}	*/
		
		return SUCCESS;
	}
}