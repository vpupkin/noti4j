// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecurityConfigurationXpp3Writer.java

package eu.blky.log4j.sec;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;
 
public class SecurityConfigurationXpp3Writer
{

    public SecurityConfigurationXpp3Writer()
    {
    }

    public void write(Writer writer, SettingsSecurity settingsSecurity)
        throws IOException
    {
        XmlSerializer serializer = new /*hidden.org.codehaus.plexus.util.xml.pull.*/MXSerializer();
        serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
        serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
        serializer.setOutput(writer);
        serializer.startDocument(settingsSecurity.getModelEncoding(), null);
        writeSettingsSecurity(settingsSecurity, "settingsSecurity", serializer);
        serializer.endDocument();
    }

    private void writeConfig(Config config, String tagName, XmlSerializer serializer)
        throws IOException
    {
        if(config != null)
        {
            serializer.startTag(NAMESPACE, tagName);
            if(config.getName() != null)
                serializer.startTag(NAMESPACE, "name").text(config.getName()).endTag(NAMESPACE, "name");
            if(config.getProperties() != null && config.getProperties().size() > 0)
            {
                serializer.startTag(NAMESPACE, "properties");
                ConfigProperty o;
                for(Iterator iter = config.getProperties().iterator(); iter.hasNext(); writeConfigProperty(o, "property", serializer))
                    o = (ConfigProperty)iter.next();

                serializer.endTag(NAMESPACE, "properties");
            }
            serializer.endTag(NAMESPACE, tagName);
        }
    }

    private void writeConfigProperty(ConfigProperty configProperty, String tagName, XmlSerializer serializer)
        throws IOException
    {
        if(configProperty != null)
        {
            serializer.startTag(NAMESPACE, tagName);
            if(configProperty.getName() != null)
                serializer.startTag(NAMESPACE, "name").text(configProperty.getName()).endTag(NAMESPACE, "name");
            if(configProperty.getValue() != null)
                serializer.startTag(NAMESPACE, "value").text(configProperty.getValue()).endTag(NAMESPACE, "value");
            serializer.endTag(NAMESPACE, tagName);
        }
    }

    private void writeSettingsSecurity(SettingsSecurity settingsSecurity, String tagName, XmlSerializer serializer)
        throws IOException
    {
        if(settingsSecurity != null)
        {
            serializer.startTag(NAMESPACE, tagName);
            if(settingsSecurity.getMaster() != null)
                serializer.startTag(NAMESPACE, "master").text(settingsSecurity.getMaster()).endTag(NAMESPACE, "master");
            if(settingsSecurity.getRelocation() != null)
                serializer.startTag(NAMESPACE, "relocation").text(settingsSecurity.getRelocation()).endTag(NAMESPACE, "relocation");
            if(settingsSecurity.getConfigurations() != null && settingsSecurity.getConfigurations().size() > 0)
            {
                serializer.startTag(NAMESPACE, "configurations");
                Config o;
                for(Iterator iter = settingsSecurity.getConfigurations().iterator(); iter.hasNext(); writeConfig(o, "configuration", serializer))
                    o = (Config)iter.next();

                serializer.endTag(NAMESPACE, "configurations");
            }
            serializer.endTag(NAMESPACE, tagName);
        }
    }

    private static final String NAMESPACE = null;

}
