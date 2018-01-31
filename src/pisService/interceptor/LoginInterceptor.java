package pisService.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import service.impl.AccountManager;
	/**
	 * 攔截使用者動作，驗證cookie & session有效
	 * @author John
	 */
	public class LoginInterceptor implements Interceptor {
	    public void destroy() {}
	    public void init() {}
	    public String intercept(ActionInvocation invocation) throws Exception {  
	        
	    	HttpSession session=ServletActionContext.getRequest().getSession();
	    	HttpServletRequest request=ServletActionContext.getRequest();	    	
	    	//登入與登出功能不作攔截
	    	if(invocation.getAction().getClass().getName().indexOf("Log")>=0){	    		
	        	return invocation.invoke();
	        }	        
	    	//確認是否有選單及權限session，若有則驗證為線上使用者
	    	if(session!=null){
	    		if(session.getAttribute("userid")!=null){//使用者為本專案線上使用者
	    			return invocation.invoke();
	    		}
	    	}	    	
	    	//確認是否有尚未失效的cookie-name=userid，若有則為其他系統使用者跳轉
	    	//ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());	    	
    		//AccountManager am=(AccountManager) context.getBean("AccountManager");
	    	AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:../applicationContext.xml");
	    	AccountManager am =(AccountManager)context.getBean("AccountManager");
            context.registerShutdownHook();
	    	am.loginJumpUser(request);
	    	context.close();
    		return invocation.invoke();	    
	    }
	} 	