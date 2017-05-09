package com.miniProject.listener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component("taskContextBeanPostProcessor")
public class TaskContextBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent>{
	
	Logger logger = LoggerFactory.getLogger(TaskContextBeanPostProcessor.class);
	
	private ApplicationContext applicationContext;
	private long time = System.currentTimeMillis() + 5*1000*60L;
	private long period = 30*60*1000L;
	private Date startDateTime = new Date(time - time%period + 25*1000*60L);
//	private Date startDateTime = new Date();
	@Override
	public void onApplicationEvent(ContextRefreshedEvent context) {
		if(applicationContext==null){
			applicationContext=context.getApplicationContext();
			Object bean=applicationContext.getBean("autoMonitorTasker");
			if(bean!=null&& bean instanceof AutoMonitorTasker){
				TimerTask task=(AutoMonitorTasker)bean;
				Timer timer=new Timer(true);
				logger.info("startDateTime is {} " , startDateTime);
				timer.schedule(task,startDateTime,period);
			}
		}
	}

}

