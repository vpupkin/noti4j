 
package eu.blky.log4j.sec;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException; 
 
// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher:
//            SecDispatcherException

public class SecUtil
{

    //private static final Logger log = LoggerFactory.getLogger("eu.blky.log4j.sec.SecUtil");
 
    public static SettingsSecurity read(String location, boolean cycle)
        throws SecDispatcherException
    { 
    	InputStream in; 
		SettingsSecurity settingssecurity = null;
		do{
			try {
				in = toStream(location);
				SecurityConfigurationXpp3Reader sec = new SecurityConfigurationXpp3Reader();
				settingssecurity=	sec.read(in);
			} catch (MalformedURLException e) {
				throw new SecDispatcherException(e);
			} catch (IOException e) {
				throw new SecDispatcherException(e);
			} catch (XmlPullParserException e) {
				throw new SecDispatcherException(e);
			}
			location = settingssecurity.getRelocation() ;
		}while(cycle && location != null	);
		
    	return settingssecurity;
    }

    private static InputStream toStream(String resource)
        throws MalformedURLException, IOException
    {
        if(resource == null)
            return null;
        int ind = resource.indexOf("://");
        if(ind > 1)
        {
            String protocol = resource.substring(0, ind);
            resource = resource.substring(ind + PROTOCOL_DELIM_LEN);
            for(int i = 0; i < URL_PROTOCOLS.length; i++)
            {
                String p = URL_PROTOCOLS[i];
                if(protocol.regionMatches(true, 0, p, 0, p.length()))
                    return (new URL(p + "://" + resource)).openStream();
            } 
        }
        File secDir = null;
        try{// assums that the path to cgf-file fully specified
        	if (new File(  resource).exists()) {
        		//log.debug("MASTERPASSWORD loaded  from ["+resource+"]");//+(new File(resource)).toURI()
        		return new FileInputStream(new File(resource));
        	}
        	
        }catch(Throwable e){e.printStackTrace();}try{
        	if (new File(secDir = new File(System.getProperty("user.dir")),resource).exists()) {
        		//log.debug("MASTERPASSWORD loaded from {}/{}",System.getProperty("user.dir"),resource );
        		return new FileInputStream(new File(System.getProperty("user.dir"),resource));
        	}
        }catch(Throwable e){e.printStackTrace();}try{
        	if (new File(secDir = new File(System.getProperty("user.home")),resource).exists()) {
        		//log.debug("MASTERPASSWORD loaded from {}/{}",System.getProperty("user.home"),resource );
        		return new FileInputStream(new File(System.getProperty("user.home"),resource));
        	} 
     	
        	
        }catch(Throwable e){e.printStackTrace();}try{
        	if (SecUtil.class.getClassLoader().getResourceAsStream(resource)!=null){
        		//log.debug("MASTERPASSWORD loaded from {}/{}",secDir = new File(SecUtil.class.getClassLoader().getResource(resource).toURI()),resource );
        		return SecUtil.class.getClassLoader().getResourceAsStream(resource);
        	}
        	
        }catch(Throwable e){e.printStackTrace();}try{
        	if (System.getProperty("MASTERPASSWORD")!=null){
        		//log.debug("MASTERPASSWORD loaded from ${MASTERPASSWORD}" );
        		return new ByteArrayInputStream(("<settingsSecurity><master>{"+System.getProperty("MASTERPASSWORD")+"}</master></settingsSecurity>").getBytes());
        	}
        }catch(Throwable e){e.printStackTrace();}try{
        	if (System.getenv("MASTERPASSWORD")!=null){
        		//log.debug("MASTERPASSWORD loaded from env.MASTERPASSWORD" );
        		return new ByteArrayInputStream(("<settingsSecurity><master>{"+System.getenv("MASTERPASSWORD")+"}</master></settingsSecurity>").getBytes());
        	}
        	
        }catch(Throwable e){e.printStackTrace();} 
        System.out.println("secDir:"+secDir);
        return new FileInputStream(new File(resource));
    }

    public static Map<String, String> getConfig(SettingsSecurity sec, String name)
    {
        if(name == null)
            return null;
        List<?> cl = sec.getConfigurations();
        if(cl == null)
            return null;
        for(Iterator<?> i = cl.iterator(); i.hasNext();)
        {
            Config cf = (Config)i.next();
            if(name.equals(cf.getName()))
            {
                List<?> pl = cf.getProperties();
                if(pl == null || pl.isEmpty())
                    return null;
                Map<String, String> res = new HashMap<String, String>(pl.size());
                ConfigProperty p;
                for(Iterator<?> j = pl.iterator(); j.hasNext(); res.put(p.getName(), p.getValue()))
                    p = (ConfigProperty)j.next();

                return res;
            }
        }

        return null;
    }

    public static final String PROTOCOL_DELIM = "://";
    public static final int PROTOCOL_DELIM_LEN = "://".length();
    public static final String URL_PROTOCOLS[] = {
        //@deprecated         "http", 
        "https", 
        //@deprecated         "dav", 
        //@deprecated         "file", 
        "davs", 
        //@deprecated 		   "webdav", 
        "webdavs", 
        //@deprecated 			"dav+http", 
        "dav+https"
    };

}
