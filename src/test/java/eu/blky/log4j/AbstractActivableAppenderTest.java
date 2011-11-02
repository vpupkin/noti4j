package eu.blky.log4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.log4j.spi.LoggingEvent;

import eu.blky.log4j.sec.Config;
import eu.blky.log4j.sec.ConfigProperty;
import eu.blky.log4j.sec.PlexusCipherException;
import eu.blky.log4j.sec.SecDispatcherException;
import eu.blky.log4j.sec.SecurityConfigurationXpp3Writer;
import eu.blky.log4j.sec.SettingsSecurity;

import junit.framework.TestCase;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  02.11.2011::13:17:09<br> 
 */
public class AbstractActivableAppenderTest extends TestCase {
	String actual = null;
	String expected ="004916098989882";
	SettingsSecurity settingsSecurity;
	public void testPWD(){
		AbstractActivableAppender aaa = new AbstractActivableAppender(){

			

			@Override
			protected String[] getBeanPropertyNames() {
				// TODO Auto-generated method stub
				if (1==1)throw new RuntimeException("not yet implemented since 02.11.2011");
				else {
				return null;
				}
			}

			@Override
			protected void append(LoggingEvent event) {
				// TODO Auto-generated method stub
				if (1==1)throw new RuntimeException("not yet implemented since 02.11.2011");
				else {
				}
			}

			@Override
			public void close() {
				try {
					System.out.println(actual = decryptPassword("{fWoAIt45gMoHGQHfg8LvGqZr5ZNSQAQi7N3sghLb0vk=}"));
					settingsSecurity = sec ;
				} catch (SecDispatcherException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PlexusCipherException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public boolean requiresLayout() {
				// TODO Auto-generated method stub
				if (1==1)throw new RuntimeException("not yet implemented since 02.11.2011");
				else {
				return false;
				}
			}};
			
			aaa.close();
			assertEquals(expected, actual);
			
			SecurityConfigurationXpp3Writer swr = new SecurityConfigurationXpp3Writer();
			Writer wrTmp = new PrintWriter(System.out, true);
			try {
				 
				swr.write(wrTmp , settingsSecurity);
				Config config = new Config() ;
				ConfigProperty configProperty = new ConfigProperty();
				configProperty .setName("bla1");
				configProperty .setValue( "blabla1");
				config.addProperty(configProperty );
				settingsSecurity.addConfiguration(config );
				
				swr.write(wrTmp , settingsSecurity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}


 