package eu.blky.log4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.OptionConverter;

import eu.blky.log4j.sec.DefaultPlexusCipher;
import eu.blky.log4j.sec.DefaultSecDispatcher;
import eu.blky.log4j.sec.PlexusCipherException;
import eu.blky.log4j.sec.SecDispatcherException;
import eu.blky.log4j.sec.SecUtil;
import eu.blky.log4j.sec.SettingsSecurity;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  28.10.2011::16:11:47<br> 
 */
public abstract class AbstractActivableAppender extends AppenderSkeleton {
	
	protected abstract String[] getBeanPropertyNames();
	protected SettingsSecurity sec ;

	@Override
	public final void activateOptions() {
		String uHome = System.getProperty("user.home");
		String uDir = System.getProperty("user.dir");
		System.out.println(uDir);
		File iUserCfg = new File(uHome, ".l");
		if (iUserCfg.exists()) {
			Properties lProps = new Properties();
			FileReader reader;
			try {
				reader = new FileReader(iUserCfg);
				lProps.load(reader);
				String[] keys = getBeanPropertyNames();
				for (String key : keys ) {
					String keyToSub = this.get(key);
					if (keyToSub==null)continue;
					keyToSub = keyToSub.trim();
					if (keyToSub.startsWith("~{") && keyToSub.endsWith("}"))
						keyToSub = keyToSub.substring(2, keyToSub.length() - 1);
					String value = OptionConverter.findAndSubst(keyToSub,
							lProps);
					// iterate thru bean-props
					if (value.startsWith("{") && value.endsWith("}")) {
						// try to restore the value from keyChain
						value = decryptPassword(value);
					}
					set(key, value);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecDispatcherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PlexusCipherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} 
	}

	protected final String decryptPassword(String value) throws SecDispatcherException,
			PlexusCipherException {
		String configurationFile = "master.xml";
//DefaultSecDispatcher()	    {	        _configurationFile = "~/.settings-security.xml";
//		if (configurationFile.startsWith("~")) {
//			configurationFile = System.getProperty("user.home") + configurationFile.substring(1);
//		}
//new File(".").getAbsolutePath()
		String file = System.getProperty( DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION, configurationFile);
		// try to retrieve the master-configuration from web, dav, file, userhome, userdir, env, e.t.c..
		sec = SecUtil.read(file, true);
		String master = null;
		if (sec != null) {
			master = sec.getMaster();
		}
		DefaultPlexusCipher cipher = new DefaultPlexusCipher();
		String masterPasswd = cipher.decryptDecorated(master, DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION);
		String pwdTmp = cipher.decrypt(value , masterPasswd);
		return pwdTmp;
	}
 
	protected final String get(String key) {
		String retval = null;
		String name2 = "get"+key.substring(0,1).toUpperCase()+key.substring(1);
		
		try {
			java.lang.reflect.Method mTmp = this.getClass().getMethod(name2 );
			retval = (String)mTmp.invoke(this );
		} catch (SecurityException e) { 
		} catch (NoSuchMethodException e) { 
		} catch (IllegalArgumentException e) { 
		} catch (IllegalAccessException e) { 
		} catch (InvocationTargetException e) { 
		}
		return retval;
	}

	protected final void set(String key, String value) {
		String name2 = "set"+key.substring(0,1).toUpperCase()+key.substring(1);
		Class<?> parameterTypes =  String.class ;
		try {
			java.lang.reflect.Method mTmp = this.getClass().getMethod(name2, parameterTypes);
			mTmp.invoke(this, value);
		} catch (SecurityException e) { 
		} catch (NoSuchMethodException e) { 
		} catch (IllegalArgumentException e) { 
		} catch (IllegalAccessException e) { 
		} catch (InvocationTargetException e) { 
		}
	
	}

}


 