package ajax.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import service.tools.ImageHandler;
import action.BaseAction;
import model.EnrollRegistAttach;

/**
 * 儲存報名檔案至FTP伺服器
 * 依SYS_HOST欄位對應伺服器存取資訊
 * @author John
 */
public class putEnrollFtpFile extends BaseAction {
	
	//private static final long serialVersionUID = 572146812454l;
	//private static final int BUFFER_SIZE = 16 * 1024;

	private File myFile;
	private String fileName;
	private String newFileName;
	
	
	/*private Object files[];	

	public Object[] getFiles() {
		return files;
	}

	public void setFiles(Object[] files) {
		this.files = files;
	}*/
	/*
	public void setMyFileContentType(String contentType) {
		this.contentType = contentType;
	}
	*/
	public void setMyFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getNewFileName() {
		return newFileName;
	}

	@Override
	public String execute() throws IOException {	
		
		String ext;
		try{
			ext=bio.getExtention(fileName);
		}catch(Exception e){
			ext="";
		}		
		String fullpath=getContext().getRealPath("/tmp" )+"/";
		newFileName=new Date().getTime()+ext;
		
        File file = new File(fullpath+newFileName);
        File dir=new File(fullpath);
        if(!dir.exists())dir.mkdirs();
        bio.copyFile(myFile, file);//copy至server        
		
		//FTP
		//String useid=request.getParameter("useid");	
        EnrollRegistAttach a=new EnrollRegistAttach();
		try{
			
			String target="host_runtime";
			if(!df.testOnlineServer()){
				target="host_debug";
				//fullpath=fullpath.replace("/", "\\");
			}
			Map<String, String>ftpinfo=df.sqlGetMap("SELECT "+target+" as host, username, password, path FROM SYS_HOST WHERE useid='"+request.getParameter("useid")+"'");
			//System.out.println(fullpath);
			
			//System.out.println(newFileName);
			bio.putFTPFile(ftpinfo.get("host"), ftpinfo.get("username"), ftpinfo.get("password"), fullpath+"/", ftpinfo.get("path")+"/", newFileName);
			df.exSql("DELETE FROM Enroll_regist_attach WHERE Enroll_regist_oid="+request.getParameter("erOid")+" AND Enroll_attach_oid="+request.getParameter("eOid"));
			
			a.setEnrollAttachOid(Integer.parseInt(request.getParameter("eOid")));
			a.setEnrollRegistOid(Integer.parseInt(request.getParameter("erOid")));
			a.setPath(newFileName);
			df.update(a);
		}catch(Exception e){
			e.printStackTrace();				
		}
        this.setNewFileName(a.getPath());
		//System.out.println(a.getPath());
        //result.put("name", "fuck");
		//files=new Object[1];
		//files[0]=a.getPath();        
        return SUCCESS;
	}
	
	
}