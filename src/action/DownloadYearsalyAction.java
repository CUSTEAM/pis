package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class DownloadYearsalyAction extends BaseAction{
	
	public String execute() throws IOException {
		
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
		out.println ("  <LastPrinted>2019-04-25T06:31:49Z</LastPrinted>");
		out.println ("  <Created>2019-04-25T06:21:05Z</Created>");
		out.println ("  <LastSaved>2019-04-25T06:34:30Z</LastSaved>");
		out.println ("  <Company>Microsoft</Company>");
		out.println ("  <Version>14.00</Version>");
		out.println (" </DocumentProperties>");
		out.println (" <OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'>");
		out.println ("  <AllowPNG/>");
		out.println (" </OfficeDocumentSettings>");
		out.println (" <ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  <WindowHeight>3120</WindowHeight>");
		out.println ("  <WindowWidth>10755</WindowWidth>");
		out.println ("  <WindowTopX>480</WindowTopX>");
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
		out.println ("  <Style ss:ID='s77'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體' x:CharSet='136' x:Family='Swiss' ss:Size='11'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s78'>");
		out.println ("   <Alignment ss:Horizontal='Left' ss:Vertical='Center'/>");
		out.println ("   <Font ss:FontName='微軟正黑體' x:CharSet='136' x:Family='Swiss' ss:Size='11'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println ("  <Style ss:ID='s79'>");
		out.println ("   <Alignment ss:Horizontal='Right' ss:Vertical='Center'/>");
		out.println ("   <Borders>");
		out.println ("    <Border ss:Position='Bottom' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("    <Border ss:Position='Top' ss:LineStyle='Continuous' ss:Weight='1'/>");
		out.println ("   </Borders>");
		out.println ("   <Font ss:FontName='微軟正黑體' x:CharSet='136' x:Family='Swiss' ss:Size='11'");
		out.println ("    ss:Color='#000000'/>");
		out.println ("  </Style>");
		out.println (" </Styles>");
		
		
		
		
		String ydate=df.sqlGetStr("SELECT ydate FROM yearsaly ORDER BY ydate DESC");
		List<Map>list=df.sqlGet("SELECT e.cname, e.acct_no, s.* FROM yearsaly s, yearempl e WHERE e.idno=s.idno AND s.ydate='"+ydate+"'ORDER BY sno");
		
		
		out.println (" <Worksheet ss:Name='工作表1'>");
		out.println ("  <Names>");
		out.println ("   <NamedRange ss:Name='_FilterDatabase' ss:RefersTo='=工作表1!R1C1:R2C10'");
		out.println ("    ss:Hidden='1'/>");
		out.println ("   <NamedRange ss:Name='Print_Area' ss:RefersTo='=工作表1!C1:C10'/>");
		out.println ("  </Names>");
		out.println ("  <Table ss:ExpandedColumnCount='10' ss:ExpandedRowCount='"+(list.size()+999)+"' x:FullColumns='1'");
		out.println ("   x:FullRows='1' ss:StyleID='s78' ss:DefaultColumnWidth='54'");
		out.println ("   ss:DefaultRowHeight='15'>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='30.75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='40.5'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='74.25'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='84.75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='51.75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='30.75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='40.5'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:Width='84.75'/>");
		out.println ("   <Column ss:StyleID='s77' ss:AutoFitWidth='0' ss:Width='66'/>");
		
		
		out.println ("   <Row>");
		
		
		out.println ("    <Cell><Data ss:Type='String'>序號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>姓名</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>身分證號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>轉帳帳號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>轉帳金額</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		
		
		
		out.println ("    <Cell><Data ss:Type='String'>序號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>姓名</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>身分證號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>轉帳帳號</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("    <Cell><Data ss:Type='String'>轉帳金額</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
		out.println ("      ss:Name='Print_Area'/></Cell>");
		out.println ("   </Row>");
		
		
		int tmp;
		Map m;
		for(int i=0; i<list.size(); i+=2) {			
			out.println ("   <Row>");
			out.println ("    <Cell><Data ss:Type='Number'>"+list.get(i).get("sno")+"</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
			out.println ("      ss:Name='Print_Area'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("cname")+"</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
			out.println ("      ss:Name='Print_Area'/></Cell>");
			out.println ("    <Cell><Data ss:Type='String'>"+list.get(i).get("idno")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
			out.println ("    <Cell><Data ss:Type='Number'>"+list.get(i).get("acct_no")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
			out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='Number'>"+list.get(i).get("realpay")+"</Data><NamedCell");
			out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");	
			try {
				out.println ("    <Cell><Data ss:Type='Number'>"+list.get(i+1).get("sno")+"</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='String'>"+list.get(i+1).get("cname")+"</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='String'>"+list.get(i+1).get("idno")+"</Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='Number'>"+list.get(i).get("acct_no")+"</Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='Number'>"+list.get(i).get("realpay")+"</Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
				
			}catch(Exception e) {
				/*out.println ("    <Cell><Data ss:Type='Number'>0</Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='String'></Data><NamedCell ss:Name='_FilterDatabase'/><NamedCell");
				out.println ("      ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='String'></Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell><Data ss:Type='Number'>0</Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
				out.println ("    <Cell ss:StyleID='s79'><Data ss:Type='Number'>0</Data><NamedCell");
				out.println ("      ss:Name='_FilterDatabase'/><NamedCell ss:Name='Print_Area'/></Cell>");
								*/
			}			
			out.println ("   </Row>");
		}		
		
		out.println ("  </Table>");
		out.println ("  <WorksheetOptions xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("   <PageSetup>");
		out.println ("    <Header x:Margin='0.31496062992125984'");
		out.println ("     x:Data='&amp;C&amp;&quot;標楷體,標準&quot;&amp;14中華科技大學公保年金轉帳清冊(年金日期：2019-03-15)&#10;&amp;R&amp;&quot;微軟正黑體,標準&quot;&amp;P/&amp;N'/>");
		out.println ("    <Footer x:Margin='0.31496062992125984'/>");
		out.println ("    <PageMargins x:Bottom='0.74803149606299213' x:Left='0.19685039370078741'");
		out.println ("     x:Right='0.19685039370078741' x:Top='0.74803149606299213'/>");
		out.println ("   </PageSetup>");
		out.println ("   <Print>");
		out.println ("    <ValidPrinterInfo/>");
		out.println ("    <PaperSizeIndex>9</PaperSizeIndex>");
		out.println ("    <HorizontalResolution>-1</HorizontalResolution>");
		out.println ("    <VerticalResolution>-1</VerticalResolution>");
		out.println ("   </Print>");
		out.println ("   <Zoom>166</Zoom>");
		out.println ("   <Selected/>");
		out.println ("   <ProtectObjects>False</ProtectObjects>");
		out.println ("   <ProtectScenarios>False</ProtectScenarios>");
		out.println ("  </WorksheetOptions>");
		out.println ("  <AutoFilter x:Range='R1C1:R2C10'");
		out.println ("   xmlns='urn:schemas-microsoft-com:office:excel'>");
		out.println ("  </AutoFilter>");
		out.println (" </Worksheet>");
		out.println ("</Workbook>");		
		out.close();
		return null;
	}

}
