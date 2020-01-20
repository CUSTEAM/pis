package ajax.pub;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import action.BaseAction;

public class EnrollDoc extends BaseAction{
	
	public String execute() throws IOException{
		
		Date date=new Date();
		PrintWriter out=response.getWriter();
		
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		response.setHeader("Content-disposition","attachment;filename="+date.getTime()+".xls");
		
		String idno=request.getParameter("idno");
		
		
		Map reg=df.sqlGetMap("SELECT * FROM Enroll_regist e, Enroll_stmd s WHERE s.idno=e.idno AND s.idno='"+idno+"'");
		Map enroll;
			
		List<Map>dept=df.sqlGet("SELECT cs.name as SchoolName, cc.name as CampusName, cd.name as DeptName, d.dept_name, "
		+ "erd.choice, erd.rank FROM Enroll_regist_dept erd, Enroll_dept d, CODE_CAMPUS cc, CODE_SCHOOL cs, "
		+ "CODE_DEPT cd WHERE cs.id=d.SchoolNo AND cd.id=d.DeptNo AND cc.id=d.CampusNo AND d.Oid=erd.Enroll_dept_oid "
		+ "AND erd.idno='"+idno+"' ORDER BY erd.choice");
			
			/*reg.put("atta", df.sqlGet("SELECT ea.Oid as eOid, ea.attach_name, ea.online, era.* FROM Enroll_attach ea "
			+ "LEFT OUTER JOIN Enroll_regist_attach era ON ea.Oid=era.Enroll_attach_oid AND "
			+ "era.Enroll_regist_oid="+reg.get("Oid")+" WHERE ea.Enroll_oid="+reg.get("Enroll_oid")));*/
		//}		
		
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
		out.println ("  <LastPrinted>2018-02-08T06:57:57Z</LastPrinted>");
		out.println ("  <Created>2018-02-08T06:20:39Z</Created>");
		out.println ("  <LastSaved>2018-02-08T06:55:59Z</LastSaved>");
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
		out.println ("  <Style ss:ID='m1651902816784'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810544'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810564'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810584'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810604'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810624'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='m1651902810644'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s62'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s63'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s64'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s65'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s66'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s67'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s68'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s69'>");
		out.println ("   <Alignment ss:Horizontal='Center' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s70'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s71'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='新細明體' x:CharSet='136' x:Family='Roman' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("   <NumberFormat ss:Format='Short Date'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s78'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s79'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center' ss:WrapText='1'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Left' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Right' ss:LineStyle='Continuous' ss:Weight='2'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s97'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Bottom' ss:WrapText='1'/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s98'>");
		out.println ("   <Alignment ss:Horizontal='Right' ss:Vertical='Bottom' ss:WrapText='1'/>");
		out.println ("   <Font ss:FontName='標楷體' x:CharSet='136' x:Family='Script' ss:Size='12'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");
		
		
		
		if(reg!=null){
			
			enroll=df.sqlGetMap("SELECT * FROM Enroll WHERE Oid="+reg.get("Enroll_oid"));
			
			reg.put("reg", df.sqlGetMap("SELECT * FROM Enroll_regist WHERE idno='"+idno+"'"));		
			
			
			out.println (" <Worksheet ss:Name='工作表1'>");
			out.println ("  <Table ss:ExpandedColumnCount='4' ss:ExpandedRowCount='12' x:FullColumns='1'");
			out.println ("   x:FullRows='1' ss:StyleID='s62' ss:DefaultColumnWidth='54'");
			out.println ("   ss:DefaultRowHeight='16.5'>");
			out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='130.5' ss:Span='3'/>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s63'><Data ss:Type='String'>准考證號碼</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s64'/>");
			out.println ("    <Cell ss:StyleID='s65'><Data ss:Type='String'>報考身分別</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s66'><Data ss:Type='String'></Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>姓名</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s68'><Data ss:Type='String'>"+reg.get("student_name")+"</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>性別</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'></Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>出生年月日</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>"+reg.get("birthday")+"</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>身分證字號</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>"+reg.get("idno")+"</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>報考系所</Data></Cell>");
			
			StringBuilder sb=new StringBuilder("");
			for(int i=0; i<dept.size(); i++){
				sb.append(dept.get(i).get("dept_name")+", ");
			}
			sb.delete(sb.length()-2, sb.length());
			out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810544'><Data ss:Type='String'>"+sb+"</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>連絡地址</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s78'><Data ss:Type='String'>"+reg.get("curr_post")+reg.get("curr_addr")+"</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>連絡電話</Data></Cell>");
			out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>"+reg.get("CellPhone")+", "+reg.get("telephone")+"</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>畢業學校</Data></Cell>");
			out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810564'><Data ss:Type='String'>"+reg.get("schl_name")+", "+reg.get("grad_dept")+"</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>同等學歷</Data></Cell>");
			out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810584'><Data ss:Type='String'></Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
			out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>服務單位</Data></Cell>");
			out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810604'><Data ss:Type='String'></Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='147'>");
			out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m1651902810624'><Data ss:Type='String'>身分證影本正面黏貼處</Data></Cell>");
			out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m1651902810644'><Data ss:Type='String'>身分證影本反面黏貼處</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='167.25'>");
			out.println ("    <Cell ss:MergeAcross='3' ss:StyleID='m1651902816784'><Data ss:Type='String'>本表所填資料及所附文件均經本人詳實核對無誤，若有不實，本人願接受招生委員會處置，絕無異議。中華科技大學研究所碩士班考試招生委員會(以下簡稱本會)主辦本招生，需依個人資料保護法規定取得並保管考生個人資料，在辦理招生事務之目的下，進行處理及利用。本會將善盡保管人之義務與責任，妥善保管考生個人資料，僅提供招生相關工作目的使用。凡報名本招生者，即表示同意授權本會，得將自考生報名參加本招生所取得之個人及其相關成績資料等，運用於本招生事務使用，並同意提供其報名資料及成績予考生本人、本會招生考試各項試務、中華科技大學辦理新生報到或入學資料建置使用。</Data></Cell>");
			out.println ("   </Row>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'/>");
			out.println ("   <Row ss:AutoFitHeight='0' ss:Height='63' ss:StyleID='s97'>");
			out.println ("    <Cell ss:Index='3' ss:StyleID='s98'><Data ss:Type='String'>考生簽名</Data></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>___________________</Data></Cell>");
			out.println ("   </Row>");
			out.println ("  </Table>");
			out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
			out.println ("   <PageSetup>");
			out.println ("    <Header x:Margin='0.3' x:Data='&amp;C&amp;&quot;標楷體,標準&quot;&amp;16中華科技大學"+enroll.get("enroll_name")+"報名表'/>");
			out.println ("    <Footer x:Margin='0.3'");
			out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;&amp;10附註一、本表須由考生親自以正楷填寫，若有塗改，請於塗改處加蓋私章。&#10;附註二、考生若無簽名者，視同願意接受本招生簡章(含表件)內所有規定，考生不得有異議。'/>");
			out.println ("    <PageMargins x:Bottom='0.75' x:Left='0.7' x:Right='0.7' x:Top='0.75'/>");
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
			out.println ("     <ActiveRow>5</ActiveRow>");
			out.println ("     <ActiveCol>1</ActiveCol>");
			out.println ("     <RangeSelection>R6C2:R6C4</RangeSelection>");
			out.println ("    </Pane>");
			out.println ("   </Panes>");
			out.println ("   <ProtectObjects>False</ProtectObjects>");
			out.println ("   <ProtectScenarios>False</ProtectScenarios>");
			out.println ("  </WorksheetOptions>");
			out.println (" </Worksheet>");
			
		}else{
			
			String enrollOid=request.getParameter("EnrollOid");
			List<Map>stds=df.sqlGet("SELECT idno FROM Enroll_regist WHERE Enroll_oid="+enrollOid);
			enroll=df.sqlGetMap("SELECT * FROM Enroll WHERE Oid="+enrollOid);
			
			
			for(int x=0; x<stds.size(); x++){
				idno=stds.get(x).get("idno").toString();
				reg=df.sqlGetMap("SELECT * FROM Enroll_regist e, Enroll_stmd s WHERE s.idno=e.idno AND s.idno='"+idno+"'");
				
				//reg.put("reg", df.sqlGetMap("SELECT * FROM Enroll_regist WHERE idno='"+idno+"'"));		
				
				dept=df.sqlGet("SELECT cs.name as SchoolName, cc.name as CampusName, cd.name as DeptName, d.dept_name, "
				+ "erd.choice, erd.rank FROM Enroll_regist_dept erd, Enroll_dept d, CODE_CAMPUS cc, CODE_SCHOOL cs, "
				+ "CODE_DEPT cd WHERE cs.id=d.SchoolNo AND cd.id=d.DeptNo AND cc.id=d.CampusNo AND d.Oid=erd.Enroll_dept_oid "
				+ "AND erd.idno='"+idno+"' ORDER BY erd.choice");
				
				out.println (" <Worksheet ss:Name='"+reg.get("student_name")+"'>");
				out.println ("  <Table ss:ExpandedColumnCount='4' ss:ExpandedRowCount='12' x:FullColumns='1'");
				out.println ("   x:FullRows='1' ss:StyleID='s62' ss:DefaultColumnWidth='54'");
				out.println ("   ss:DefaultRowHeight='16.5'>");
				out.println ("   <Column ss:StyleID='s62' ss:AutoFitWidth='0' ss:Width='130.5' ss:Span='3'/>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s63'><Data ss:Type='String'>准考證號碼</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s64'/>");
				out.println ("    <Cell ss:StyleID='s65'><Data ss:Type='String'>報考身分別</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s66'><Data ss:Type='String'></Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>姓名</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s68'><Data ss:Type='String'>"+reg.get("student_name")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>性別</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'></Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>出生年月日</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s71'><Data ss:Type='String'>"+reg.get("birthday")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>身分證字號</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s70'><Data ss:Type='String'>"+reg.get("idno")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>報考系所</Data></Cell>");
				
				StringBuilder sb=new StringBuilder("");
				for(int i=0; i<dept.size(); i++){
					sb.append(dept.get(i).get("dept_name")+", ");
				}
				sb.delete(sb.length()-2, sb.length());
				out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810544'><Data ss:Type='String'>"+sb+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>連絡地址</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s78'><Data ss:Type='String'>"+reg.get("curr_post")+reg.get("curr_addr")+"</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s69'><Data ss:Type='String'>連絡電話</Data></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='String'>"+reg.get("CellPhone")+", "+reg.get("telephone")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>畢業學校</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810564'><Data ss:Type='String'>"+reg.get("schl_name")+", "+reg.get("grad_dept")+"</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>同等學歷</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810584'><Data ss:Type='String'></Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='45'>");
				out.println ("    <Cell ss:StyleID='s67'><Data ss:Type='String'>服務單位</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='2' ss:StyleID='m1651902810604'><Data ss:Type='String'></Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='147'>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m1651902810624'><Data ss:Type='String'>身分證影本正面黏貼處</Data></Cell>");
				out.println ("    <Cell ss:MergeAcross='1' ss:StyleID='m1651902810644'><Data ss:Type='String'>身分證影本反面黏貼處</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='167.25'>");
				out.println ("    <Cell ss:MergeAcross='3' ss:StyleID='m1651902816784'><Data ss:Type='String'>本表所填資料及所附文件均經本人詳實核對無誤，若有不實，本人願接受招生委員會處置，絕無異議。中華科技大學研究所碩士班考試招生委員會(以下簡稱本會)主辦本招生，需依個人資料保護法規定取得並保管考生個人資料，在辦理招生事務之目的下，進行處理及利用。本會將善盡保管人之義務與責任，妥善保管考生個人資料，僅提供招生相關工作目的使用。凡報名本招生者，即表示同意授權本會，得將自考生報名參加本招生所取得之個人及其相關成績資料等，運用於本招生事務使用，並同意提供其報名資料及成績予考生本人、本會招生考試各項試務、中華科技大學辦理新生報到或入學資料建置使用。</Data></Cell>");
				out.println ("   </Row>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='30'/>");
				out.println ("   <Row ss:AutoFitHeight='0' ss:Height='63' ss:StyleID='s97'>");
				out.println ("    <Cell ss:Index='3' ss:StyleID='s98'><Data ss:Type='String'>考生簽名</Data></Cell>");
				out.println ("    <Cell><Data ss:Type='String'>___________________</Data></Cell>");
				out.println ("   </Row>");
				out.println ("  </Table>");
				out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
				out.println ("   <PageSetup>");
				out.println ("    <Header x:Margin='0.3' x:Data='&amp;C&amp;&quot;標楷體,標準&quot;&amp;16中華科技大學"+enroll.get("enroll_name")+"報名表'/>");
				out.println ("    <Footer x:Margin='0.3'");
				out.println ("     x:Data='&amp;L&amp;&quot;標楷體,標準&quot;&amp;10附註一、本表須由考生親自以正楷填寫，若有塗改，請於塗改處加蓋私章。&#10;附註二、考生若無簽名者，視同願意接受本招生簡章(含表件)內所有規定，考生不得有異議。'/>");
				out.println ("    <PageMargins x:Bottom='0.75' x:Left='0.7' x:Right='0.7' x:Top='0.75'/>");
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
				out.println ("     <ActiveRow>5</ActiveRow>");
				out.println ("     <ActiveCol>1</ActiveCol>");
				out.println ("     <RangeSelection>R6C2:R6C4</RangeSelection>");
				out.println ("    </Pane>");
				out.println ("   </Panes>");
				out.println ("   <ProtectObjects>False</ProtectObjects>");
				out.println ("   <ProtectScenarios>False</ProtectScenarios>");
				out.println ("  </WorksheetOptions>");
				out.println (" </Worksheet>");
				
				
				
			}
			
			
			
			
			
		}
		
		
		
		
		out.println ("</Workbook>");
		
		out.close();
		out.flush();
		
		return null;
	}

}
