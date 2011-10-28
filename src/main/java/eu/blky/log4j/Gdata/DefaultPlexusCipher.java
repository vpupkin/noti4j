// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultPlexusCipher.java

package eu.blky.log4j.Gdata;

import java.io.PrintStream;
import java.security.Provider;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.logging.AbstractLogEnabled;

// Referenced classes of package org.sonatype.plexus.components.cipher:
//            PBECipher, PlexusCipherException, PlexusCipher

public class DefaultPlexusCipher extends AbstractLogEnabled
    implements PlexusCipher
{

    public DefaultPlexusCipher()
        throws PlexusCipherException
    {
    }

    public String encrypt(String str, String passPhrase)
        throws PlexusCipherException
    {
        if(str == null || str.length() < 1)
            return str;
        else
            return _cipher.encrypt64(str, passPhrase);
    }

    public String encryptAndDecorate(String str, String passPhrase)
        throws PlexusCipherException
    {
        return decorate(encrypt(str, passPhrase));
    }

    public String decrypt(String str, String passPhrase)
        throws PlexusCipherException
    {
        if(str == null || str.length() < 1)
            return str;
        else
            return _cipher.decrypt64(str, passPhrase);
    }

    public String decryptDecorated(String str, String passPhrase)
        throws PlexusCipherException
    {
        if(str == null || str.length() < 1)
            return str;
        if(isEncryptedString(str))
            return decrypt(unDecorate(str), passPhrase);
        else
            return decrypt(str, passPhrase);
    }

    public boolean isEncryptedString(String str)
    {
        if(str == null || str.length() < 1)
        {
            return false;
        } else
        {
            Matcher matcher = ENCRYPTED_STRING_PATTERN.matcher(str);
            return matcher.matches() || matcher.find();
        }
    }

    public String unDecorate(String str)
        throws PlexusCipherException
    {
        Matcher matcher = ENCRYPTED_STRING_PATTERN.matcher(str);
        if(matcher.matches() || matcher.find())
            return matcher.group(1);
        else
            throw new PlexusCipherException("default.plexus.cipher.badEncryptedPassword");
    }

    public String decorate(String str)
    {
        return '{' + (str != null ? str : "") + '}';
    }

    public static String[] getServiceTypes()
    {
        Set result = new HashSet();
        Provider providers[] = Security.getProviders();
        for(int i = 0; i < providers.length; i++)
        {
            Set keys = providers[i].keySet();
            String key;
            int ix;
            for(Iterator it = keys.iterator(); it.hasNext(); result.add(key.substring(0, ix)))
            {
                key = (String)it.next();
                key = key.split(" ")[0];
                if(key.startsWith("Alg.Alias."))
                    key = key.substring(10);
                ix = key.indexOf('.');
            }

        }

        return (String[])result.toArray(new String[result.size()]);
    }

    public static String[] getCryptoImpls(String serviceType)
    {
        Set result = new HashSet();
        Provider providers[] = Security.getProviders();
label0:
        for(int i = 0; i < providers.length; i++)
        {
            Set keys = providers[i].keySet();
            Iterator it = keys.iterator();
            do
            {
                if(!it.hasNext())
                    continue label0;
                String key = (String)it.next();
                key = key.split(" ")[0];
                if(key.startsWith(serviceType + "."))
                    result.add(key.substring(serviceType.length() + 1));
                else
                if(key.startsWith("Alg.Alias." + serviceType + "."))
                    result.add(key.substring(serviceType.length() + 11));
            } while(true);
        }

        return (String[])result.toArray(new String[result.size()]);
    }

    public static void main(String args[])
    {
        String serviceTypes[] = getServiceTypes();
        if(serviceTypes != null)
        {
            for(int i = 0; i < serviceTypes.length; i++)
            {
                String serviceType = serviceTypes[i];
                String serviceProviders[] = getCryptoImpls(serviceType);
                if(serviceProviders != null)
                {
                    System.out.println(serviceType + ": provider list");
                    for(int j = 0; j < serviceProviders.length; j++)
                    {
                        String provider = serviceProviders[j];
                        System.out.println("        " + provider);
                    }

                } else
                {
                    System.out.println(serviceType + ": does not have any providers in this environment");
                }
            }

        }
    }

    private static final Pattern ENCRYPTED_STRING_PATTERN = Pattern.compile(".*?[^\\\\]?\\{(.*?[^\\\\])\\}.*");
    private final PBECipher _cipher = new PBECipher();

}
