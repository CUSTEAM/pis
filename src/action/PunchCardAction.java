package action;

public class PunchCardAction extends BaseAction{

	public String execute() {
		response.setHeader("Access-Control-Allow-Origin","*");
		
		return SUCCESS;
	}
	
}
