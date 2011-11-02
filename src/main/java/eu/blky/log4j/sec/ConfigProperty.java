 
package eu.blky.log4j.sec;

import java.io.Serializable;

public class ConfigProperty
    implements Serializable
{

    /**
	 * @author vipup
	 */
	private static final long serialVersionUID = -2022200072220175021L;
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
