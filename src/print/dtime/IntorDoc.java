package print.dtime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class IntorDoc extends SylParser{
	
	
	public String execute() throws IOException{
		String Dtime_oid=request.getParameter("Dtime_oid");
		String Savedtime_oid=request.getParameter("Savedtime_oid");//歷史學年
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
		if(dtimes.size()>0){			
			/*if(request.getParameter("type").equals("ms")) {
				response.setContentType("application/vnd.ms-excel; charset=UTF-8");
				response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
			}else {
				response.setContentType("application/vnd.oasis.opendocument.spreadsheet; charset=UTF-8");
				response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".ods");
			}
			
			*/
			response.setContentType("application/vnd.ms-excel; charset=UTF-8");
			response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
			
			
			StringBuilder sb;
			if(savedtime){
				sb=new StringBuilder("SELECT d.school_year, d.school_term, d.Oid, d.school_term, d.introduction, c.ClassName, cs.chi_name, cs.eng_name, e.cname " +
						"FROM Savedtime d LEFT OUTER JOIN empl e ON d.techid=e.idno, Class c, Csno cs WHERE d.depart_class=c.ClassNo AND " +
						"cs.cscode=d.cscode AND d.Oid IN(");			
			}else{
				sb=new StringBuilder("SELECT '"+year+"'as school_year, '"+term+"'as school_term, d.Oid, d.Sterm, d.credit, d.introduction, c.ClassName, cs.chi_name, cs.eng_name, e.cname " +
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
			out.println ("  <LastPrinted>2014-10-08T08:29:35Z</LastPrinted>");
			out.println ("  <Created>2014-10-08T07:30:16Z</Created>");
			out.println ("  <LastSaved>2014-10-08T08:29:39Z</LastSaved>");
			out.println ("  <Version>15.00</Version>");
			out.println (" </DocumentProperties>");
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
			out.println ("  <Style ss:ID='s77'>");
			out.println ("   <Alignment ss:Horizontal='Justify' ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s83'>");
			out.println ("   <Alignment ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s109'>");
			out.println ("   <Alignment ss:Vertical='Center'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s112'>");
			out.println ("   <Alignment ss:Vertical='Center'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s120'>");
			out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s121'>");
			out.println ("   <Alignment ss:Horizontal='Justify' ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s124'>");
			out.println ("   <Alignment ss:Horizontal='Justify' ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
			out.println ("     ss:Color='#000000'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s126'>");
			out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
			out.println ("     ss:Color='#000000'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s127'>");
			out.println ("   <Alignment ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s130'>");
			out.println ("   <Alignment ss:Vertical='Center' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'");
			out.println ("     ss:Color='#000000'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s132'>");
			out.println ("   <Alignment ss:Vertical='Center'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s133'>");
			out.println ("   <Alignment ss:Vertical='Center'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s135'>");
			out.println ("   <Alignment ss:Vertical='Center'/>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s198'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s203'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s236'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s237'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s238'>");
			out.println ("   <Alignment ss:Vertical='Top' ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
			out.println ("    ss:Color='#000000'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s264'>");
			out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'");
			out.println ("    ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='16'");
			out.println ("    ss:Color='#000000' ss:Bold='1'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println ("  <Style ss:ID='s268'>");
			out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'");
			out.println ("    ss:ReadingOrder='LeftToRight' ss:WrapText='1'/>");
			out.println ("   <Borders>");
			out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
			out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'");
			out.println ("     ss:Color='#000000'/>");
			out.println ("   </Borders>");
			out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='16'");
			out.println ("    ss:Color='#000000' ss:Bold='1'/>");
			out.println ("   <NumberFormat ss:Format='@'/>");
			out.println ("  </Style>");
			out.println (" </Styles>");
			Map intor;
			String chi_name;
			for(int i=0; i<dtimes.size(); i++){
				chi_name=dtimes.get(i).get("chi_name").toString();
				if(chi_name.length()>=3){
					chi_name=chi_name.substring(0, 3);
				}
				
				intor=new HashMap();
				try{
					intor=parseIntr(bl.replaceXMLSymbol(dtimes.get(i).get("Introduction").toString()));
				}catch(Exception e){
					intor.put("chi", "解析內容有誤");
					intor.put("eng", "解析內容有誤");
				}
				if(intor==null){
					intor=new HashMap();
					intor.put("chi", "解析內容有誤");
					intor.put("eng", "解析內容有誤");
				}
				
				out.println (" <Worksheet ss:Name='"+dtimes.get(i).get("Oid")+chi_name+"'>");
				out.println ("  <Names>");
				out.println ("   <NamedRange ss:Name='Print_Area' ss:RefersTo='=工作表1!R1C1:R8C1'/>");
				out.println ("  </Names>");
				out.println ("  <Table ss:ExpandedColumnCount='1' ss:ExpandedRowCount='8' x:FullColumns='1'");
				out.println ("   x:FullRows='1' ss:StyleID='s203' ss:DefaultColumnWidth='54'");
				out.println ("   ss:DefaultRowHeight='19.5'>");
				out.println ("   <Column ss:StyleID='s203' ss:AutoFitWidth='0' ss:Width='594.75'/>");
			
			
			
				
				
				
				out.println ("   <Row>");
				out.println ("    <Cell ss:StyleID='s236'><Data ss:Type='String'>系科名稱(Department)："+dtimes.get(i).get("ClassName")+"教師(Instructor)："+dtimes.get(i).get("cname")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row>");
				out.println ("    <Cell ss:StyleID='s237'><Data ss:Type='String'>科目名稱(Course Title)："+dtimes.get(i).get("chi_name")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row>");
				out.println ("    <Cell ss:StyleID='s237'><Data ss:Type='String'>英文科目名稱(Course in English)："+dtimes.get(i).get("eng_name")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:Height='20.25'>");
				out.println ("    <Cell ss:StyleID='s238'><Data ss:Type='String'>學年(School Year)："+dtimes.get(i).get("school_year")+"學期(Semester)："+dtimes.get(i).get("school_term")+" 學分數(Credit Hours)："+dtimes.get(i).get("credit")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'>");
				out.println ("    <Cell ss:StyleID='s264'><Data ss:Type='String'>中文課程簡介</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='307.5'>");
				out.println ("    <Cell ss:StyleID='s198'><Data ss:Type='String'>"+intor.get("chi")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'>");
				out.println ("    <Cell ss:StyleID='s268'><Data ss:Type='String'>英文課程簡介</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='315'>");
				out.println ("    <Cell ss:StyleID='s198'><Data ss:Type='String'>"+intor.get("eng")+"</Data><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("   </Row>");
				
						
			
				out.println ("  </Table>");
				out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
				out.println ("   <PageSetup>");
				out.println ("    <Header x:Margin='0.31496062992125984'");
				out.println ("     x:Data='&amp;C&amp;&quot;標楷體,粗體&quot;&amp;18任教科目中英文課程簡介&amp;16&#10;&amp;14General and Specialized Course Introduction&amp;&quot;-,標準&quot;&amp;12&#10;'/>");
				out.println ("    <Footer x:Margin='0.31496062992125984' x:Data='&amp;R&amp;&quot;標楷體,標準&quot;遵守智慧財產權觀念，不得非法影印  '/>");
				out.println ("    <PageMargins x:Bottom='0.74803149606299213' x:Left='0.23622047244094491'");
				out.println ("     x:Right='0.15748031496062992' x:Top='0.94488188976377963'/>");
				out.println ("   </PageSetup>");
				out.println ("   <Print>");
				out.println ("    <FitWidth>0</FitWidth>");
				out.println ("    <ValidPrinterInfo/>");
				out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
				out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
				out.println ("    <VerticalResolution>-1</VerticalResolution>");
				out.println ("   </Print>");
				out.println ("   <Selected/>");
				out.println ("   <Panes>");
				out.println ("    <Pane>");
				out.println ("     <Number>3</Number>");
				out.println ("     <ActiveRow>5</ActiveRow>");
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