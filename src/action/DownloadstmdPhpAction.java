package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 報部
 * @author John
 */
public class DownloadstmdPhpAction extends BaseAction{
	
	/*
	  	type        型態:1新生, 2畢業生
		year        年度
		division    部別
			<option value='0'>選擇部別</option>
                <option value='1'>日間部</option>
                <option value='2'>進修部</option>
                <option value='3'>進修學院</option>
                <option value='4'>新竹分部</option>
		department  系
		grade       年級
		class       班級
		
		<td align=center>序號</td>
          <td align=center>班級</td>
          <td align=center>學號</td>
          <td align=center>姓名</td>
          <td align=center>身分證號</td>
          <td align=center>出生日</td>
          <td align=center>程度</td>
	*/
	private String StmdCode(String SchoolNo, String type) {
		String bk="1";
		if(type.equals("1"))bk="2";
		
		switch (SchoolNo){
		case "1G":return"1"+bk;
		case "8G":return"1"+bk;
		case "FG":return"1"+bk;
		case "64":return"2"+bk;
		case "54":return"2"+bk;
		case "A4":return"2"+bk;
		case "B2":return"2"+bk;
		case "B3":return"2"+bk;
		case "42":return"2"+bk;
		case "52":return"2"+bk;
		case "72":return"2"+bk;
		case "82":return"2"+bk;
		case "32":return"3"+bk;
		case "22":return"3"+bk;
		case "92":return"3"+bk;
	    default:return"12";
		}		
	}
	
	
	public String execute() throws IOException{		
		String type=request.getParameter("type");
		String year=request.getParameter("year");		
		
		
		List<Map>list;
		if(type!=null && year!=null) {
			
			//入學年
			if(type.equals("1")) {
				list=df.sqlGet("SELECT c.SchoolNo, cc.name as ccName, cd.name as cdName, cs.name as csName, c.ClassName, "
						+ "s.student_no,s.student_name, s.idno, s.birthday FROM stmd s, Class c,CODE_COLLEGE cc, "
						+ "CODE_DEPT cd, CODE_SCHOOL cs WHERE cc.id=c.InstNo AND cd.id=c.DeptNo AND cs.id=c.SchoolNo AND "
						+ "s.depart_class=c.ClassNo AND s.student_no LIKE '"+year+"%'ORDER BY c.SchoolNo, c.DeptNo, c.Grade;");
				list.addAll(df.sqlGet("SELECT c.SchoolNo, cc.name as ccName, cd.name as cdName, cs.name as csName, c.ClassName, "
						+ "s.student_no,s.student_name, s.idno, s.birthday FROM Gstmd s, Class c,CODE_COLLEGE cc, "
						+ "CODE_DEPT cd, CODE_SCHOOL cs WHERE cc.id=c.InstNo AND cd.id=c.DeptNo AND cs.id=c.SchoolNo AND "
						+ "s.depart_class=c.ClassNo AND s.student_no LIKE '"+year+"%'ORDER BY c.SchoolNo, c.DeptNo, c.Grade;"));
			
			
			}else {
				//畢業年
				list=df.sqlGet("SELECT c.SchoolNo, cc.name as ccName, cd.name as cdName, cs.name as csName, c.ClassName, s.student_no,"
						+ "s.student_name, s.idno, s.birthday FROM Gstmd s, Class c,CODE_COLLEGE cc, CODE_DEPT cd, "
						+ "CODE_SCHOOL cs WHERE cc.id=c.InstNo AND cd.id=c.DeptNo AND cs.id=c.SchoolNo AND "
						+ "s.depart_class=c.ClassNo AND s.occur_year='"+year+"' AND occur_status='6' ORDER BY c.SchoolNo, c.DeptNo, c.Grade;");
				
			}
			
			for(int i=0; i<list.size(); i++) {				
				list.get(i).put("code", StmdCode(list.get(i).get("SchoolNo").toString(), type));
			}
			
			
			
		}else {
			return null;
		}
		
		
		
		Date date=new Date();
		PrintWriter out=response.getWriter();		
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
			
		
		
		
		
		
		
		
		
		
		
		
		
		out.println ("<?xml version='1.0'?>");
		out.println ("<?mso-application progid='Excel.Sheet'?>");
		out.println ("<Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:o='urn:schemas-microsoft-com:office:office'");
		out.println (" xmlns:x='urn:schemas-microsoft-com:office:excel'");
		out.println (" xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:html='http://www.w3.org/TR/REC-html40'>");
		out.println (" <DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <Author>John Hsiao</Author>");
		out.println ("  <LastAuthor>John Hsiao</LastAuthor>");
		out.println ("  <Created>2019-04-10T08:31:32Z</Created>");
		out.println ("  <Company>Microsoft</Company>");
		out.println ("  <Version>14.00</Version>");
		out.println (" </DocumentProperties>");
		out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <AllowPNG/>");
		out.println (" </OfficeDocumentSettings>");
		out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  <WindowHeight>3120</WindowHeight>");
		out.println ("  <WindowWidth>6435</WindowWidth>");
		out.println ("  <WindowTopX>240</WindowTopX>");
		out.println ("  <WindowTopY>45</WindowTopY>");
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
		out.println ("  <Style ss:ID='s62'>");
		out.println ("   <Font ss:FontName='微軟正黑體' x:CharSet='136' x:Family='Swiss' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='@'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");
		out.println (" <Worksheet ss:Name='工作表1'>");
		out.println ("  <Names>");
		out.println ("   <NamedRange ss:Name='_FilterDatabase' ss:RefersTo='=工作表1!R1C1:R2C10'");
		out.println ("    ss:Hidden='1'/>");
		out.println ("  </Names>");
		out.println ("  <Table ss:ExpandedColumnCount='10' ss:ExpandedRowCount='"+(list.size()+99)+"' x:FullColumns='1'");
		out.println ("   x:FullRows='1' ss:StyleID='s62' ss:DefaultColumnWidth='54'");
		out.println ("   ss:DefaultRowHeight='15.75'>");
		out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='46.5' ss:Span='3'/>");
		out.println ("   <Column ss:Index='5' ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='82.5'/>");
		out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='72.75'/>");
		out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='47.25'/>");
		out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='75.75'/>");
		out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='75'/>");
		
			
			
			out.println ("   <Row ss:AutoFitHeight='0'>");
			out.println ("    <Cell><Data ss:Type='String'>序號</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>院</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>部</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>系</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>班級</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>學號</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>姓名</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>身分證號</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>生日</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>程度</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("   </Row>");
			
			
		
		
		
		for(int i=0; i<list.size(); i++) {
			
			out.println ("   <Row ss:AutoFitHeight='0'>");
			out.println ("    <Cell><Data ss:Type='Number'>"+i+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("ccName")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("csName")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("cdName")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("className")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("student_no")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("student_name")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("idno")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("birthday")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/></Cell>");
			out.println ("    <Cell><Data ss:Type='Number'>"+list.get(i).get("code")+"</Data><NamedCell ss:Name='_FilterDatabase'/></Cell>");
			out.println ("   </Row>");
			
			
			
		}
		
		
		out.println ("  </Table>");
		out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("   <PageSetup>");
		out.println ("    <Header x:Margin='0.3'/>");
		out.println ("    <Footer x:Margin='0.3'/>");
		out.println ("    <PageMargins x:Bottom='0.75' x:Left='0.25' x:Right='0.25' x:Top='0.75'/>");
		out.println ("   </PageSetup>");
		out.println ("   <Unsynced/>");
		out.println ("   <Print>");
		out.println ("    <ValidPrinterInfo/>");
		out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
		out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
		out.println ("    <VerticalResolution>-1</VerticalResolution>");
		out.println ("   </Print>");
		out.println ("   <Selected/>");
		out.println ("   <Panes>");
		out.println ("    <Pane>");
		out.println ("     <Number>3</Number>");
		out.println ("     <ActiveRow>1</ActiveRow>");
		out.println ("    </Pane>");
		out.println ("   </Panes>");
		out.println ("   <ProtectObjects>False</ProtectObjects>");
		out.println ("   <ProtectScenarios>False</ProtectScenarios>");
		out.println ("  </WorksheetOptions>");
		out.println ("  <AutoFilter x:Range='R1C1:R2C10'");
		out.println ("   xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  </AutoFilter>");
		out.println (" </Worksheet>");
		out.println ("</Workbook>");
		out.println ("");
		
		
		
		
		
		out.close();
		
		
		
		
		
		
		return null;
	}
	
	
}