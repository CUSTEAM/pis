package print.dtime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import action.BaseAction;

/**
 * 班級、教室、教師、學生等公用課表
 * TODO 2016/8/22 為進修部修改為28節顯示與相關細部判斷邏輯, 強烈建議儘快恢復原先版本或新建公用課表程式
 * @author shawn
 *
 */
public class TimeTable extends BaseAction{
	
	/**
	 * TODO 
	 */
	public String execute() throws IOException{
		
		String ClassNo=request.getParameter("ClassNo");
		String student_no=request.getParameter("student_no");
		String emplOid=request.getParameter("emplOid");
		String nabbr=request.getParameter("nabbr");
		String nonStay=request.getParameter("nonStay");
		
		if(ClassNo!=null){
			print(response, df.sqlGet("SELECT Oid FROM Dtime WHERE depart_class='"+ClassNo+"'"), null, null,null, getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(),null);			
		}
		if(student_no!=null){
			print(response, null, null, df.sqlGet("SELECT student_no FROM stmd WHERE student_no='"+student_no+"'"),null, getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(),null);			
		}
		
		//取單1教師
		if(emplOid!=null){
			print(response, null, df.sqlGet("SELECT Oid as emplOid FROM empl WHERE Oid="+emplOid),null, null, getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(),nonStay);			
		}
		
		if(nabbr!=null){
			print(response, null, null,null, df.sqlGet("SELECT room_id FROM Nabbr WHERE room_id='"+nabbr+"'"), getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(),null);			
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param response
	 * @param dtimeList
	 * @param emplList
	 * @param stdsList
	 * @param nabbrList
	 * @param Syear
	 * @param Sterm
	 * @throws IOException
	 */
	public void print(HttpServletResponse response,List<Map>dtimeList, List<Map>emplList, List<Map>stdsList, List<Map>nabbrList, String Syear, String Sterm, String nonStay) throws IOException{
		
		StringBuilder sb;
		List<Map>list=new ArrayList();
		String title="";
		
		Date date=new Date();
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");		
		PrintWriter out=response.getWriter();
		//class
		if(dtimeList!=null){
			sb=new StringBuilder("SELECT c.ClassNo as No, c.ClassName as Name FROM "
			+ "Dtime d, Class c WHERE c.ClassNo=d.depart_class AND d.Oid IN(");
			for(int i=0; i<dtimeList.size(); i++){
				sb.append(dtimeList.get(i).get("Oid")+",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")GROUP BY depart_class ORDER BY c.ClassNo");
			list=df.sqlGet(sb.toString());		
			
			for(int i=0; i<list.size(); i++){
				list.get(i).putAll(df.sqlGetMap("SELECT SUM(d.thour)as thour, SUM(d.credit)as credit, COUNT(*)as cnt FROM Dtime d, Class c WHERE d.Sterm='"+Sterm+"'AND d.depart_class=c.ClassNo AND c.ClassNo='"+list.get(i).get("No")+"'"));
				list.get(i).put("cs", df.sqlGet("SELECT d.Oid, dc.week, dc.begin, dc.end, IFNULL(dc.place,'')as place,"
						+ "cs.chi_name,IFNULL(e.cname, '')as cname FROM Dtime d LEFT OUTER JOIN empl e ON "
						+ "d.techid=e.idno, Dtime_class dc, Class c, Csno cs WHERE d.Sterm='"+Sterm+"'AND cs.cscode=d.cscode AND "
						+ "c.ClassNo=d.depart_class AND d.Oid=dc.Dtime_oid AND d.depart_class ='"+list.get(i).get("No")+"'"));
			}
		}
		
		//studends
		if(stdsList!=null){
			title="同學";
			sb=new StringBuilder("SELECT student_no as No, student_name as Name FROM stmd WHERE student_no IN(");
			for(int i=0; i<stdsList.size(); i++){
				sb.append("'"+stdsList.get(i).get("student_no")+"',");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")ORDER BY student_no");
			list=df.sqlGet(sb.toString());
			for(int i=0; i<list.size(); i++){
				list.get(i).putAll(df.sqlGetMap("SELECT SUM(d.thour)as thour, SUM(d.credit)as credit, COUNT(*)as cnt FROM Dtime d, Seld s WHERE d.Sterm='"+Sterm+"'AND d.Oid=s.Dtime_oid AND s.student_no='"+list.get(i).get("No")+"'"));
				list.get(i).put("cs", df.sqlGet("SELECT c.ClassName,dc.week, dc.begin, dc.end, IFNULL(dc.place,'')as place,"
				+ "cs.chi_name,IFNULL(e.cname, '')as cname FROM Dtime d LEFT OUTER JOIN empl e ON "
				+ "d.techid=e.idno, Dtime_class dc, Class c, Csno cs, Seld s WHERE s.Dtime_oid=d.Oid AND "
				+ "d.Sterm='"+Sterm+"'AND cs.cscode=d.cscode AND c.ClassNo=d.depart_class AND d.Oid=dc.Dtime_oid AND "
				+ "s.student_no='"+list.get(i).get("No")+"'"));
			}
		}
		
		//empl
		if(emplList!=null){
			title="老師";
			sb=new StringBuilder("SELECT idno as No, cname as Name FROM empl WHERE Oid IN(");
			for(int i=0; i<emplList.size(); i++){
				sb.append(emplList.get(i).get("emplOid")+",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")ORDER BY unit DESC");
			list=df.sqlGet(sb.toString());
			List tmp;
			for(int i=0; i<list.size(); i++){				
				list.get(i).putAll(df.sqlGetMap("SELECT SUM(d.thour)+(SELECT IFNULL(SUM(Dtime.thour),0)FROM Dtime, "
				+ "Dtime_teacher WHERE Dtime.Oid=Dtime_teacher.Dtime_oid AND Dtime.Sterm='"+Sterm+"' AND "
				+ "Dtime_teacher.teach_id='"+list.get(i).get("No")+"')as thour,SUM(d.credit)+(SELECT IFNULL(SUM(Dtime.credit),0)"
				+ "FROM Dtime, Dtime_teacher WHERE Dtime.Oid=Dtime_teacher.Dtime_oid AND Dtime.Sterm='"+Sterm+"' AND "
				+ "Dtime_teacher.teach_id='"+list.get(i).get("No")+"')as credit,(COUNT(*)+(SELECT COUNT(*)FROM Dtime, "
				+ "Dtime_teacher WHERE Dtime.Oid=Dtime_teacher.Dtime_oid AND Dtime.Sterm='"+Sterm+"' AND "
				+ "Dtime_teacher.teach_id='"+list.get(i).get("No")+"'))as cnt FROM Dtime d, empl e WHERE d.Sterm='"+
				Sterm+"'AND d.techid=e.idno AND e.idno='"+list.get(i).get("No")+"'"));
				
				tmp=df.sqlGet("SELECT c.ClassName, dc.week, dc.begin, dc.end, IFNULL(dc.place,'')as place,"
				+ "cs.chi_name,IFNULL(e.cname, '')as cname FROM Dtime d LEFT OUTER JOIN empl e ON "
				+ "d.techid=e.idno, Dtime_class dc, Class c, Csno cs, empl e1 WHERE "
				+ "e1.idno=d.techid AND d.Sterm='"+Sterm+"'AND cs.cscode=d.cscode AND "
				+ "c.ClassNo=d.depart_class AND d.Oid=dc.Dtime_oid AND e1.idno='"+list.get(i).get("No")+"'");
				//多教師
				tmp.addAll(df.sqlGet("SELECT c.ClassName, dc.week, dc.begin, dc.end, IFNULL(dc.place,'')as "
				+ "place,cs.chi_name,IFNULL(e.cname, '')as cname FROM Dtime d, Dtime_teacher dt, Class c, "
				+ "Dtime_class dc, empl e, Csno cs WHERE e.idno=dt.teach_id AND cs.cscode=d.cscode AND d.Sterm='"+Sterm+"'AND "
				+ "c.ClassNo=d.depart_class AND dc.Dtime_oid=d.Oid AND d.Oid=dt.Dtime_oid AND dt.teach_id='"+list.get(i).get("No")+"'"));
				
				
				//留校
				if(nonStay==null){//課務專用報表
					tmp.addAll(df.sqlGet("SELECT 'OFFICE HOUR'as chi_name,"
					+ "e.week,e.period as begin,e.period as end,IFNULL(ep.place,'未設定地點')as place, (CASE WHEN e.kind='1'THEN '課後輔導'ELSE '生活輔導'END )as ClassName, ''as cname FROM Empl_stay_info e LEFT OUTER "
					+ "JOIN Empl_stay_place ep ON e.idno=ep.idno WHERE e.school_term='"+Sterm+"'AND e.school_year='"+Syear+"' AND e.idno='"+list.get(i).get("No")+"'"));	
				}
				list.get(i).put("cs", tmp);
				
			}
		}
		
		//nabbr
		if(nabbrList!=null){
			title="";			
			
			if(nabbrList.get(0).get("Oid")==null){//來自外部連結
				sb=new StringBuilder("SELECT dc.place FROM Dtime d, Dtime_class dc WHERE dc.place IS NOT NULL AND dc.place!='' AND "
						+ "d.Oid=dc.Dtime_oid AND dc.place IN(");
				for(int i=0; i<nabbrList.size(); i++){
					sb.append("'"+nabbrList.get(i).get("room_id")+"',");
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append(")");
				
			}else{//來自內部連結(課程管理...)
				sb=new StringBuilder("SELECT dc.place FROM Dtime d, Dtime_class dc WHERE dc.place IS NOT NULL AND dc.place!='' AND "
						+ "d.Oid=dc.Dtime_oid AND d.Oid IN(");
						for(int i=0; i<nabbrList.size(); i++){
							sb.append(nabbrList.get(i).get("Oid")+",");
						}
						sb.delete(sb.length()-1, sb.length());
						sb.append(")GROUP BY dc.place");
						nabbrList=df.sqlGet(sb.toString());
			}
			try{
				nabbrList=df.sqlGet(sb.toString());
			}catch(Exception e){
					out.println ("Not enough columns given to draw the requested table.");
				return;
			}	
			
			sb=new StringBuilder("SELECT room_id as No, room_id as Name FROM Nabbr WHERE room_id IN(");
			for(int i=0; i<nabbrList.size(); i++){
				sb.append("'"+nabbrList.get(i).get("place")+"',");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")");
			list=df.sqlGet(sb.toString());
			for(int i=0; i<list.size(); i++){
				list.get(i).putAll(df.sqlGetMap("SELECT SUM(d.thour)as thour, SUM(d.credit)as credit, COUNT(*)as cnt FROM Dtime d, Dtime_class dc WHERE d.Sterm='"+Sterm+"'AND dc.Dtime_oid=d.Oid AND dc.place='"+list.get(i).get("No")+"'"));
				list.get(i).put("cs", df.sqlGet("SELECT c.ClassName, dc.week, dc.begin, dc.end, IFNULL(dc.place,'')as place,"
				+ "cs.chi_name,IFNULL(e.cname, '')as cname FROM Dtime d LEFT OUTER JOIN empl e ON "
				+ "d.techid=e.idno, Dtime_class dc, Class c, Csno cs, Nabbr n WHERE "
				+ "dc.place=n.room_id AND d.Sterm='"+Sterm+"'AND cs.cscode=d.cscode AND "
				+ "c.ClassNo=d.depart_class AND d.Oid=dc.Dtime_oid AND n.room_id='"+list.get(i).get("No")+"'"));
			}
		}		
		
		List<Map>tmp;		
		out.println ("<?xml version='1.0'?>");
		out.println ("<?mso-application progid='Excel.Sheet'?>");
		out.println ("<Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:o='urn:schemas-microsoft-com:office:office'");
		out.println (" xmlns:x='urn:schemas-microsoft-com:office:excel'");
		out.println (" xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:html='http://www.w3.org/TR/REC-html40'>");
		out.println (" <DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <Author>shawn</Author>");
		out.println ("  <LastAuthor>shawn</LastAuthor>");
		out.println ("  <LastPrinted>2014-04-18T04:56:12Z</LastPrinted>");
		out.println ("  <Created>2014-04-18T01:20:17Z</Created>");
		out.println ("  <LastSaved>2014-04-18T04:59:41Z</LastSaved>");
		out.println ("  <Version>15.00</Version>");
		out.println (" </DocumentProperties>");
		out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <AllowPNG/>");
		out.println (" </OfficeDocumentSettings>");
		out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  <WindowHeight>12540</WindowHeight>");
		out.println ("  <WindowWidth>28800</WindowWidth>");
		out.println ("  <WindowTopX>0</WindowTopX>");
		out.println ("  <WindowTopY>0</WindowTopY>");
		out.println ("  <ProtectStructure>False</ProtectStructure>");
		out.println ("  <ProtectWindows>False</ProtectWindows>");
		out.println (" </ExcelWorkbook>");
		out.println (" <Styles>");
		out.println ("  <Style ss:ID='Default' ss:Name='Normal'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <Interior/>");
		out.println ("   <NumberFormat/>");
		out.println ("   <Protection/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s16'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Dash' ss:Weight='2' ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s17'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Dash' ss:Weight='2' ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s18'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Dash' ss:Weight='2' ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s19'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Dash' ss:Weight='2' ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s20'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Dash' ss:Weight='2' ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s28'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s29'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s30'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s32'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s33'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s34'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s35'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s37'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s38'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s39'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s40'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s41'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s42'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s43'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s44'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s45'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s48'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s49'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s50'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s51'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s53'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s54'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='14'");
		out.println ("    ss:Color='#262626'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s55'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='14'");
		out.println ("    ss:Color='#C00000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s56'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s57'>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s58'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:Indent='1'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333' ss:Bold='1'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s105'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s106'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s107'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s108'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s109'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s110'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s111'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s112'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Dash' ss:Weight='1' ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s113'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s114'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s115'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Dash' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Dash' ss:Weight='1' ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#333333'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s116'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'");
		out.println ("     ss:Color='#262626'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體 Light' x:CharSet='136' x:Family='Swiss' ss:Size='9'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");
		
		boolean extra;//15節標記
		for(int i=0; i<list.size(); i++){			
			out.println (" <Worksheet ss:Name='"+list.get(i).get("name")+"'>");
			out.println ("  <Table ss:ExpandedColumnCount='9' ss:ExpandedRowCount='17' x:FullColumns='1'");
			out.println ("   x:FullRows='1' ss:StyleID='s57' ss:DefaultColumnWidth='54'");
			out.println ("   ss:DefaultRowHeight='15.75'>");
			out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='15'/>");
			out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='80.25' ss:Span='4'/>");
			out.println ("   <Column ss:Index='7' ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='15'/>");
			out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='80.25' ss:Span='1'/>");
			out.println ("   <Row ss:Height='19.5' ss:StyleID='s56'>");
			out.println ("    <Cell ss:StyleID='s53'/>");
			out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期一</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期二</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期三</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期四</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期五</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s54'/>");
			out.println ("    <Cell ss:StyleID='s55'><Data ss:Type='String'>星期六</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s55'><Data ss:Type='String'>星期日</Data></Cell>");
			out.println ("   </Row>");
			
			tmp=(List) list.get(i).get("cs");
			extra=false;
			//1-1
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s105'><Data ss:Type='String'>1</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s28'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				//超過15節標記
				if(Integer.parseInt(tmp.get(j).get("end").toString())>=15)extra=true;
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					//tmp.remove(j);
				}
			}
			out.println ("</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s30'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s16'><Data ss:Type='String'>1</Data></Cell>");			
			out.println ("<Cell ss:StyleID='s28'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s106'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=1 && 1<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//2-8
			for(int k=2; k<=8; k++){
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
				out.println ("    <Cell ss:StyleID='s107'><Data ss:Type='String'>"+k+"</Data></Cell>");
				
				out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				
				out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>"+k+"</Data></Cell>");
				out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
				for(int j=0; j<tmp.size(); j++){
					if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
						out.println (tmp.get(j).get("chi_name")+"&#10;");
						if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
						//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
						if(emplList==null)out.println (tmp.get(j).get("cname"));
						if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
					}
				}
				out.println ("</Data></Cell>");
				out.println ("   </Row>");
			}
			
			//9-9			
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s109'><Data ss:Type='String'>9</Data></Cell>");
			out.println ("<Cell ss:StyleID='s37'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s39'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>9</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=9 && 9<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//10-10
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s110'><Data ss:Type='String'>10</Data></Cell>");
			out.println ("<Cell ss:StyleID='s40'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s42'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>10</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=10 && 10<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//11-11
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s111'><Data ss:Type='String'>1</Data></Cell>");
			out.println ("<Cell ss:StyleID='s43'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s44'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>11</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=11 && 11<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//12-12
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s112'><Data ss:Type='String'>2</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s18'><Data ss:Type='String'>12</Data></Cell>");
			out.println ("<Cell ss:StyleID='s45'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s113'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=12 && 12<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//13-13
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s112'><Data ss:Type='String'>3</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s18'><Data ss:Type='String'>13</Data></Cell>");
			out.println ("<Cell ss:StyleID='s45'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s113'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=13 && 13<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			//14-14
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s115'><Data ss:Type='String'>4</Data></Cell>");
			out.println ("<Cell ss:StyleID='s48'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s49'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s50'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s50'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s51'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s20'><Data ss:Type='String'>14</Data></Cell>");
			out.println ("<Cell ss:StyleID='s48'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s116'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=14 && 14<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null&&stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
			
			out.println ("   <Row>");
			out.println ("    <Cell ss:StyleID='s58'/>");
			out.println ("   </Row>");
			out.println ("   <Row>");
			out.println ("    <Cell ss:StyleID='s58'/>");
			out.println ("   </Row>");
			out.println ("  </Table>");
			out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
			out.println ("   <PageSetup>");
			out.println ("    <Header x:Margin='0.3'");
			out.println ("     x:Data='&amp;L&amp;&quot;微軟正黑體,標準&quot;&amp;24中華科技大學&amp;C&amp;&quot;微軟正黑體,粗體&quot;&amp;24 "+list.get(i).get("Name")+title+"&amp;R&amp;&quot;微軟正黑體,標準&quot;&amp;18 "+Syear+"學年度第"+Sterm+"學期'/>");
			out.println ("    <Footer x:Margin='0.3'");
			out.println ("     x:Data='&amp;L&amp;&quot;微軟正黑體,標準&quot;課程數 "+list.get(i).get("cnt")+"&#10;總時數 "+list.get(i).get("thour")+"&#10;總學分 "+list.get(i).get("credit")+"&amp;R&amp;&quot;微軟正黑體,標準&quot;列印時間 &amp;D &amp;T&#10;&amp;10所有資料依相關單位期末留存為準'/>");
			out.println ("    <PageMargins x:Bottom='1.0729166666666667' x:Left='0.25' x:Right='0.25'");
			out.println ("     x:Top='0.75'/>");
			out.println ("   </PageSetup>");
			out.println ("   <Print>");
			out.println ("    <ValidPrinterInfo/>");
			out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
			out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
			out.println ("    <VerticalResolution>-1</VerticalResolution>");
			out.println ("   </Print>");
			out.println ("   <Selected/>");
			out.println ("   <ProtectObjects>False</ProtectObjects>");
			out.println ("   <ProtectScenarios>False</ProtectScenarios>");
			out.println ("  </WorksheetOptions>");
			out.println (" </Worksheet>");	
			
			if(extra)
			extraPrint(out,Syear, Sterm, list.get(i).get("Name")+title, tmp, dtimeList, emplList, nabbrList, stdsList);
						
		}		
		
		
		
		out.println ("</Workbook>");
		out.println ("");
	}
	
	private void extraPrint(PrintWriter out,String Syear, String Sterm, String name, List<Map>tmp, List dtimeList, List emplList, List nabbrList, List stdsList){
		
		
		
		out.println (" <Worksheet ss:Name='"+name+"續'>");
		out.println ("  <Table ss:ExpandedColumnCount='9' ss:ExpandedRowCount='17' x:FullColumns='1'");
		out.println ("   x:FullRows='1' ss:StyleID='s57' ss:DefaultColumnWidth='54'");
		out.println ("   ss:DefaultRowHeight='15.75'>");
		out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='15'/>");
		out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='80.25' ss:Span='4'/>");
		out.println ("   <Column ss:Index='7' ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='15'/>");
		out.println ("   <Column ss:StyleID='s57' ss:AutoFitWidth='0' ss:Width='80.25' ss:Span='1'/>");
		out.println ("   <Row ss:Height='19.5' ss:StyleID='s56'>");
		out.println ("    <Cell ss:StyleID='s53'/>");
		out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期一</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期二</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期三</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期四</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s54'><Data ss:Type='String'>星期五</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s54'/>");
		out.println ("    <Cell ss:StyleID='s55'><Data ss:Type='String'>星期六</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s55'><Data ss:Type='String'>星期日</Data></Cell>");
		out.println ("   </Row>");
		
		
		
		//15-15
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s105'><Data ss:Type='String'>15</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s28'><Data ss:Type='String'>");
		
		for(int j=0; j<tmp.size(); j++){			
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				//tmp.remove(j);
			}
		}
		out.println ("</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s29'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s30'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s16'><Data ss:Type='String'>15</Data></Cell>");			
		out.println ("<Cell ss:StyleID='s28'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s106'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=15 && 15<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//16-22
		for(int k=16; k<=22; k++){
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
			out.println ("    <Cell ss:StyleID='s107'><Data ss:Type='String'>"+k+"</Data></Cell>");
			
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			
			out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>"+k+"</Data></Cell>");
			out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
			for(int j=0; j<tmp.size(); j++){
				if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=k && k<=Integer.parseInt(tmp.get(j).get("end").toString())){
					out.println (tmp.get(j).get("chi_name")+"&#10;");
					if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
					//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
					if(emplList==null)out.println (tmp.get(j).get("cname"));
					if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
				}
			}
			out.println ("</Data></Cell>");
			out.println ("   </Row>");
		}
		
		//23-23			
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s109'><Data ss:Type='String'>23</Data></Cell>");
		out.println ("<Cell ss:StyleID='s37'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s38'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s39'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>23</Data></Cell>");
		out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=23 && 23<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//24-24
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s110'><Data ss:Type='String'>24</Data></Cell>");
		out.println ("<Cell ss:StyleID='s40'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s41'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s42'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>24</Data></Cell>");
		out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=24 && 24<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//11-11
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s111'><Data ss:Type='String'>25</Data></Cell>");
		out.println ("<Cell ss:StyleID='s43'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s44'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s17'><Data ss:Type='String'>25</Data></Cell>");
		out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s108'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=25 && 25<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//26-26
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s112'><Data ss:Type='String'>26</Data></Cell>");
		out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s18'><Data ss:Type='String'>26</Data></Cell>");
		out.println ("<Cell ss:StyleID='s45'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s113'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=26 && 26<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//27-27
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s112'><Data ss:Type='String'>27</Data></Cell>");
		out.println ("<Cell ss:StyleID='s32'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s33'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s34'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s35'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s18'><Data ss:Type='String'>27</Data></Cell>");
		out.println ("<Cell ss:StyleID='s45'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s113'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=27 && 27<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		//28-28
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='51.75'>");
		out.println ("    <Cell ss:StyleID='s115'><Data ss:Type='String'>28</Data></Cell>");
		out.println ("<Cell ss:StyleID='s48'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("1")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s49'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("2")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s50'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("3")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s50'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("4")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s51'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("5")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s20'><Data ss:Type='String'>28</Data></Cell>");
		out.println ("<Cell ss:StyleID='s48'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("6")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null)out.println (tmp.get(j).get("ClassName")+"&#10;");else out.println (tmp.get(j).get("Oid")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("<Cell ss:StyleID='s116'><Data ss:Type='String'>");
		for(int j=0; j<tmp.size(); j++){
			if(tmp.get(j).get("week").toString().equals("7")&& Integer.parseInt(tmp.get(j).get("begin").toString())<=28 && 28<=Integer.parseInt(tmp.get(j).get("end").toString())){
				out.println (tmp.get(j).get("chi_name")+"&#10;");
				if(dtimeList==null&&stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				//if(stdsList!=null)out.println (tmp.get(j).get("ClassName")+"&#10;");
				if(emplList==null)out.println (tmp.get(j).get("cname"));
				if(nabbrList==null)out.println (tmp.get(j).get("place")+"&#10;");
			}
		}
		out.println ("</Data></Cell>");
		out.println ("   </Row>");
		
		out.println ("   <Row>");
		out.println ("    <Cell ss:StyleID='s58'/>");
		out.println ("   </Row>");
		out.println ("   <Row>");
		out.println ("    <Cell ss:StyleID='s58'/>");
		out.println ("   </Row>");
		out.println ("  </Table>");
		out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("   <PageSetup>");
		out.println ("    <Header x:Margin='0.3'");
		out.println ("     x:Data='&amp;L&amp;&quot;微軟正黑體,標準&quot;&amp;24中華科技大學&amp;C&amp;&quot;微軟正黑體,粗體&quot;&amp;24 "+name+"&amp;R&amp;&quot;微軟正黑體,標準&quot;&amp;18 "+Syear+"學年度第"+Sterm+"學期'/>");
		out.println ("    <Footer x:Margin='0.3'");
		out.println ("     x:Data='&amp;L&amp;&quot;微軟正黑體,標準&quot;列印時間 &amp;D &amp;T&#10;&amp;10所有資料依相關單位期末留存為準'/>");
		out.println ("    <PageMargins x:Bottom='1.0729166666666667' x:Left='0.25' x:Right='0.25'");
		out.println ("     x:Top='0.75'/>");
		out.println ("   </PageSetup>");
		out.println ("   <Print>");
		out.println ("    <ValidPrinterInfo/>");
		out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
		out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
		out.println ("    <VerticalResolution>-1</VerticalResolution>");
		out.println ("   </Print>");
		out.println ("   <Selected/>");
		out.println ("   <ProtectObjects>False</ProtectObjects>");
		out.println ("   <ProtectScenarios>False</ProtectScenarios>");
		out.println ("  </WorksheetOptions>");
		out.println (" </Worksheet>");
		
		
		
		
		
		
		
		
		
		
		
		
		
	
		
		
	}
	

}
