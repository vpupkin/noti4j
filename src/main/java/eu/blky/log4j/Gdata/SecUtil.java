// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecUtil.java

package eu.blky.log4j.Gdata;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
 
// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher:
//            SecDispatcherException

public class SecUtil
{

    public SecUtil()
    {
    }

    public static SettingsSecurity read(String location, boolean cycle)
        throws SecDispatcherException
    {
    	if(1==2)
    		throw new SecDispatcherException("no implemented!");
    	InputStream in;

		SettingsSecurity settingssecurity = null;
		try {
			in = toStream(location);
			settingssecurity=	(new SecurityConfigurationXpp3Reader()).read(in);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//        InputStream in;
//        if(location == null)
//            throw new SecDispatcherException("location to read from is null");
//        in = null;
//        SettingsSecurity settingssecurity1;
//        try
//        {
//        SettingsSecurity sec;
//        SettingsSecurity settingssecurity;
//        in = toStream(location);
//        sec = (new SecurityConfigurationXpp3Reader()).read(in);
//        in.close();
//        if(!cycle || sec.getRelocation() == null)
//            break MISSING_BLOCK_LABEL_64;
//        settingssecurity = read(sec.getRelocation(), true);
//        return settingssecurity;
//       
//
//            settingssecurity1 = sec;
//        }
//        catch(Exception e)
//        {
//            throw new SecDispatcherException(e);
//        }
//        if(in == null)
//        	return settingssecurity1;
//        //local;
//        
//            try
//            {
//                in.close();
//            }
//            catch(Exception e) { }
//        //JVM INSTR ret 6;
//            
    	
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
        if (new File(System.getProperty("user.dir"),resource).exists()) return new FileInputStream(new File(System.getProperty("user.dir"),resource));
        if (new File(System.getProperty("user.home"),resource).exists()) return new FileInputStream(new File(System.getProperty("user.home"),resource));
        if (new File(System.getenv("security.home"),resource).exists()) return new FileInputStream(new File(System.getProperty("security.home"),resource)); 
        if (SecUtil.class.getClassLoader().getResourceAsStream(resource)!=null)return SecUtil.class.getClassLoader().getResourceAsStream(resource); 
        return new FileInputStream(new File(resource));
    }

    public static Map getConfig(SettingsSecurity sec, String name)
    {
        if(name == null)
            return null;
        List cl = sec.getConfigurations();
        if(cl == null)
            return null;
        for(Iterator i = cl.iterator(); i.hasNext();)
        {
            Config cf = (Config)i.next();
            if(name.equals(cf.getName()))
            {
                List pl = cf.getProperties();
                if(pl == null || pl.isEmpty())
                    return null;
                Map res = new HashMap(pl.size());
                ConfigProperty p;
                for(Iterator j = pl.iterator(); j.hasNext(); res.put(p.getName(), p.getValue()))
                    p = (ConfigProperty)j.next();

                return res;
            }
        }

        return null;
    }

    public static final String PROTOCOL_DELIM = "://";
    public static final int PROTOCOL_DELIM_LEN = "://".length();
    public static final String URL_PROTOCOLS[] = {
        "http", "https", "dav", "file", "davs", "webdav", "webdavs", "dav+http", "dav+https"
    };

}
