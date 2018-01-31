package ajax.ftp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import action.BaseAction;
import service.tools.FtpClient;

/**
 * 下載檔案至FTP伺服器
 * 依參數對應伺服器資料夾位置
 * 依SYS_HOST欄位對應伺服器存取資訊
 * @author John
 */
public class getFtpFile extends BaseAction {
	private String filename;
	private String mimeType;
	private InputStream inStream;

	@Override
	public String execute() throws Exception {
		setFilename(request.getParameter("file"));			
		setMimeType(getContext().getMimeType(filename));
		getInStream();
		return SUCCESS;
	}

	public InputStream getInStream() throws IOException {	
		System.out.println("work???");
		String file=request.getParameter("file");
		String path=request.getParameter("path");		
		String host=((Map)getContext().getAttribute("FtpMain")).get("host_runtime").toString();
		if(getContext().getAttribute("isServer").equals("0")){
			host=((Map)getContext().getAttribute("FtpMain")).get("host_debug").toString();
		}
		FtpClient ftp=new FtpClient(host, "CIS", "cust!@#", "", "");		
		File tempFile = File.createTempFile(filename.substring(0, filename.indexOf(".")), filename.substring(filename.indexOf("."), filename.length()));
		
		ftp.connect();
		ftp.setBinaryTransfer(true);	
		
		
		if(path!=null){
			ftp.get(path+"/"+file, tempFile.getPath(), false);
		}else{
			ftp.get(file, tempFile.getPath(), false);
		}
		
		
		
		ftp.disconnect();		
		try{		
			setInStream(new FileInputStream(tempFile));		
			if (inStream == null) {
				inStream = new ByteArrayInputStream("Sorry,File not found !".getBytes());
			}			
		}catch(Exception e){
			e.printStackTrace();			
			
		}
		tempFile.delete();
		return inStream;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}

	public void setFilename(String filename) {
		try {
			this.filename = new String(filename.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getFilename() {
		try {
			return new String(filename.getBytes(),"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return this.filename;
		}
	}
}