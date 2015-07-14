package ajax.empApp;

import java.util.List;

import action.BaseAction;

public class getTimetableApp extends BaseAction{
	
	private List list;
	
	public String execute(){
		
		this.setList(df.sqlGet("SELECT cl.ClassName, d.Oid, d.thour, d.credit, c.cscode, c.chi_name,dc.* " +
				"FROM Dtime d, empl e, Dtime_class dc,Csno c, Class cl WHERE cl.ClassNo=d.depart_class AND d.techid=e.idno AND " +
				"c.cscode=d.cscode AND d.Oid=dc.Dtime_oid AND d.Sterm='"+getContext().getAttribute("school_term")+"' AND " +
				"d.techid='"+request.getParameter("id")+"' ORDER BY CAST(dc.begin AS UNSIGNED)"));
		
		
		
		return SUCCESS;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}
