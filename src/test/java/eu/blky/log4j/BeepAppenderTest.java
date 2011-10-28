package eu.blky.log4j;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class BeepAppenderTest extends TestCase {
	
	Logger log = LoggerFactory.getLogger(BeepAppenderTest.class);

	/**
	 * 
	 * The file log4j.properties has predefined configuration 
	 * ...
	 * log4j.appender.BEEP=eu.blky.log4j.BeepAppender
	 * log4j.category.eu.blky.log4j.BeepAppenderTest==DEBUG, BEEP
	 * ...
	 * @author vipup
	 */
	public void testBeep(){
		log.trace("Ttest@{}:{}",this.getName(),new Date());
		log.debug("Dtest@{}:{}",this.getName(),new Date());
		log.info("Itest@{}:{}",this.getName(),new Date());
	}

}


 