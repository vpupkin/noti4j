// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SettingsSecurity.java

package eu.blky.log4j.sec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher.model:
//            Config

public class SettingsSecurity
    implements Serializable
{

    public SettingsSecurity()
    {
        modelEncoding = "UTF-8";
    }

    public void addConfiguration(Config config)
    {
        if(!(config instanceof Config))
        {
            throw new ClassCastException("SettingsSecurity.addConfigurations(config) parameter must be instanceof " + (Config.class).getName());
        } else
        {
            getConfigurations().add(config);
            return;
        }
    }

    public List getConfigurations()
    {
        if(configurations == null)
            configurations = new ArrayList();
        return configurations;
    }

    public String getMaster()
    {
        return master;
    }

    public String getModelEncoding()
    {
        return modelEncoding;
    }

    public String getRelocation()
    {
        return relocation;
    }

    public void removeConfiguration(Config config)
    {
        if(!(config instanceof Config))
        {
            throw new ClassCastException("SettingsSecurity.removeConfigurations(config) parameter must be instanceof " + ( Config.class).getName());
        } else
        {
            getConfigurations().remove(config);
            return;
        }
    }

    public void setConfigurations(List configurations)
    {
        this.configurations = configurations;
    }

    public void setMaster(String master)
    {
        this.master = master;
    }

    public void setModelEncoding(String modelEncoding)
    {
        this.modelEncoding = modelEncoding;
    }

    public void setRelocation(String relocation)
    {
        this.relocation = relocation;
    }

    private String master;
    private String relocation;
    private List configurations;
    private String modelEncoding;
}
