 
package eu.blky.log4j.sec;


public class SecDispatcherException extends Exception
{

    /**
	 * @author vipup
	 */
	private static final long serialVersionUID = -2888701662942798096L;

	public SecDispatcherException()
    {
    }

    public SecDispatcherException(String message)
    {
        super(message);
    }

    public SecDispatcherException(Throwable cause)
    {
        super(cause);
    }

    public SecDispatcherException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
