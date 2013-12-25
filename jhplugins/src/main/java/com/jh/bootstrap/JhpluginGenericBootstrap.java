package com.jh.bootstrap;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jh.util.ProcessLockingUtil;

public class JhpluginGenericBootstrap {

	private static ApplicationContext ctx;
	private static String appCtxFileName = "file:./config/jh-main-context.xml";
	private static final String logPropertiesFile = "./config/log4j.properties";
	private static Logger logger = Logger.getLogger(JhpluginGenericBootstrap.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(System.getProperty("jh.config.log4j") == null ? logPropertiesFile : System.getProperty("jh.config.log4j"));
		new ProcessLockingUtil("./locks/"+args[0]).lockProcess();
//		System.setProperty("spring.profiles.active", "website_sync");
		bootstrap();
		executeBean(args[0], args[1]);
		((AbstractApplicationContext)ctx).stop();
		((AbstractApplicationContext)ctx).destroy();		
	}
	
	private static void bootstrap() {
		if (System.getProperty("jh.config.springcontext") != null) {
			appCtxFileName = "file:"+System.getProperty("jh.config.springcontext");
			logger.info("Loading context from file '"+appCtxFileName+"'");
		}
		else{
			logger.info("No context file env variable set hence loading context from default file '"+appCtxFileName+"'");
		}
		ctx = new ClassPathXmlApplicationContext(appCtxFileName);
	}
	
	private static void executeBean(String beanName, String methodName){
		if (ctx != null && ctx.containsBean(beanName)) {
			Object bean = ctx.getBean(beanName);
			try {
				Method method = bean.getClass().getMethod(methodName, new Class[]{});
				method.invoke(bean, new Object[]{});
			} catch (Exception e) {
				logger.error("Error while executing bean '"+beanName+"' and method '"+methodName+"'.",e);
				throw new RuntimeException(e);
			}
		}
	}
}
