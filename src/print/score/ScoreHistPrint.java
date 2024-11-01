package print.score;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import action.BasePrintXmlAction;



/**
 * 提供外部成績單
 * http://john.cust.edu.tw/pis/ScoreHistPrint?stdno=91AD013&bd=67-07-08
 * stdno=學號
 * bd=出生日期yyy-mm-dd
 * @author John
 *
 */
public class ScoreHistPrint extends BasePrintXmlAction{
	
	private List<Map>getStd(String stdno, String bd){
		List stds;
		//StringBuilder sb=new StringBuilder("SELECT (SELECT COUNT(DISTINCT school_year) FROM ScoreHist WHERE student_no=s.student_no)as year, s.student_no, c.ClassName, s.idno, s.student_name FROM Class c, stmd s WHERE s.depart_class=c.ClassNo AND c.CampusNo='"+cno+"'");
		StringBuilder sb=new StringBuilder("SELECT c.graduate, c.SchNo, s.student_no, cs.name as SchoolName, cd.name as DeptName, s.idno, s.student_name FROM CODE_DEPT cd, CODE_SCHOOL cs, Class c, stmd s WHERE cs.id=c.SchoolNo AND c.DeptNo=cd.id AND s.depart_class=c.ClassNo AND s.student_no='"+stdno+"'AND birthday='"+bd+"'");
		try {
			stds=df.sqlGet(sb.toString());
		}catch(Exception e){
			return null;
		}
		
		try{
			sb=new StringBuilder("SELECT c.graduate, c.SchNo, s.student_no, cs.name as SchoolName, cd.name as DeptName, s.idno, s.student_name FROM CODE_DEPT cd, CODE_SCHOOL cs, Class c, Gstmd s WHERE cs.id=c.SchoolNo AND c.DeptNo=cd.id AND s.depart_class=c.ClassNo AND s.student_no='"+stdno+"'AND birthday='"+bd+"'");
			stds.addAll(df.sqlGet(sb.toString()));
		}catch(Exception e) {}
		return stds;
	}
	
	private List getScoreTerm(String stdNo, String year, String term, int pass){		
		return df.sqlGet("SELECT cs.chi_name, c.sname,IF(score<"+pass+", '*', '')as np, cs.cscode, s.credit, s.score FROM ScoreHist s, CODE_DTIME_OPT c, Csno cs WHERE "
		+ "s.student_no='"+stdNo+"'AND s.school_year='"+year+"'AND s.school_term='"+term+"'AND c.id=s.opt AND s.cscode=cs.cscode AND s.cscode!='99999'ORDER BY s.opt");
	}
	
	private List getScoreHist(String grad, String stdNo, String SchNo){
		int pass=60;
		if(SchNo.equals("M"))pass = 70;
		List<Map>years=df.sqlGet("SELECT DISTINCT school_year FROM ScoreHist WHERE student_no='"+stdNo+"'ORDER BY school_year");
		for(int i=0; i<years.size(); i++){			
			if(years.get(i).get("school_year")==null)continue;
			years.get(i).put("extra1", getExtraTerm(stdNo, years.get(i).get("school_year").toString(), "1", pass));
			years.get(i).put("term1", getScoreTerm(stdNo, years.get(i).get("school_year").toString(), "1", pass));
			years.get(i).put("extra2", getExtraTerm(stdNo, years.get(i).get("school_year").toString(), "2", pass));
			years.get(i).put("term2", getScoreTerm(stdNo, years.get(i).get("school_year").toString(), "2", pass));
			//System.out.println(years.get(i));			
		}
		if(grad.equals("0")){
			return years;//非畢業班
		}else{			
			Map m=new HashMap();
			m=df.sqlGetMap("SELECT ''as Gmark, ''as cond,(SELECT SUM(case when (score>="+pass+" OR score IS NULL) then credit else 0 end)FROM ScoreHist WHERE student_no=se.student_no)as cnt,"
			+ "SUM(dt.credit)as credit FROM Seld se, Dtime dt WHERE se.Dtime_oid=dt.Oid AND dt.Sterm='"+getContext().getAttribute("school_term")+"' AND se.student_no='"+stdNo+"'");
			
			try{
				m.put("cnt", Float.parseFloat(m.get("cnt").toString())+Float.parseFloat(m.get("credit").toString()));
				years.get(years.size()-1).put("school_year", getContext().getAttribute("school_year"));
				years.get(years.size()-1).put("extra"+getContext().getAttribute("school_term"), m);
				years.get(years.size()-1).put("term"+getContext().getAttribute("school_term"), df.sqlGet("SELECT cd.sname, c.chi_name, c.cscode, d.credit, ''as score, ''as np FROM Seld s, Dtime d, Csno c, CODE_DTIME_OPT cd WHERE d.opt=cd.id AND c.cscode=d.cscode AND s.Dtime_oid=d.Oid AND d.Sterm='"+getContext().getAttribute("school_term")+"' AND s.student_no='"+stdNo+"'"));
				
			}catch(Exception e){
				
			}
			return years;
		}
		
		
	}
	
	private Map getExtraTerm(String stdNo, String year, String term, int pass){		
		StringBuilder sql=new StringBuilder("SELECT(SELECT remark FROM Gmark WHERE student_no='"+stdNo+"'AND school_year='"+year+"'AND school_term='"+term+"'LIMIT 1)as Gmark,"
		+ "(SELECT score FROM cond WHERE student_no='"+stdNo+"'AND school_year='"+year+"'AND school_term='"+term+"')as cond,"
		+ "(SELECT SUM(case when (score>="+pass+" OR score IS NULL) then credit else 0 end)FROM ");
		if(term.equals("2")){
			sql.append("ScoreHist WHERE student_no='"+stdNo+"'AND school_year<='"+year+"')as cnt, ");
		}else{
			sql.append("ScoreHist WHERE student_no='"+stdNo+"'AND (school_year<'"+year+"'OR(school_year='"+year+"'AND school_term='1')))as cnt, ");
		}		
		//sql.append("ROUND(AVG(score),1)as score,SUM(case when (score>="+pass+" OR score IS NULL) then credit else 0 end)as credit "
		sql.append("ROUND(SUM(score*credit)/SUM(credit), 1)as score, ");
		sql.append("SUM(case when (score>="+pass+" OR score IS NULL) then credit else 0 end)as credit ");
		sql.append("FROM ScoreHist WHERE student_no='"+stdNo+"'AND school_year='"+year+"'AND school_term='"+term+"'AND cscode!='99999'");
		//System.out.println(sql);
		Map m=df.sqlGetMap(sql.toString());
		
		//System.out.println(m);
		if(m.get("score")!=null){
			return m;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 西元民國隨便轉	 * 
	 * @param someday  西元年或民國年隨便1天 '/', '-' 都可接受
	 * @return 傳西元進來丟民國, 傳民國進來丟西元 ex: in 96/11/5 out 2007/11/5; in 2007-11-5 out 96-11-5
	 */
	public String convertDate (String someday){
	    SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat d2 = new SimpleDateFormat("-MM-dd");
	    Calendar cal = Calendar.getInstance();
	    
	    StringBuilder sb=new StringBuilder(someday);
	    for(int i=0; i<sb.length(); i++){
	    	
	    	try{
	    	      Integer.parseInt(sb.charAt(i)+"");	    	      
	    	    }catch(NumberFormatException e){
	    	      sb.replace(i, i+1, "-");
	    	    }
	    }
	    
	    someday=sb.toString();
	    try {
	    	try{
	    		cal.setTime(d1.parse(someday));
	    	}catch(Exception e){
	    		d1 = new SimpleDateFormat("yyyy-MM-dd");
	    	    d2 = new SimpleDateFormat("-MM-dd");
	    	   // cal = Calendar.getInstance();
	    	    cal.setTime(d1.parse(someday));
	    	}
	    	if(cal.get(Calendar.YEAR)>1492){
	    	  cal.add(Calendar.YEAR,-1911);
	    	}else{
	    	cal.add(Calendar.YEAR,+1911);
	      }
	      return Integer.toString(cal.get(Calendar.YEAR))+d2.format(cal.getTime());
	    } catch (Exception e) {
	      // e.printStackTrace();
	      return "日期格式有誤";
	    }
	}
	
	public String execute() throws IOException, ParseException{
		
		String stdno="";
		try {
			try {
				stdno=df.sqlGetStr("SELECT student_no FROM Gstmd WHERE idno='"+request.getParameter("stdno")+"'");
			}catch(Exception e) {
				stdno=df.sqlGetStr("SELECT student_no FROM stmd WHERE idno='"+request.getParameter("stdno")+"'");
			}
		}catch(Exception exe) {
			try {
				stdno=df.sqlGetStr("SELECT student_no FROM Gstmd WHERE student_no='"+request.getParameter("stdno")+"'");
			}catch(Exception e) {
				stdno=df.sqlGetStr("SELECT student_no FROM stmd WHERE student_no='"+request.getParameter("stdno")+"'");
			}
		}
		
		
		response.sendRedirect("http://ap.cust.edu.tw/CIS/Course/StudentScoreHistory.do?no="+stdno);
		/*String bd=request.getParameter("bd");
		
		bd=convertDate(bd);
		List<Map>stds=getStd(stdno, bd);
		
		//成績單外部單筆進入點
		if(stds!=null){
						
			for(int i=0; i<stds.size(); i++){		
				stds.get(i).put("MasterData", df.sqlGetMap("SELECT*FROM MasterData WHERE student_no='"+stds.get(i).get("student_no")+"'"));
				stds.get(i).put("years", getScoreHist(stds.get(i).get("graduate").toString(), stds.get(i).get("student_no").toString(), stds.get(i).get("SchNo").toString()));			
			}		
			ScoreHistPrint p=new ScoreHistPrint();
			p.print(response, stds);
			//ap.cust.edu.tw/pis/ScoreHistPrint?stdno=A123456789&bd=88-08-08
			response.sendRedirect(request.getContextPath() + "/CIS/Course/StudentScoreHistory.do?no="+st);
			return null;
			
		}else {
			PrintWriter out=response.getWriter();		
			
			out.println ("verification failed ");
			
			out.close();
			out.flush();
			return null;
		}*/
		
		return null;
	}
	
	/**
	 * 成績單
	 * 為導覽機製作的
	 * @param response
	 * @param stds
	 * @throws IOException
	 */
	public void print(HttpServletResponse response, List<Map>stds) throws IOException{		
		
		Date date=new Date();
		xml2ods(response, getRequest(), date);
		PrintWriter out=response.getWriter();		
		
		out.println ("<?xml version='1.0'?>");
		out.println ("<?mso-application progid='Excel.Sheet'?>");
		out.println ("<Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:o='urn:schemas-microsoft-com:office:office'");
		out.println (" xmlns:x='urn:schemas-microsoft-com:office:excel'");
		out.println (" xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:html='http://www.w3.org/TR/REC-html40'>");
		out.println (" <DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <Author>Oscar Wei</Author>");
		out.println ("  <LastAuthor>John</LastAuthor>");
		out.println ("  <LastPrinted>2018-04-07T05:31:23Z</LastPrinted>");
		out.println ("  <Created>2008-01-01T02:41:06Z</Created>");
		out.println ("  <LastSaved>2018-04-09T01:41:20Z</LastSaved>");
		out.println ("  <Version>15.00</Version>");
		out.println (" </DocumentProperties>");
		out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <AllowPNG/>");
		out.println (" </OfficeDocumentSettings>");
		out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  <WindowHeight>9345</WindowHeight>");
		out.println ("  <WindowWidth>15480</WindowWidth>");
		out.println ("  <WindowTopX>0</WindowTopX>");
		out.println ("  <WindowTopY>30</WindowTopY>");
		out.println ("  <ProtectStructure>False</ProtectStructure>");
		out.println ("  <ProtectWindows>False</ProtectWindows>");
		out.println (" </ExcelWorkbook>");
		out.println (" <Styles>");
		out.println ("  <Style ss:ID='Default' ss:Name='Normal'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'/>");
		out.println ("   <Interior/>");
		out.println ("   <NumberFormat/>");
		out.println ("   <Protection/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591668'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591688'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591708'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591748'>");
		out.println ("   <Alignment ss:Horizontal='JustifyDistributed' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591768'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='10'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591788'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='5'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591044'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='10'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591064'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='5'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591084'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591104'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591124'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='5'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591144'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591164'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017591184'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584784'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1' ss:ShrinkToFit='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='10'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584804'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1' ss:ShrinkToFit='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584824'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1' ss:ShrinkToFit='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584844'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1' ss:ShrinkToFit='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584864'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='10'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584884'>");
		out.println ("   <Alignment ss:Horizontal='Distributed' ss:Vertical='Center' ss:Indent='5'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584904'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584924'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584944'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017584964'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017587112'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017587252'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017581664'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017581684'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017581744'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017581804'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017594768'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017594828'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017585656'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017585676'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017585736'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017585796'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017582536'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017582556'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017582616'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m2197017582676'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s62'>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s64'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s65'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s70'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s71'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s72'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s73'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s74'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s75'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s99'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s101'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s104'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s106'>");
		out.println ("   <Alignment ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s127'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s128'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s131'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:ShrinkToFit='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s132'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");
		
		
		
		
		
		
		
		

		List<Map>year;
		/*
		List<Map>y1term1, y2term1, y3term1, y4term1;
		List<Map>y1term2, y2term2, y3term2, y4term2;
		Map y1extra1, y2extra1, y3extra1, y4extra1;
		Map y1extra2, y2extra2, y3extra2, y4extra2;
		*/
		
		List<Map> term1[], term2[];
		Map extra1[], extra2[];
		
		int p,m;
		int over, sy;
		for(int i=0; i<stds.size(); i++){	
			
			over=0;
			
			year=(List<Map>)stds.get(i).get("years");			
			
			p=year.size()/4;			
			if(year.size()>4&&p%4>0)p++;
			if(p==0)p++;			
			
			int t2[], e2[];
			term1=new List[year.size()];
			term2=new List[year.size()];
			extra1=new Map[year.size()];
			extra2=new Map[year.size()];
			for(int j=0; j<p; j++){
				
				for(int k=0; k<4; k++){
					try{term1[k]=null;term1[k]=(List<Map>)year.get(k+over).get("term1");}catch(Exception e){}
					try{term2[k]=null;term2[k]=(List<Map>)year.get(k+over).get("term2");}catch(Exception e){}
					try{extra1[k]=null;extra1[k]=(Map)year.get(k+over).get("extra1");}catch(Exception e){}
					try{extra2[k]=null;extra2[k]=(Map)year.get(k+over).get("extra2");}catch(Exception e){}
				}
				
				out.println (" <Worksheet ss:Name='"+stds.get(i).get("student_no")+"第"+(j+1)+"共"+p+"頁'>");
				out.println ("  <Table ss:ExpandedColumnCount='24' ss:ExpandedRowCount='40' x:FullColumns='1'");
				out.println ("   x:FullRows='1' ss:StyleID='s62' ss:DefaultColumnWidth='54'");
				out.println ("   ss:DefaultRowHeight='19.5'>");
				out.println ("   <Column ss:StyleID='s62' ss:Width='27.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:Width='169.5'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='39.75' ss:Span='3'/>");
				out.println ("   <Column ss:Index='7' ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='27.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='169.5'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='39.75' ss:Span='3'/>");
				out.println ("   <Column ss:Index='13' ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='27.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='169.5'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='39.75' ss:Span='3'/>");
				out.println ("   <Column ss:Index='19' ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='27.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='169.5'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='39.75' ss:Span='3'/>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'>");
				out.println ("    <Cell ss:Index='2' ss:MergeAcross='3' ss:StyleID='s127'><Data ss:Type='String'>學號："+stds.get(i).get("student_no")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s64'/>");
				out.println ("    <Cell ss:StyleID='s65'/>");
				out.println ("    <Cell ss:StyleID='s65'><Data ss:Type='String'>姓名："+stds.get(i).get("student_name")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s65'/>");
				out.println ("    <Cell ss:MergeAcross='8' ss:StyleID='s128'><Data ss:Type='String'>系所科別："+stds.get(i).get("DeptName")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s65'/>");
				out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='s128'><Data ss:Type='String'>身分證字號："+stds.get(i).get("idno")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				for(int y=0; y<4; y++){
					out.println ("    <Cell ss:MergeAcross='5' ss:StyleID='m2197017591748'><Data ss:Type='String'>第"+(y+over+1)+"學年</Data></Cell>");
				}
				out.println ("   </Row>");				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");				
				for(int k=0; k<4; k++){					
					try{
						sy=Integer.parseInt(year.get(k+over).get("school_year").toString());
						out.println ("    <Cell ss:MergeAcross='5' ss:StyleID='m2197017584884'><Data ss:Type='String'>"+sy+"年9月至"+(sy+1)+"年7月</Data></Cell>");
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='5' ss:StyleID='m2197017584884'><Data ss:Type='String'></Data></Cell>");
					}					
				}				
				out.println ("   </Row>");				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				out.println ("    <Cell ss:MergeAcross='1' ss:MergeDown='1' ss:StyleID='m2197017591144'><Data");
				out.println ("      ss:Type='String'>科 目 名 稱</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591164'><Data ss:Type='String'>第一學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591184'><Data ss:Type='String'>第二學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:MergeDown='1' ss:StyleID='m2197017591084'><Data");
				out.println ("      ss:Type='String'>科 目 名 稱</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591708'><Data ss:Type='String'>第一學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591104'><Data ss:Type='String'>第二學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:MergeDown='1' ss:StyleID='m2197017584964'><Data");
				out.println ("      ss:Type='String'>科 目 名 稱</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591668'><Data ss:Type='String'>第一學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017591688'><Data ss:Type='String'>第二學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:MergeDown='1' ss:StyleID='m2197017584904'><Data");
				out.println ("      ss:Type='String'>科 目 名 稱</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017584924'><Data ss:Type='String'>第一學期</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017584944'><Data ss:Type='String'>第二學期</Data></Cell>");
				out.println ("   </Row>");
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				out.println ("    <Cell ss:Index='3' ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:Index='9' ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:Index='15' ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:Index='21' ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>學分</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>成績</Data></Cell>");
				out.println ("   </Row>");
				
				t2=new int[4]; e2=new int[4];
				for(int x=0; x<27; x++){
					out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375' ss:StyleID='s74'>");
					//t2[0]=0;
					for(int y=0; y<4; y++){
						//選別							
						try{
							out.println ("    <Cell ss:StyleID='s72'><Data ss:Type='String'>"+term1[y].get(x).get("sname")+"</Data></Cell>");							
						}catch(Exception e1){							
							try{
								out.println ("    <Cell ss:StyleID='s72'><Data ss:Type='String'>"+term2[y].get(t2[y]).get("sname")+"</Data></Cell>");
							}catch(Exception ex1){
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
							}							
						}
						
						try{
							out.println ("    <Cell ss:StyleID='s131'><Data ss:Type='String'>"+term1[y].get(x).get("chi_name")+"</Data></Cell>");							
						}catch(Exception e1){							
							try{
								out.println ("    <Cell ss:StyleID='s131'><Data ss:Type='String'>"+term2[y].get(t2[y]).get("chi_name")+"</Data></Cell>");
							}catch(Exception ex1){
								out.println ("    <Cell ss:StyleID='s131'><Data ss:Type='String'></Data></Cell>");
							}							
						}
						
						try{
							out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>"+term1[y].get(x).get("credit")+"</Data></Cell>");
							if(term1[y].get(x).get("score")!=null){
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>"+term1[y].get(x).get("score")+term1[y].get(x).get("np")+"</Data></Cell>");
							}else{
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>抵</Data></Cell>");
							}
							out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
							out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
						}catch(Exception e1){								
							try{
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>"+term2[y].get(t2[y]).get("credit")+"</Data></Cell>");
								if(term2[y].get(t2[y]).get("score")!=null){
									out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>"+term2[y].get(t2[y]).get("score")+term2[y].get(t2[y]).get("np")+"</Data></Cell>");
								}else{
									out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'>抵</Data></Cell>");
								}	
								
								t2[y]+=1; //有名字不一定有分
							}catch(Exception ex1){
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
								out.println ("    <Cell ss:StyleID='s73'><Data ss:Type='String'></Data></Cell>");
							}							
						}					
					}				
					
					out.println ("   </Row>");
				}				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				
				for(int y=0; y<4; y++){
					out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s104'><Data ss:Type='String'>學業平均成績</Data></Cell>");
					try{						
						if(extra1[y].get("score")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'>"+extra1[y].get("score")+"</Data></Cell>");
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
						}					
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
					}
					try{
						if(extra2[y].get("score")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'>"+extra2[y].get("score")+"</Data></Cell>");
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
						}	
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
					}
					
				}
					
				
				
				out.println ("   </Row>");
				
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				for(int y=0; y<4; y++){					
					out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s106'><Data ss:Type='String'>實得學分數</Data></Cell>");
					try{						
						if(extra1[y].get("credit")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'>"+extra1[y].get("credit")+"</Data></Cell>");
							
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
						}					
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
					}
					try{
						if(extra2[y].get("credit")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017587252'><Data ss:Type='String'>"+extra2[y].get("credit")+"</Data></Cell>");
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017587252'><Data ss:Type='String'></Data></Cell>");
						}	
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017587252'><Data ss:Type='String'></Data></Cell>");
					}
				}
				out.println ("   </Row>");
				
				
				
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				for(int y=0; y<4; y++){	
					out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s106'><Data ss:Type='String'>學分累計</Data></Cell>");
					try{						
						if(extra1[y].get("cnt")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'>"+extra1[y].get("cnt")+"</Data></Cell>");
							
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
						}					
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
					}
					try{
						if(extra2[y].get("cnt")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581664'><Data ss:Type='String'>"+extra2[y].get("cnt")+"</Data></Cell>");
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581664'><Data ss:Type='String'></Data></Cell>");
						}	
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581664'><Data ss:Type='String'></Data></Cell>");
					}					
				}
				
				out.println ("   </Row>");
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='21.9375'>");
				
				
				
				
				
				for(int y=0; y<4; y++){	
					out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s99'><Data ss:Type='String'>操行成績</Data></Cell>");
					try{						
						if(extra1[y].get("cond")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'>"+extra1[y].get("cond")+"</Data></Cell>");
							
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
						}					
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='s101'><Data ss:Type='String'></Data></Cell>");
					}
					try{
						if(extra2[y].get("cond")!=null){
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581684'><Data ss:Type='String'>"+extra2[y].get("cond")+"</Data></Cell>");
						}else{
							out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581684'><Data ss:Type='String'></Data></Cell>");
						}	
					}catch(Exception e){
						out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m2197017581684'><Data ss:Type='String'></Data></Cell>");
					}					
				}
				
				out.println ("   </Row>");
				
				
				
				
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'>");
				
				
				for(int y=0; y<4; y++){					
					out.println ("    <Cell ss:MergeAcross='5' ss:MergeDown='2' ss:StyleID='m2197017584784'><Data ss:Type='String'>");
					try{
						m=1;
						if((term1[y].size()+term2[y].size())>27){
							//System.out.println(stds.get(i).get("student_no"));
							//System.out.println(term1[y].size()+term2[y].size());
							//out.print("下學期修課&#10;");
							for(int c=term1[y].size()+term2[y].size(); c>=27; c--){
								out.print("【"+term2[y].get(term2[y].size()-m).get("sname")+"】");
								out.print(term2[y].get(term2[y].size()-m).get("chi_name")+",學分");
								out.print(term2[y].get(term2[y].size()-m).get("credit")+",成績");
								out.print(term2[y].get(term2[y].size()-m).get("score"));
								out.println("&#10;");
								m++;
							}
							
						}
					}catch(Exception e){}
					
					try{						
						if(extra1[y].get("Gmark")!=null){
							out.println(extra1[y].get("Gmark"));							
						}					
					}catch(Exception e){}
					try{
						if(extra2[y].get("Gmark")!=null){
							out.println(extra2[y].get("Gmark"));
						}	
					}catch(Exception e){}		
					out.println ("</Data></Cell>");
				}
				
				
				/*out.println ("    <Cell ss:MergeAcross='5' ss:MergeDown='2' ss:StyleID='m2197017584804'><Data");
				out.println ("      ss:Type='String'>note2</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='5' ss:MergeDown='2' ss:StyleID='m2197017584824'><Data");
				out.println ("      ss:Type='String'>note3</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='5' ss:MergeDown='2' ss:StyleID='m2197017584844'><Data");
				out.println ("      ss:Type='String'>note4</Data></Cell>");*/
				
				
				out.println ("   </Row>");
				
				
				
				
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='9.75'/>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='47.25'/>");
				out.println ("  </Table>");
				out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
				out.println ("   <PageSetup>");
				out.println ("    <Layout x:Orientation='Landscape' x:CenterHorizontal='1'/>");
				out.println ("    <Header x:Margin='0.31496062992125984' x:Data='&amp;C&amp;&quot;標楷體,標準&quot;&amp;24中華科技大學"+stds.get(i).get("schoolName")+"學生歷年成績表'/>");
				out.println ("    <Footer x:Margin='0.31496062992125984'");
				//out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
				if(stds.get(i).get("MasterData")!=null){
					//out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;論文題目："+((Map)stds.get(i).get("MasterData")).get("theses_chiname")+"&#10;"+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"&#10;學位考試成績："+((Map)stds.get(i).get("MasterData")).get("theses_engname")+" 學位考試成績(50%)："+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"  學業平均成績："+((Map)stds.get(i).get("MasterData")).get("theses_engname")+" 學業平均成績(50%)："+((Map)stds.get(i).get("MasterData")).get("theses_engname")+" 總成績："+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"&amp;R&amp;&quot;標楷體,標準&quot;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
					if(((Map)stds.get(i).get("MasterData")).get("remark")!=null){
						if(!((Map)stds.get(i).get("MasterData")).get("remark").equals("")){
							out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;論文題目："+((Map)stds.get(i).get("MasterData")).get("theses_chiname")+"&#10;"+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"&#10;學位考試成績："+((Map)stds.get(i).get("MasterData")).get("theses_score")+" 學業平均成績："+((Map)stds.get(i).get("MasterData")).get("evgr1_score")+" 總成績："+((Map)stds.get(i).get("MasterData")).get("graduate_score")+"&amp;R&amp;&quot;標楷體,標準&quot;備註:"+((Map)stds.get(i).get("MasterData")).get("remark")+"&#10;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
						}else{
							out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;論文題目："+((Map)stds.get(i).get("MasterData")).get("theses_chiname")+"&#10;"+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"&#10;學位考試成績："+((Map)stds.get(i).get("MasterData")).get("theses_score")+" 學業平均成績："+((Map)stds.get(i).get("MasterData")).get("evgr1_score")+" 總成績："+((Map)stds.get(i).get("MasterData")).get("graduate_score")+"&amp;R&amp;&quot;標楷體,標準&quot;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
						}
					}else{
						out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;論文題目："+((Map)stds.get(i).get("MasterData")).get("theses_chiname")+"&#10;"+((Map)stds.get(i).get("MasterData")).get("theses_engname")+"&#10;學位考試成績："+((Map)stds.get(i).get("MasterData")).get("theses_score")+" 學業平均成績："+((Map)stds.get(i).get("MasterData")).get("evgr1_score")+" 總成績："+((Map)stds.get(i).get("MasterData")).get("graduate_score")+"&amp;R&amp;&quot;標楷體,標準&quot;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
					}
					
					
				}else{
					out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;符號說明 *: 不及格, #輔系, &amp;雙主修, 抵:抵免'/>");
				}
				
				
				out.println ("    <PageMargins x:Bottom='0.39370078740157483' x:Left='0.23622047244094491'");
				out.println ("     x:Right='0.23622047244094491' x:Top='0.74803149606299213'/>");
				out.println ("   </PageSetup>");
				out.println ("   <Print>");
				out.println ("    <ValidPrinterInfo/>");
				out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
				out.println ("    <Scale>59</Scale>");
				out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
				out.println ("    <VerticalResolution>-1</VerticalResolution>");
				out.println ("   </Print>");
				out.println ("   <Zoom>75</Zoom>");
				out.println ("   <Selected/>");
				out.println ("   <TopRowVisible>0</TopRowVisible>");
				out.println ("   <Panes>");
				out.println ("    <Pane>");
				out.println ("     <Number>3</Number>");
				out.println ("     <ActiveRow>0</ActiveRow>");
				out.println ("     <ActiveCol>0</ActiveCol>");
				out.println ("    </Pane>");
				out.println ("   </Panes>");
				out.println ("   <ProtectObjects>False</ProtectObjects>");
				out.println ("   <ProtectScenarios>False</ProtectScenarios>");
				out.println ("  </WorksheetOptions>");
				out.println (" </Worksheet>");
				
				over+=4;
			}
			
			
		
		
		
		}
		
		
		
		out.println ("</Workbook>");
		
		
		
		
		
		out.close();
		out.flush();
	}

}
