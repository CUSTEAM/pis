package print.dtime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class SylDoc extends SylParser{
	
	
	public String execute() throws IOException{		
		String Dtime_oid=request.getParameter("Dtime_oid");
		String Savedtime_oid=request.getParameter("Savedtime_oid");//歷史學年
		System.out.println("SELECT Oid FROM Dtime WHERE Oid="+Dtime_oid);
		if(Dtime_oid!=null){//現在學年
			print(response, df.sqlGet("SELECT Oid FROM Dtime WHERE Oid="+Dtime_oid),getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(), false);
		}else{
			print(response, df.sqlGet("SELECT Oid FROM Savedtime WHERE Oid="+Savedtime_oid),getContext().getAttribute("school_year").toString(), getContext().getAttribute("school_term").toString(), true);
		}				
		return null;
	}
	
	public void print(HttpServletResponse response, List<Map>dtimes, String year, String term, boolean savedtime) throws IOException{
		
		Date date=new Date();
		PrintWriter out=response.getWriter();
		
		//System.out.println(request.getParameter("type"));
		if(dtimes.size()>0){			
			/*if(request.getParameter("type").equals("ms")) {
				response.setContentType("application/vnd.ms-excel; charset=UTF-8");
				response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
			}else {
				response.setContentType("application/vnd.oasis.opendocument.spreadsheet; charset=UTF-8");
				response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".ods");
			}*/
			
			response.setContentType("application/vnd.ms-excel; charset=UTF-8");
			response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
			
			StringBuilder sb;
			if(savedtime){
				sb=new StringBuilder("SELECT d.Oid, d.school_year, d.school_term, d.credit, d.Syllabi, d.Syllabi_sub, c.ClassName, cs.chi_name, cs.eng_name, e.cname " +
						"FROM Savedtime d LEFT OUTER JOIN empl e ON d.techid=e.idno, Class c, Csno cs WHERE d.depart_class=c.ClassNo AND " +
						"cs.cscode=d.cscode AND d.Oid IN(");			
			}else{
				sb=new StringBuilder("SELECT d.Oid, "+year+" as school_year, d.Sterm as school_term, d.credit, d.Syllabi, d.Syllabi_sub, c.ClassName, cs.chi_name, cs.eng_name, e.cname " +
						"FROM Dtime d LEFT OUTER JOIN empl e ON d.techid=e.idno, Class c, Csno cs WHERE d.depart_class=c.ClassNo AND " +
						"cs.cscode=d.cscode AND d.Oid IN(");
			}
			for(int i=0; i<dtimes.size(); i++){
				sb.append(dtimes.get(i).get("Oid")+",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")");	
			dtimes=df.sqlGet(sb.toString());
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
			out.println ("  <LastPrinted>2014-10-03T06:24:09Z</LastPrinted>");
			out.println ("  <Created>2014-10-03T04:30:19Z</Created>");
			out.println ("  <LastSaved>2014-10-03T06:26:50Z</LastSaved>");
			out.println ("  <Version>15.00</Version>");
			out.println (" </DocumentProperties>");
			out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
			out.println ("  <AllowPNG/>");
			out.println (" </OfficeDocumentSettings>");
			out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
			out.println ("  <WindowHeight>11880</WindowHeight>");
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
			out.println ("  <Style ss:ID='m447189269520'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189269540'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189269560'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189269580'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266400'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266420'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266440'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266460'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266480'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='m447189266500'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s62'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s79'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s80'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:ReadingOrder='LeftToRight'");
			out.println ("    ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s81'>");
			out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'");
			out.println ("    ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s82'>");
			out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'");
			out.println ("    ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println (" </Styles>");	
			Map sly;
			List<Map>list;	
			String chi_name;	
			for(int i=0; i<dtimes.size(); i++){
				sly=parseSyl(bl.replaceXMLSymbol(((String)dtimes.get(i).get("Syllabi"))));
				if(sly==null){
					sly=new HashMap();
				}
				list=parseSyls(bl.replaceXMLSymbol((String)dtimes.get(i).get("Syllabi_sub")));	
				
				chi_name=dtimes.get(i).get("chi_name").toString();
				if(chi_name.length()>=3){
					chi_name=chi_name.substring(0, 3);
				}
				out.println (" <Worksheet ss:Name='"+dtimes.get(i).get("Oid")+chi_name+"'>");
				/*out.println ("  <Names>");
				out.println ("   <NamedRange ss:Name='Print_Titles' ss:RefersTo='=工作表1!R11:R12'/>");
				out.println ("  </Names>");*/
				out.println ("  <Table ss:ExpandedColumnCount='5' ss:ExpandedRowCount='999' x:FullColumns='1'");
				out.println ("   x:FullRows='1' ss:StyleID='s62' ss:DefaultColumnWidth='54'");
				out.println ("   ss:DefaultRowHeight='16.5'>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='93'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='351'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='48.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='42.75'/>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='54.75'/>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266400'><Data ss:Type='String'>系科名稱(Department)："+dtimes.get(i).get("ClassName")+" 教師(Instructor)："+dtimes.get(i).get("cname")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266420'><Data ss:Type='String'>輔導時間(Office Hours)：</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266440'><Data ss:Type='String'>科目名稱(Course Title)："+dtimes.get(i).get("chi_name")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266460'><Data ss:Type='String'>英文科目名稱(Course in English)："+dtimes.get(i).get("eng_name")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266480'><Data ss:Type='String'>學年(School Year)："+dtimes.get(i).get("school_year")+" 學期(Semester)："+dtimes.get(i).get("school_term")+" 學分數(Credit Hours)："+dtimes.get(i).get("credit")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189266500'><Data ss:Type='String'>先修科目或先備能力(Course Prerequisites)："+bl.replaceXMLSymbol((String)sly.get("pre"))+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='18.75'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189269520'><Data ss:Type='String'>課程目標(Course Objectives)：</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='112.5'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189269540'><Data ss:Type='String'>"+bl.replaceXMLSymbol((String)sly.get("obj"))+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189269560'><Data ss:Type='String'>教學大綱(Syllabus)：</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='112.5'>");
				out.println ("    <Cell ss:MergeAcross='4' ss:StyleID='m447189269580'><Data ss:Type='String'>"+bl.replaceXMLSymbol((String)sly.get("syl"))+"</Data></Cell>");
				out.println ("   </Row>");			
				out.println ("   <Row ss:AutoFitHeight='0'>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>章節主題</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>內容綱要</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>時數</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>週次</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>備註</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0'>");
				out.println ("    <Cell ss:StyleID='s80'><Data ss:Type='String'>(Topics)</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s80'><Data ss:Type='String'>(Content)</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s80'><Data ss:Type='String'>(Hours)</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s80'><Data ss:Type='String'>(Week)</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("    <Cell ss:StyleID='s80'><Data ss:Type='String'>(Remark)</Data><NamedCell");
				out.println ("      ss:Name='Print_Titles'/></Cell>");
				out.println ("   </Row>");			
				for(int j=0; j<list.size(); j++){
					out.println ("   <Row ss:AutoFitHeight='0'>");
					out.println ("    <Cell ss:StyleID='s81'><Data ss:Type='String'>"+list.get(j).get("topic")+"</Data></Cell>");
					out.println ("    <Cell ss:StyleID='s82'><Data ss:Type='String'>"+list.get(j).get("content")+"</Data></Cell>");
					out.println ("    <Cell ss:StyleID='s81'><Data ss:Type='String'>"+list.get(j).get("hours")+"</Data></Cell>");
					out.println ("    <Cell ss:StyleID='s81'><Data ss:Type='String'>"+list.get(j).get("week")+"</Data></Cell>");
					out.println ("    <Cell ss:StyleID='s81'/>");
					out.println ("   </Row>");
				}
				out.println ("  </Table>");
				out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
				out.println ("   <PageSetup>");
				out.println ("    <Header x:Margin='0.31496062992125984'");
				out.println ("     x:Data='&amp;C&amp;&quot;標楷體,粗體&quot;&amp;18一般及專業理論[含實驗(習)]課程綱要表&#10;&amp;&quot;Times New Roman,粗體&quot;&amp;16General and Specialized Course Syllabus'/>");
				out.println ("    <Footer x:Margin='0.31496062992125984'");
				//out.println ("     x:Data='&amp;C&amp;&quot;標楷體,標準&quot;遵守智慧財產權觀念，不得非法影印&amp;R&amp;&quot;標楷體,標準&quot;第&amp;P頁共&amp;N頁 '/>");
				out.println ("     x:Data='&amp;C&amp;&quot;標楷體,標準&quot;遵守智慧財產權觀念，不得非法影印&amp;R&amp;&quot;標楷體,標準&quot;&amp;D &amp;T '/>");
				out.println ("    <PageMargins x:Bottom='0.74803149606299213' x:Left='0.23622047244094491'");
				out.println ("     x:Right='0.23622047244094491' x:Top='0.94488188976377963'/>");
				out.println ("   </PageSetup>");
				out.println ("   <Unsynced/>");
				out.println ("   <Print>");
				out.println ("    <ValidPrinterInfo/>");
				out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
				out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
				out.println ("    <VerticalResolution>-1</VerticalResolution>");
				out.println ("   </Print>");
				out.println ("   <PageBreakZoom>60</PageBreakZoom>");
				out.println ("   <Selected/>");
				out.println ("   <TopRowVisible>6</TopRowVisible>");
				out.println ("   <Panes>");
				out.println ("    <Pane>");
				out.println ("     <Number>3</Number>");
				out.println ("     <ActiveRow>7</ActiveRow>");
				out.println ("     <RangeSelection>R8C1:R8C5</RangeSelection>");
				out.println ("    </Pane>");
				out.println ("   </Panes>");
				out.println ("   <ProtectObjects>False</ProtectObjects>");
				out.println ("   <ProtectScenarios>False</ProtectScenarios>");
				out.println ("  </WorksheetOptions>");
				out.println (" </Worksheet>");		
			}		
			out.println ("</Workbook>");
			out.close();
			
		}else{
			response.setContentType("text; charset=UTF-8");
			response.setHeader("Content-disposition","attachment;filename=findnot.txt");
			out.println ("範圍內無課程查詢結果");
			out.close();
		}
		
		
	}
}