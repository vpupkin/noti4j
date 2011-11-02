 
package eu.blky.log4j.sec;

import java.io.*;
import java.util.*;
import org.codehaus.plexus.logging.AbstractLogEnabled;
//import org.sonatype.plexus.components.cipher.*;
//import org.sonatype.plexus.components.sec.dispatcher.model.SettingsSecurity;

// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher:
//            SecDispatcherException, PasswordDecryptor, SecDispatcher, SecUtil

public class DefaultSecDispatcher extends AbstractLogEnabled
    implements SecDispatcher
{

    public DefaultSecDispatcher()
    {
        _configurationFile = "~/.settings-security.xml";
    }

    public String decrypt(String str)
        throws SecDispatcherException
    {
        if(!isEncryptedString(str))
            return str;
        String bare = null;
        try
        {
            bare = _cipher.unDecorate(str);
        }
        catch(PlexusCipherException e1)
        {
            throw new SecDispatcherException(e1);
        }
        String res;
        try
        {
            Map<String, String> attr = stripAttributes(bare);
            res = null;
            SettingsSecurity sec = getSec();
            if(attr == null || attr.get("type") == null)
            {
                String master = getMaster(sec);
                res = _cipher.decrypt(bare, master);
            } else
            {
                String type = (String)attr.get("type");
                if(_decryptors == null)
                    throw new SecDispatcherException("plexus container did not supply any required dispatchers - cannot lookup " + type);
                Map<?, ?> conf = SecUtil.getConfig(sec, type);
                PasswordDecryptor dispatcher = (PasswordDecryptor)_decryptors.get(type);
                if(dispatcher == null)
                {
                    throw new SecDispatcherException("no dispatcher for hint " + type);
                } else
                {
                    String pass = attr != null ? strip(bare) : bare;
                    return dispatcher.decrypt(pass, attr, conf);
                }
            }
        }
        catch(Exception e)
        {
            throw new SecDispatcherException(e);
        }
        return res;
    }

    private String strip(String str)
    {
        int pos = str.indexOf(']');
        if(pos == str.length())
            return null;
        if(pos != -1)
            return str.substring(pos + 1);
        else
            return str;
    }

    private Map<String, String> stripAttributes(String str)
    {
        int start = str.indexOf('[');
        int stop = str.indexOf(']');
        if(start != -1 && stop != -1 && stop > start)
        {
            if(stop == start + 1)
                return null;
            String attrs = str.substring(start + 1, stop).trim();
            if(attrs == null || attrs.length() < 1)
                return null;
            Map<String, String> res = null;
            StringTokenizer st = new StringTokenizer(attrs, ", ");
            do
            {
                if(!st.hasMoreTokens())
                    break;
                if(res == null)
                    res = new HashMap<String, String>(st.countTokens());
                String pair = st.nextToken();
                int pos = pair.indexOf('=');
                if(pos != -1)
                {
                    String key = pair.substring(0, pos).trim();
                    if(pos == pair.length())
                    {
                        res.put(key, null);
                    } else
                    {
                        String val = pair.substring(pos + 1);
                        res.put(key, val.trim());
                    }
                }
            } while(true);
            return res;
        } else
        {
            return null;
        }
    }

    private boolean isEncryptedString(String str)
    {
        if(str == null)
            return false;
        else
            return _cipher.isEncryptedString(str);
    }

    private SettingsSecurity getSec()
        throws SecDispatcherException
    {
        String location = System.getProperty(SYSTEM_PROPERTY_SEC_LOCATION, getConfigurationFile());
        String realLocation = location.charAt(0) != '~' ? location : System.getProperty("user.home") + location.substring(1);
        SettingsSecurity sec = SecUtil.read(realLocation, true);
        if(sec == null)
            throw new SecDispatcherException("cannot retrieve master password. Please check that " + realLocation + " exists and has data");
        else
            return sec;
    }

    private String getMaster(SettingsSecurity sec)
        throws SecDispatcherException
    {
        String master = sec.getMaster();
        if(master == null)
            throw new SecDispatcherException("master password is not set");
        try
        {
            return _cipher.decryptDecorated(master, SYSTEM_PROPERTY_SEC_LOCATION);
        }
        catch(PlexusCipherException e)
        {
            throw new SecDispatcherException(e);
        }
    }

    public String getConfigurationFile()
    {
        return _configurationFile;
    }

    public void setConfigurationFile(String file)
    {
        _configurationFile = file;
    }

    private static boolean propertyExists(String values[], String av[])
    {
        if(values != null)
        {
            for(int i = 0; i < values.length; i++)
            {
                String p = System.getProperty(values[i]);
                if(p != null)
                    return true;
            }

            if(av != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    for(int j = 0; j < av.length; j++)
                        if(("--" + values[i]).equals(av[j]))
                            return true;

                }

            }
        }
        return false;
    }

    private static final void usage()
    {
        System.out.println("usage: java -jar ...jar [-m|-p]\n-m: encrypt master password\n-p: encrypt password");
    }

    public static void main(String args[])
        throws Exception
    {
        if(args == null || args.length < 1)
        {
            usage();
            return;
        }
        if("-m".equals(args[0]) || propertyExists(SYSTEM_PROPERTY_MASTER_PASSWORD, args))
            show(true);
        else
        if("-p".equals(args[0]) || propertyExists(SYSTEM_PROPERTY_SERVER_PASSWORD, args))
            show(false);
        else
            usage();
    }

    private static void show(boolean showMaster)
        throws Exception
    {
        if(showMaster)
            System.out.print("\nsettings master password\n");
        else
            System.out.print("\nsettings server password\n");
        System.out.print("enter password: ");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String pass = r.readLine();
        System.out.println("\n");
        DefaultPlexusCipher dc = new DefaultPlexusCipher();
        DefaultSecDispatcher dd = new DefaultSecDispatcher();
        dd._cipher = dc;
        if(showMaster)
        {
            System.out.println(dc.encryptAndDecorate(pass, SYSTEM_PROPERTY_SEC_LOCATION));
        } else
        {
            SettingsSecurity sec = dd.getSec();
            System.out.println(dc.encryptAndDecorate(pass, dd.getMaster(sec)));
        }
    }

    public static final String SYSTEM_PROPERTY_SEC_LOCATION = "settings.security";
    public static final String TYPE_ATTR = "type";
    public static final char ATTR_START = 91;
    public static final char ATTR_STOP = 93;
    protected PlexusCipher _cipher;
    protected Map<?, ?> _decryptors;
    protected String _configurationFile;
}
