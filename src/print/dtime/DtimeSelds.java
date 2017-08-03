package print.dtime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import action.BaseAction;

public class DtimeSelds extends BaseAction{
	
	
	public String execute() throws IOException{
		
		String Oid=request.getParameter("Oid");
		List<Map>list=df.sqlGet("SELECT (select Class.ClassName from Class where Class.ClassNo=d.depart_class) as sclass,c.ClassName,cs.chi_name , st.student_no, st.student_name, IF(st.sex=1,'男','女')as sex ,IFNULL(e.cname,'')as cname FROM"
				+ "(Seld s LEFT OUTER JOIN Dtime_teacher dt ON s.Dtime_teacher=dt.Oid)LEFT OUTER JOIN empl e ON e.idno=dt.teach_id,"
				+ "stmd st, Class c,Dtime d, Csno cs WHERE cs.cscode=d.cscode AND d.oid="+Oid+" AND c.ClassNo=st.depart_class AND s.student_no=st.student_no AND s.Dtime_oid="+Oid+" ORDER BY c.DeptNo, s.student_no");
		
		Date date=new Date();
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");		
		PrintWriter out=response.getWriter();
		out.println("<html>");
		out.println("<head><style>body{background-color:#cccccc;}td{font-size:18px;color:#333333;font-family:新細明體;mso-number-format:\\@;}</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<table>");
		
		out.println("<tr>");
		out.println("<td>");
		out.println("			<table bgcolor='#ffffff'><tr><td>");
		out.println("			<table border='1'>");

		out.println("<tr>");
		out.println("<td>");
		out.println("			<table border='1' align='left' cellpadding='0' cellspacing='1' width='100%'>");
		out.println("				<tr height='24'>");
		out.println("					<td>開課班級</td>");
		out.println("					<td>開課科目</td>");
		out.println("					<td>學生班級</td>");
		out.println("					<td>學生學號</td>");
		out.println("					<td>姓名</td>");
		out.println("					<td>性別</td>");
		out.println("					<td>分組教師</td>");

		out.println("				</tr>");
		
		for (int i = 0; i < list.size(); i++) {
			out.println("				<tr height='24'>");
			out.println("					<td>"+list.get(i).get("sclass")+"</td>");
			out.println("					<td>"+list.get(i).get("chi_name")+"</td>");
			out.println("					<td>"+list.get(i).get("ClassName")+"</td>");
			out.println("					<td>"+list.get(i).get("student_no")+"</td>");
			out.println("					<td>"+list.get(i).get("student_name")+"</td>");
			out.println("					<td>"+list.get(i).get("sex")+"</td>");
			out.println("					<td>"+list.get(i).get("cname")+"</td>");

			out.println("				</tr>");

		}

		out.print("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</body>");

		out.println("</html>");
		
		
		
		
		out.close();
		out.flush();
		return null;
		
	}

}
