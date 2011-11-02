 
package eu.blky.log4j.sec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher.model:
//            ConfigProperty

public class Config
    implements Serializable
{

    /**
	 * @author vipup
	 */
	private static final long serialVersionUID = -3362786840946065206L;
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

    public List<ConfigProperty> getProperties()
    {
        if(properties == null)
            properties = new ArrayList<ConfigProperty>();
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

    public void setProperties(List<ConfigProperty> properties)
    {
        this.properties = properties;
    }

    private String name;
    private List<ConfigProperty> properties;
}
