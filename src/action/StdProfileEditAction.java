package action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import model.Message;

public class StdProfileEditAction extends BaseAction{
	
	public String idno;
	public String birthday;
	
	
	//public String sex;
	public String birth_province;
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
	public String ident_remark;
	
	public String AborigineCode, liner;
	
	public String save(){
		Date now=new Date();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		//request.setAttribute("group", df.sqlGet("SELECT idno,name FROM code5 WHERE category='group' ORDER BY sequence;"));
		//request.setAttribute("Aborigine", df.sqlGet("SELECT Code,Name From Aborigine"));
		request.setAttribute("birth_county", df.sqlGet("SELECT no,name FROM code3 WHERE name LIKE'%市' OR name LIKE'%縣'"));
		
		StringBuilder sql=new StringBuilder("UPDATE stmd SET ");
		if(!birth_province.equals(""))sql.append("birth_province='"+birth_province+"',");
		if(!birth_county.equals(""))sql.append("birth_county='"+birth_county+"',");
		if(!AborigineCode.equals(""))sql.append("AborigineCode='"+AborigineCode+"',");
		if(!ident_remark.equals(""))sql.append("ident_remark='"+ident_remark+"',");
		if(!schl_name.equals(""))sql.append("schl_name='"+schl_name+"',");
		if(!grad_dept.equals(""))sql.append("grad_dept='"+grad_dept+"',");
		if(!student_ename.equals(""))sql.append("student_ename='"+student_ename+"',");
		
		if(!parent_name.equals(""))sql.append("parent_name='"+parent_name+"',");
		if(!telephone.equals(""))sql.append("telephone='"+telephone+"',");
		if(!curr_post.equals(""))sql.append("curr_post='"+curr_post+"',");
		if(!curr_addr.equals(""))sql.append("curr_addr='"+curr_addr+"',");
		if(!liner.equals(""))sql.append("liner='"+liner+"',");
		
		if(!CellPhone.equals(""))sql.append("CellPhone='"+CellPhone+"',");
		if(!perm_post.equals(""))sql.append("perm_post='"+perm_post+"',");
		if(!perm_addr.equals(""))sql.append("perm_addr='"+perm_addr+"',");
		if(!Email.equals(""))sql.append("Email='"+Email+"',");
		
		/*if(!birth_province.equals("")){sql.append("birth_province='"+birth_province+"',");}else{sql.append("birth_province=null,");}
		if(!birth_county.equals("")){sql.append("birth_county='"+birth_county+"',");}else{sql.append("birth_county=null,");}
		if(!AborigineCode.equals("")){sql.append("AborigineCode='"+AborigineCode+"',");}else{sql.append("AborigineCode=null,");}
		if(!ident_remark.equals("")){sql.append("ident_remark='"+ident_remark+"',");}else{sql.append("ident_remark=null,");}
		if(!schl_name.equals("")){sql.append("schl_name='"+schl_name+"',");}else{sql.append("schl_name=null,");}
		if(!grad_dept.equals("")){sql.append("grad_dept='"+grad_dept+"',");}else{sql.append("grad_dept=null,");}
		if(!student_ename.equals("")){sql.append("student_ename='"+student_ename+"',");}else{sql.append("student_ename=null,");}
		
		if(!parent_name.equals("")){sql.append("parent_name='"+parent_name+"',");}else{sql.append("parent_name=null,");}
		if(!telephone.equals("")){sql.append("telephone='"+telephone+"',");}else{sql.append("telephone=null,");}
		if(!curr_post.equals("")){sql.append("curr_post='"+curr_post+"',");}else{sql.append("curr_post=null,");}
		if(!curr_addr.equals("")){sql.append("curr_addr='"+curr_addr+"',");}else{sql.append("curr_addr=null,");}
		if(!liner.equals("")){sql.append("liner='"+liner+"',");}else{sql.append("liner=null,");}
		
		if(!CellPhone.equals("")){sql.append("CellPhone='"+CellPhone+"',");}else{sql.append("CellPhone=null,");}
		if(!perm_post.equals("")){sql.append("perm_post='"+perm_post+"',");}else{sql.append("perm_post=null,");}
		if(!perm_addr.equals("")){sql.append("perm_addr='"+perm_addr+"',");}else{sql.append("perm_addr=null,");}
		if(!Email.equals("")){sql.append("Email='"+Email+"',");}else{sql.append("Email=null,");}*/
		
		sql.delete(sql.length()-1, sql.length());
		sql.append("WHERE student_no='"+getSession().getAttribute("myStdNo").toString()+"'");
		
		
		df.exSql(sql.toString());
		
		
		//Message msg=new Message();
		
		request.setAttribute("std", getStd(getSession().getAttribute("myStdNo").toString(), null, null));
		return SUCCESS;
	}
	
	private Map getStd(String stdNo, String id, String bd){		
		StringBuilder sql=new StringBuilder("SELECT c.ClassName,s.* FROM stmd s,Class c WHERE s.depart_class=c.ClassNo ");
		if(stdNo!=null){
			sql.append("AND s.student_no='"+stdNo+"'");
		}else{
			sql.append("AND s.idno='"+id+"'AND s.birthday='"+bd+"'");
		}
		return df.sqlGetMap(sql.toString());
	}
	
	public String execute() throws Exception {		
		
		return SUCCESS;
	}
	
	public String login() throws ParseException{
		

		request.setAttribute("group", df.sqlGet("SELECT idno,name FROM code5 WHERE category='group' ORDER BY sequence;"));
		request.setAttribute("Aborigine", df.sqlGet("SELECT Code,Name From Aborigine"));
		request.setAttribute("birth_county", df.sqlGet("SELECT no,name FROM code3 WHERE name LIKE'%市' OR name LIKE'%縣'"));
		
		//stmd要有新生的班級、學號、姓名、身份證、生日，RegistrationCard要有新生的學號
		Map std=getStd(null, idno, birthday);
		
		if(std==null){	
			Message msg=new Message();
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