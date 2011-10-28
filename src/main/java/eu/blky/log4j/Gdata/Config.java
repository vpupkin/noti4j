// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Config.java

package eu.blky.log4j.Gdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher.model:
//            ConfigProperty

public class Config
    implements Serializable
{

    public Config()
    {
    }

    public void addProperty(ConfigProperty configProperty)
    {
        if(!(configProperty instanceof ConfigProperty))
        {
            throw new ClassCastException("Config.addProperties(configProperty) parameter must be instanceof " + ( ConfigProperty.class).getName());
        } else
        {
            getProperties().add(configProperty);
            return;
        }
    }

    public String getName()
    {
        return name;
    }

    public List getProperties()
    {
        if(properties == null)
            properties = new ArrayList();
        return properties;
    }

    public void removeProperty(ConfigProperty configProperty)
    {
        if(!(configProperty instanceof ConfigProperty))
        {
            throw new ClassCastException("Config.removeProperties(configProperty) parameter must be instanceof " + ( ConfigProperty.class).getName());
        } else
        {
            getProperties().remove(configProperty);
            return;
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setProperties(List properties)
    {
        this.properties = properties;
    }

    private String name;
    private List properties;
}
