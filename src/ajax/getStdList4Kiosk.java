package ajax;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import action.BaseAction;

/***
 * 外部取學號
 * http://john.cust.edu.tw/pis/getStdList4Kiosk?id=E122713583&bd=67-07-08
 * id=身分證
 * bd=民國出生日期yyy-mm-dd
 * @author John
 *
 */
public class getStdList4Kiosk extends BaseAction{
	
	private List list;	

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	/***
	 * http://john.cust.edu.tw/pis/getStdList4Kiosk?id=E122713583&bd=67-07-08
	 */
	public String execute(){
		String idno=request.getParameter("id");
		String bd=request.getParameter("bd");		
		bd=convertDate(bd);
		
		setList(df.sqlGet("SELECT student_no FROM Gstmd WHERE idno='"+idno+"' AND birthday='"+bd+"'"));		
		
		return SUCCESS;
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
}