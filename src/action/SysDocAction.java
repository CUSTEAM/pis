package action;

import java.util.List;
import java.util.Map;

public class SysDocAction extends BaseAction{
	
	public String execute(){
		
		
		
		List<Map>sys=df.sqlGet("SELECT * FROM SYS WHERE parent=0");
		List<Map>subSys;
		for(int i=0; i<sys.size(); i++){
			subSys=df.sqlGet("SELECT * FROM SYS WHERE parent!=0 AND parent="+sys.get(i).get("Oid"));
			
			for(int j=0; j<subSys.size();j++){
				subSys.get(j).put("qa", df.sqlGet("SELECT * FROM SYS_DOC WHERE sys="+subSys.get(j).get("Oid")+" AND type='q'"));
				subSys.get(j).put("halp", df.sqlGet("SELECT * FROM SYS_DOC WHERE sys="+subSys.get(j).get("Oid")+" AND type='h'"));
			}
			sys.get(i).put("subSys", subSys);
			
		}
		
		request.setAttribute("sys", sys);
		
		return SUCCESS;
	}
}
