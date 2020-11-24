package service.listener;

import java.util.Date;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.impl.DataFinder;

public class PISTask extends TimerTask{
	
	private ServletContextEvent event = null;
    
    public PISTask(ServletContextEvent event){
        this.event = event;
    }

	@Override
	public void run() {
		Date now=new Date();
    	AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
    	DataFinder dm =(DataFinder)context.getBean("DataFinder");
        context.registerShutdownHook();		
		ServletContext servletContext = event.getServletContext();
		
		//dm.sqlGetInt("SELECT COUNT(*) FROM SYS_CALENDAR WHERE name='aqansw_end' AND cdate>='"+sf.format(now)+"'")>0){
		System.out.println("建立可招生科系: CounselorDept");
		//servletContext.setAttribute("CounselorDept", d);
		
		System.out.println("建立可招生學制: CounselorDept");
		//servletContext.setAttribute("CounselorDept", "");
		
		
		
		context.close();
	}

}
