package ajax.empApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

/**
 * 取班級列表
 * @author John
 *
 */
public class getCntClass extends BaseAction{
	
	private List assistant;
	private List military;
	private List military_h;
	private List military_n;
	private List director;
	private List taught;
	
	
	public List getMilitary_h() {
		return military_h;
	}

	public void setMilitary_h(List military_h) {
		this.military_h = military_h;
	}

	public List getMilitary_n() {
		return military_n;
	}

	public void setMilitary_n(List military_n) {
		this.military_n = military_n;
	}

	
	
	public List getAssistant() {
		return assistant;
	}

	public void setAssistant(List assistant) {
		this.assistant = assistant;
	}

	public List getMilitary() {
		return military;
	}

	public void setMilitary(List military) {
		this.military = military;
	}

	public List getDirector() {
		return director;
	}

	public void setDirector(List director) {
		this.director = director;
	}

	public List getTaught() {
		return taught;
	}

	public void setTaught(List taught) {
		this.taught = taught;
	}

	public String execute(){
		
		
		String id=request.getParameter("id");
		//系助理
		setAssistant(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE DeptNo=(SELECT id FROM CODE_DEPT WHERE assistant='"+id+"')AND Type='P'ORDER BY ClassNo"));
		//系主任
		setDirector(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE DeptNo=(SELECT id FROM CODE_DEPT WHERE director='"+id+"')AND Type='P'"));
		//日教官
		setMilitary(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE DeptNo=(SELECT id FROM CODE_DEPT WHERE military='"+id+"')AND Type='P'"));
		//夜教官
		setMilitary_n(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE DeptNo=(SELECT id FROM CODE_DEPT WHERE military_n='"+id+"')AND Type='P'"));
		//學院教官
		setMilitary_h(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE DeptNo=(SELECT id FROM CODE_DEPT WHERE military_h='"+id+"')AND Type='P'"));
		//任課
		setTaught(df.sqlGet("SELECT ClassName, ClassNo FROM Class WHERE ClassNo IN(SELECT depart_class FROM Dtime WHERE techid='"+id+"' AND Sterm='"+getContext().getAttribute("school_term")+"')AND Type='P'"));
		return SUCCESS;
	}

	
	
}
