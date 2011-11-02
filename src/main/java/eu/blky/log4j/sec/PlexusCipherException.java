 
package eu.blky.log4j.sec;


public class PlexusCipherException extends Exception
{

    /**
	 * @author vipup
	 */
	private static final long serialVersionUID = 4726529313465825414L;

	public PlexusCipherException()
    {
    }

    public PlexusCipherException(String message)
    {
        super(message);
    }

    public PlexusCipherException(Throwable cause)
    {
        super(cause);
    }

    public PlexusCipherException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
