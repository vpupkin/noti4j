// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConfigProperty.java

package eu.blky.log4j.Gdata;

import java.io.Serializable;

public class ConfigProperty
    implements Serializable
{

    public ConfigProperty()
    {
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    private String name;
    private String value;
}
