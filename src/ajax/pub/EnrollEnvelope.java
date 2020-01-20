package ajax.pub;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

public class EnrollEnvelope extends BaseAction{
	
	public String execute() throws IOException{
		
		Date date=new Date();
		PrintWriter out=response.getWriter();
		
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
		
		String idno=request.getParameter("idno");
		
		out.println ("<?xml version='1.0'?>");
		out.println ("<?mso-application progid='Excel.Sheet'?>");
		out.println ("<Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:o='urn:schemas-microsoft-com:office:office'");
		out.println (" xmlns:x='urn:schemas-microsoft-com:office:excel'");
		out.println (" xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet'");
		out.println (" xmlns:html='http://www.w3.org/TR/REC-html40'>");
		out.println (" <DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <Author>John</Author>");
		out.println ("  <LastAuthor>John</LastAuthor>");
		out.println ("  <LastPrinted>2018-02-12T02:33:52Z</LastPrinted>");
		out.println ("  <Created>2018-02-12T00:58:19Z</Created>");
		out.println ("  <LastSaved>2018-02-12T02:18:22Z</LastSaved>");
		out.println ("  <Version>15.00</Version>");
		out.println (" </DocumentProperties>");
		out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <AllowPNG/>");
		out.println (" </OfficeDocumentSettings>");
		out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  <WindowHeight>9690</WindowHeight>");
		out.println ("  <WindowWidth>24000</WindowWidth>");
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
		out.println ("  <Style ss:ID='s125'>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s140'>");
		out.println ("   <Alignment ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s156'>");
		out.println ("   <Alignment ss:Horizontal='Right' ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s157'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s158'>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s160'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Double' ss:Weight='3'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Double' ss:Weight='3'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Double' ss:Weight='3'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Double' ss:Weight='3'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s180'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='22'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s184'>");
		out.println ("   <Alignment ss:Horizontal='Right' ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s185'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s200'>");
		out.println ("   <Alignment ss:Horizontal='Right' ss:Vertical='Top'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s202'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Top' ss:WrapText='1'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s206'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='14'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");		
		
		Map reg=df.sqlGetMap("SELECT * FROM Enroll_regist e, Enroll_stmd s WHERE s.idno=e.idno AND s.idno='"+idno+"'");
		Map enroll=df.sqlGetMap("SELECT e.* FROM Enroll e, Enroll_regist er WHERE e.Oid=er.Enroll_oid AND er.idno='"+idno+"'");
			
		List<Map>dept=df.sqlGet("SELECT cs.name as SchoolName, cc.name as CampusName, cd.name as DeptName, d.dept_name, "
		+ "erd.choice, erd.rank FROM Enroll_regist_dept erd, Enroll_dept d, CODE_CAMPUS cc, CODE_SCHOOL cs, "
		+ "CODE_DEPT cd WHERE cs.id=d.SchoolNo AND cd.id=d.DeptNo AND cc.id=d.CampusNo AND d.Oid=erd.Enroll_dept_oid "
		+ "AND erd.idno='"+idno+"' ORDER BY erd.choice");
		
		
		
		
		out.println (" <Worksheet ss:Name='工作表1'>");
		out.println ("  <Table ss:ExpandedColumnCount='7' ss:ExpandedRowCount='27' x:FullColumns='1'");
		out.println ("   x:FullRows='1' ss:StyleID='s125' ss:DefaultColumnWidth='54'");
		out.println ("   ss:DefaultRowHeight='16.5'>");
		out.println ("   <Column ss:StyleID='s125' ss:AutoFitWidth='0' ss:Width='58.5'/>");
		out.println ("   <Column ss:StyleID='s125' ss:AutoFitWidth='0' ss:Width='117'/>");
		out.println ("   <Column ss:StyleID='s125' ss:AutoFitWidth='0' ss:Width='50.25'/>");
		out.println ("   <Column ss:Index='5' ss:StyleID='s125' ss:AutoFitWidth='0' ss:Width='117.75'/>");
		out.println ("   <Column ss:StyleID='s125' ss:Hidden='1' ss:AutoFitWidth='0' ss:Width='9'/>");
		out.println ("   <Column ss:StyleID='s125' ss:AutoFitWidth='0' ss:Width='72.75'/>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='36'>");
		out.println ("    <Cell ss:StyleID='s156'><Data ss:Type='String'>寄件者:</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s157'><Data ss:Type='String'>"+reg.get("student_name")+"</Data></Cell>");
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s160'><Data ss:Type='String'>限時掛號</Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='36'>");
		out.println ("    <Cell ss:StyleID='s156'><Data ss:Type='String'>地址:</Data></Cell>");
		
		out.println ("    <Cell ss:MergeAcross='5' ss:StyleID='s206'><Data ss:Type='String'>"+reg.get("curr_post")+","+reg.get("curr_addr")+"</Data></Cell>");
		
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='36'>");
		out.println ("    <Cell ss:StyleID='s156'><Data ss:Type='String'>電話:</Data></Cell>");
		if(reg.get("telephone")!=null){
			out.println ("    <Cell ss:StyleID='s157'><Data ss:Type='String'>"+reg.get("telephone")+"</Data></Cell>");
		}else{
			out.println ("    <Cell ss:StyleID='s157'><Data ss:Type='String'></Data></Cell>");
		}
		
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s156'><Data ss:Type='String'>行動電話:</Data></Cell>");
		if(reg.get("CellPhone")!=null){
			out.println ("    <Cell ss:StyleID='s157'><Data ss:Type='String'>"+reg.get("CellPhone")+"</Data></Cell>");
		}else{
			out.println ("    <Cell ss:StyleID='s157'><Data ss:Type='String'></Data></Cell>");
		}
		
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("    <Cell ss:StyleID='s158'/>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='172.5'>");
		out.println ("    <Cell ss:MergeAcross='6' ss:StyleID='s180'><Data ss:Type='String'>11581&#10;台北市南港區研究院路三段245號&#10;中華科技大學"+enroll.get("enroll_name")+"招生委員會 收</Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='122.25'>");
		out.println ("    <Cell ss:StyleID='s200'><Data ss:Type='String'>報考:</Data></Cell>");
		
		StringBuilder sb=new StringBuilder("");
		for(int i=0; i<dept.size(); i++){
			sb.append(dept.get(i).get("dept_name")+", ");
		}
		sb.delete(sb.length()-2, sb.length());
		
		out.println ("    <Cell ss:MergeAcross='5' ss:StyleID='s202'><Data ss:Type='String'>"+sb+"</Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='19.5'>");
		out.println ("    <Cell ss:MergeAcross='6' ss:StyleID='s184'><Data ss:Type='String' x:Ticked='1'>&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;-</Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'>");
		out.println ("    <Cell ss:MergeAcross='6' ss:StyleID='s185'><ss:Data ss:Type='String'");
		out.println ("      xmlns='http://www.w3.org/TR/REC-html40'><Font html:Color='#000000'>下列各欄位請考生填寫，並確實檢查資料內容後劃記「</Font><Font");
		out.println ("       html:Face='Wingdings 2' x:CharSet='2' x:Family='Roman' html:Color='#000000'>R</Font><Font");
		out.println ("       html:Color='#000000'>」符號以利處理。</Font></ss:Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0'>");
		
		dept=df.sqlGet("SELECT attach_name FROM Enroll_attach WHERE Enroll_oid="+enroll.get("Oid"));
		sb=new StringBuilder("");
		for(int i=0; i<dept.size(); i++){
			sb.append("□ "+dept.get(i).get("attach_name")+"&#10;");
		}
		out.println ("    <Cell ss:MergeAcross='6' ss:MergeDown='19' ss:StyleID='s140'><Data");
		out.println ("      ss:Type='String'>"+sb+"</Data></Cell>");
		out.println ("   </Row>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Span='5'/>");
		out.println ("   <Row ss:Index='15' ss:AutoFitHeight='0' ss:Height='13.5'/>");
		out.println ("   <Row ss:AutoFitHeight='0' ss:Hidden='1' ss:Span='10'/>");
		out.println ("   <Row ss:Index='27' ss:AutoFitHeight='0' ss:Height='145.5'/>");
		out.println ("  </Table>");
		out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("   <PageSetup>");
		out.println ("    <Header");
		out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;招生報名專用信封&#10;-------------------------------------------------------------------------'/>");
		out.println ("    <PageMargins x:Left='1' x:Right='1'/>");
		out.println ("   </PageSetup>");
		out.println ("   <Unsynced/>");
		out.println ("   <Print>");
		out.println ("    <ValidPrinterInfo/>");
		out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
		out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
		out.println ("    <VerticalResolution>-1</VerticalResolution>");
		out.println ("   </Print>");
		out.println ("   <Zoom>86</Zoom>");
		out.println ("   <Selected/>");
		out.println ("   <ProtectObjects>False</ProtectObjects>");
		out.println ("   <ProtectScenarios>False</ProtectScenarios>");
		out.println ("  </WorksheetOptions>");
		out.println (" </Worksheet>");
		out.println ("</Workbook>");
		out.println ("");
		
		return null;
	}

}
