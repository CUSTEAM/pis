package action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import model.Message;

public class StdProfileEditAction extends BaseAction{
	
	public String idno;
	public String birthday;
	public String myStdNo;
	public String sex;
	public String divi;
	public String birth_county;
	public String curr_post;
	public String curr_addr;
	public String schl_name;
	public String grad_dept;
	public String perm_post;
	public String perm_addr;
	public String telephone;
	public String CellPhone;
	public String student_ename;
	public String parent_name;
	public String Email;
	
	public String BirthCountry;
	public String Aborigine;
	public String ForeignPlace;
	public String ParentAge;
	public String ParentCareer;
	public String EmergentPhone;
	public String EmergentCell;
	
	public String save(){
		Date now=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		request.setAttribute("group", df.sqlGet("SELECT idno,name FROM code5 WHERE category='group' ORDER BY sequence;"));
		request.setAttribute("Aborigine", df.sqlGet("SELECT Code,Name From Aborigine"));
		request.setAttribute("birth_county", df.sqlGet("SELECT no,name FROM code3 WHERE name LIKE'%市' OR name LIKE'%縣'"));
		/*
		if(sex.equals("")||divi.equals("")||birth_county.equals("")||curr_post.equals("")||curr_addr.equals("")||perm_post.equals("")||perm_addr.equals("")||
			telephone.equals("")||CellPhone.equals("")||parent_name.equals("")||Email.equals("")||EmergentPhone.equals("")||EmergentCell.equals("")){					
			
			request.setAttribute("std", df.sqlGetMap("SELECT s.student_no, s.student_name, c.ClassName, s.sex, s.divi, s.birth_county, "+
			"s.curr_post, s.curr_addr, s.perm_post, s.perm_addr, s.telephone, s.parent_name, s.CellPhone, s.student_ename, s.Email, "+
			"r.BirthCountry, r.Aborigine, r.ForeignPlace, r.BeforeSchool, r.ParentAge, r.ParentCareer, r.EmergentPhone, r.EmergentCell " +
			"FROM stmd s,Class c,RegistrationCard r WHERE s.depart_class=c.ClassNo AND s.student_no=r.StudentNo AND s.student_no='"+myStdNo+"'"));
			
			Message msg=new Message();
			msg.setError("必填資料未完成");
			savMessage(msg);
			return SUCCESS;
		}
		*/
		df.exSql("UPDATE stmd SET sex='"+sex+"', birth_county='"+birth_county+
		"', curr_post='"+curr_post+"', curr_addr='"+curr_addr+"', perm_post='"+perm_post+"', perm_addr='"+perm_addr+
		"', telephone='"+telephone+"', CellPhone='"+CellPhone+"', schl_name='"+schl_name+"', grad_dept='"+grad_dept+
		"', student_ename='"+student_ename+"', parent_name='"+parent_name+"', Email='"+Email+"' WHERE student_no='"+myStdNo+"'");
		
		df.exSql("UPDATE RegistrationCard SET BirthCountry='"+BirthCountry+"', Aborigine='"+Aborigine+"', ForeignPlace='"+ForeignPlace+
		"', ParentAge='"+ParentAge+"', ParentCareer='"+ParentCareer+"', EmergentPhone='"+EmergentPhone+
		"', EmergentCell='"+EmergentCell+"', LastModified='"+sf.format(now)+"' WHERE StudentNo='"+myStdNo+"'");		
		
		Message msg=new Message();
		if(sex.equals("")||birth_county.equals("")||curr_post.equals("")||curr_addr.equals("")||perm_post.equals("")||perm_addr.equals("")||
			telephone.equals("")||CellPhone.equals("")||parent_name.equals("")||Email.equals("")||EmergentPhone.equals("")||EmergentCell.equals("")){
			
			request.setAttribute("std", df.sqlGetMap("SELECT s.student_no, s.student_name, c.ClassName, s.sex, (SELECT c5.name FROM code5 c5 WHERE c5.category='group' AND c5.idno=s.divi) as divi, s.birth_county, "+
			"s.curr_post, s.curr_addr, s.schl_name, s.grad_dept, s.perm_post, s.perm_addr, s.telephone, s.parent_name, s.CellPhone, s.student_ename, s.Email, "+
			"r.BirthCountry, r.Aborigine, r.ForeignPlace, r.ParentAge, r.ParentCareer, r.EmergentPhone, r.EmergentCell " +
			"FROM stmd s,Class c,RegistrationCard r WHERE s.depart_class=c.ClassNo AND s.student_no=r.StudentNo AND s.student_no='"+myStdNo+"'"));
			
			msg.setError("請填寫所有必填欄位!");
			savMessage(msg);
			return SUCCESS;
		}else{
			msg.setSuccess("基本資料已儲存成功。");
			savMessage(msg);
			return SUCCESS;
		}

	}
	
	public String execute() throws Exception {		
		
		return SUCCESS;
	}
	
	public String login() throws ParseException{
		Message msg=new Message();
		try{
			birthday=bl.getBirthday(birthday);
		}catch(Exception e){
			msg.setError("生日應為YYMMDD, 如90年1月12日為:900112");
			savMessage(msg);
			return SUCCESS;
		}

		request.setAttribute("group", df.sqlGet("SELECT idno,name FROM code5 WHERE category='group' ORDER BY sequence;"));
		request.setAttribute("Aborigine", df.sqlGet("SELECT Code,Name From Aborigine"));
		request.setAttribute("birth_county", df.sqlGet("SELECT no,name FROM code3 WHERE name LIKE'%市' OR name LIKE'%縣'"));
		
		//stmd要有新生的班級、學號、姓名、身份證、生日，RegistrationCard要有新生的學號
		Map std=df.sqlGetMap("SELECT s.student_no, s.student_name, c.ClassName, s.sex, (SELECT c5.name FROM code5 c5 WHERE c5.category='group' AND c5.idno=s.divi) as divi, s.birth_county, "+
		"s.curr_post, s.curr_addr, s.schl_name, s.grad_dept, s.perm_post, s.perm_addr, s.telephone, s.parent_name, s.CellPhone, s.student_ename, s.Email, "+
		"r.BirthCountry, r.Aborigine, r.ForeignPlace, r.BeforeSchool, r.ParentAge, r.ParentCareer, r.EmergentPhone, r.EmergentCell " +
		"FROM stmd s,Class c,RegistrationCard r WHERE s.depart_class=c.ClassNo AND s.student_no=r.StudentNo " +
		"AND s.idno='"+idno+"' AND s.birthday='"+birthday+"'");
		
		if(std==null){			
			msg.setError("驗證錯誤");
			savMessage(msg);
			return SUCCESS;
		}else{
			getSession().setAttribute("myStdNo", std.get("student_no"));
			request.setAttribute("std", std);
			df.exSql("UPDATE stmd SET edited='1' WHERE student_no ='"+std.get("student_no")+"'");
		}
		
		return SUCCESS;
	}
}