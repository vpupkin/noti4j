package eu.blky.log4j;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skype.Skype;

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.10.2011::12:03:01<br> 
 */
public class SkypeAppenderTest extends TestCase {
	
	Logger log = LoggerFactory.getLogger(SkypeAppenderTest.class);
	

	/**
	 * 
	 * The file log4j.properties has predefined configuration 
	 * ...
log4j.appender.SKYPE=eu.blky.log4j.SkypeAppender
log4j.appender.SKYPE.layout=org.apache.log4j.PatternLayout
log4j.appender.skype.layout.ConversionPattern=[%p] %c - %m
log4j.appender.SKYPE.receiver=wolli-home-gw
log4j.category.eu.blky.log4j.SkypeAppenderTest==DEBUG, SKYPE
	 * ...
	 * @author vipup
	 */
	public void testLog(){
		log.trace("Ttest@{}:{}",this.getName(),new Date());
		log.debug("Dtest@{}:{}",this.getName(),new Date());
		log.info("Itest@{}:{}",this.getName(),new Date());

	}


	@Override
	protected void setUp() throws Exception {
        Skype.setDebug(false);
        Skype.setDeamon(false);
		super.setUp(); 
	}

}


 