package eu.blky.log4j.Gdata;
 
import java.io.IOException;  
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar; 
import java.util.TimeZone;
import org.apache.log4j.spi.LoggingEvent; 
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.Recurrence;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.util.ServiceException;
import eu.blky.log4j.AbstractActivableAppender;

/**
 * 
 * @see http://www.javaexpress.pl/article/show/Log4J_a_komunikatory_internetowe
 * 
 *      <b>Description:TODO</b>
 * @author vipup<br>
 * <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 *         Creation: 20.10.2011::11:33:04<br>
 */
public class GICalcAppender extends AbstractActivableAppender {
	public static void setProxy(String proxyHost, String proxyPort)// , String
																	// host, int
																	// port
	{
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		System.setProperty("http.proxyHost", proxyHost);
		System.setProperty("http.proxyPort", proxyPort);
		System.setProperty("https.proxyHost", proxyHost);
		System.setProperty("https.proxyPort", proxyPort);

	}
	private static final String METAFEED_URL_BASE = "https://www.google.com/calendar/feeds/";

	// The string to add to the user's metafeedUrl to access the event feed for
	// their primary calendar.
	private static final String EVENT_FEED_URL_SUFFIX = "/private/full";

	// The URL for the metafeed of the specified user.
	// (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
	private static URL metafeedUrl = null;
	// The URL for the event feed of the specified user's primary calendar.
	// (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/private/full)
	private static URL eventFeedUrl = null;

	/**
	 * Helper method to create either single-instance or recurring events. For
	 * simplicity, some values that might normally be passed as parameters (such
	 * as author name, email, etc.) are hard-coded.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @param recurData
	 *            Recurrence value for the event, or null for single-instance
	 *            events.
	 * @param isQuickAdd
	 *            True if eventContent should be interpreted as the text of a
	 *            quick add event.
	 * @param wc
	 *            A WebContent object, or null if this is not a web content
	 *            event.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             Error communicating with the server.
	 */
	private static CalendarEventEntry createEvent(CalendarService service,
			String eventTitle, String eventContent, String recurData,
			boolean isQuickAdd, WebContent wc) throws ServiceException,
			IOException {
		CalendarEventEntry myEntry = new CalendarEventEntry();

		myEntry.setTitle(new PlainTextConstruct(eventTitle));
		myEntry.setContent(new PlainTextConstruct(eventContent));
		myEntry.setQuickAdd(isQuickAdd);
		myEntry.setWebContent(wc);

		// If a recurrence was requested, add it. Otherwise, set the
		// time (the current date and time) and duration (30 minutes)
		// of the event.
		if (recurData == null) {
			Calendar calendar = new GregorianCalendar();
			// 15 mins after now
			calendar.add(Calendar.MINUTE, 15);
			DateTime startTime = new DateTime(calendar.getTime(),
					TimeZone.getDefault());

			calendar.add(Calendar.MINUTE, 30);
			DateTime endTime = new DateTime(calendar.getTime(),
					TimeZone.getDefault());

			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);
		} else {
			Recurrence recur = new Recurrence();
			recur.setValue(recurData);
			myEntry.setRecurrence(recur);
		}

		// Send the request and receive the response:
		return service.insert(eventFeedUrl, myEntry);
	}
	/**
	 * Creates a single-occurrence event.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             Error communicating with the server.
	 */
	private static CalendarEventEntry createSingleEvent(
			CalendarService service, String eventTitle, String eventContent)
			throws ServiceException, IOException {
		return createEvent(service, eventTitle, eventContent, null, false, null);
	}
	
	@Override
	protected String[] getBeanPropertyNames() {
		return new String[]{"userName","userPassword", "proxyPort", "proxyHost"};
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	String userName;
	String userPassword;

	public String getProxyPort() { 
			return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getProxyHost() { 
			return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	private String proxyPort;
	private String proxyHost;

	@Override
	protected void append(LoggingEvent event) {
		String message = event.getMessage().toString();
		{  
			setProxy(proxyHost, proxyPort);
		}

		// Create the necessary URL objects.
		try {
			metafeedUrl = new URL(METAFEED_URL_BASE + userName);
			eventFeedUrl = new URL(METAFEED_URL_BASE + userName
					+ EVENT_FEED_URL_SUFFIX);
		} catch (MalformedURLException e) {
			// Bad URL
			System.err.println("Uh oh - you've got an invalid URL.");
			e.printStackTrace();
			return;
		}

		CalendarService myService = new CalendarService(
				"exampleCo-exampleApp-1");

		// Demonstrate creating a single-occurrence event.
		CalendarEventEntry singleEvent = null;
		try {
			myService.setUserCredentials(userName, userPassword);
			singleEvent = createSingleEvent(myService, message,
					event.getLoggerName());
			// singleEvent = addReminder(singleEvent, 15, Method.EMAIL);
			singleEvent = addReminder(singleEvent, 15, Method.ALL);
			singleEvent = addReminder(singleEvent, 15, Method.SMS);
			singleEvent = addReminder(singleEvent, 1, Method.EMAIL);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Successfully created event " + singleEvent.getTitle().getPlainText());
	}
	/**
	 * Adds a reminder to a calendar event.
	 * 
	 * @param entry
	 *            The event to update.
	 * @param numMinutes
	 *            Reminder time, in minutes.
	 * @param methodType
	 *            Method of notification (e.g. email, alert, sms).
	 * @return The updated EventEntry object.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             Error communicating with the server.
	 */
	private static CalendarEventEntry addReminder(CalendarEventEntry entry,
			int numMinutes, Method methodType) throws ServiceException,
			IOException {
		Reminder reminder = new Reminder();
		reminder.setMinutes(numMinutes);
		reminder.setMethod(methodType);
		entry.getReminder().add(reminder);

		return entry.update();
	}
	@Override
	public boolean requiresLayout() {
		return false;

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (1 == 1)
			throw new RuntimeException("not yet implemented since 25.10.2011");
		else {
		}
	}
	   

}
