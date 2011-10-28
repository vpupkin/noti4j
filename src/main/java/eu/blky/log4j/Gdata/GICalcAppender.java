package eu.blky.log4j.Gdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.OptionConverter;
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
public class GICalcAppender extends AppenderSkeleton {
	public static void setProxy(String proxyHost, String proxyPort)// , String
																	// host, int
																	// port
	{
		System.setProperty("java.protocol.handler.pkgs",
				"com.sun.net.ssl.internal.www.protocol");
		System.setProperty("http.proxyHost", proxyHost);
		System.setProperty("http.proxyPort", proxyPort);
		System.setProperty("https.proxyHost", proxyHost);
		System.setProperty("https.proxyPort", proxyPort);

	}
	private static final String METAFEED_URL_BASE = "https://www.google.com/calendar/feeds/";

	// The string to add to the user's metafeedUrl to access the event feed for
	// their primary calendar.
	private static final String EVENT_FEED_URL_SUFFIX = "/private/full";

	private static final String[] keys = {"userName", "userPassword"};

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

	@Override
	protected void append(LoggingEvent event) {
		String message = event.getMessage().toString();
		{
			// setProxy("proxy", "8080");
			setProxy("127.0.0.1", "9666");
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
		System.out.println("Successfully created event "
				+ singleEvent.getTitle().getPlainText());
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
	@Override
	public void activateOptions() {
		String uHome = System.getProperty("user.home");
		String uDir = System.getProperty("user.dir");
		System.out.println(uDir);
		File iUserCfg = new File(uHome, ".l");
		if (iUserCfg.exists()) {
			Properties lProps = new Properties();
			FileReader reader;
			try {
				reader = new FileReader(iUserCfg);
				lProps.load(reader);
				for (String key : keys) {
					String keyToSub = this.get(key);
					keyToSub = keyToSub.trim();
					if (keyToSub.startsWith("${") && keyToSub.endsWith("}"))
						keyToSub = keyToSub.substring(2, keyToSub.length() - 1);
					String value = OptionConverter.findAndSubst(keyToSub,
							lProps);
					// iterate thru bean-props
					if (value.startsWith("{") && value.endsWith("}")) {
						// try to restore the value from keyChain
						value = decryptPassword(value);
					}
					set(key, value);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecDispatcherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PlexusCipherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("asdasdasda");
	}
	private String decryptPassword(String value) throws SecDispatcherException,
			PlexusCipherException {
		// else if ( commandLine.hasOption( CLIManager.ENCRYPT_PASSWORD ) )
		// {
		// String passwd = commandLine.getOptionValue(
		// CLIManager.ENCRYPT_PASSWORD );
		//
		// dispatcher = (DefaultSecDispatcher) embedder.lookup(
		// SecDispatcher.ROLE );
		// String configurationFile = dispatcher.getConfigurationFile();
		// if ( configurationFile.startsWith( "~" ) )
		// {
		// configurationFile = System.getProperty( "user.home" ) +
		// configurationFile.substring( 1 );
		// }
		// String file = System.getProperty(
		// DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION, configurationFile
		// );
		// embedder.release( dispatcher );
		//
		// String master = null;
		//
		// SettingsSecurity sec = SecUtil.read( file, true );
		// if ( sec != null )
		// {
		// master = sec.getMaster();
		// }
		//
		// if ( master == null )
		// {
		// System.err.println(
		// "Master password is not set in the setting security file" );
		//
		// return 1;
		// }
		//
		// DefaultPlexusCipher cipher = new DefaultPlexusCipher();
		// String masterPasswd =
		// cipher.decryptDecorated( master,
		// DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION );
		// System.out.println( cipher.encryptAndDecorate( passwd, masterPasswd )
		// );
		//
		// return 0;
		// }

		String configurationFile = "master.xml";
		if (configurationFile.startsWith("~")) {
			configurationFile = System.getProperty("user.home")
					+ configurationFile.substring(1);
		}
		//new File(".").getAbsolutePath()
		String file = System.getProperty(
				DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION,
				configurationFile);
		SettingsSecurity sec = SecUtil.read(file, true);
		String master = null;
		if (sec != null) {
			master = sec.getMaster();
		}
		DefaultPlexusCipher cipher = new DefaultPlexusCipher();
		String masterPasswd = cipher.decryptDecorated(master,
				DefaultSecDispatcher.SYSTEM_PROPERTY_SEC_LOCATION);
		String pwdTmp = cipher.decrypt(value , masterPasswd);
		return pwdTmp;
	}
	// TODO refactor to reflection-way
	private String get(String key) {
		String retval = null;
		if ("userName".equals(key))
			retval = getUserName();
		if ("userPassword".equals(key))
			retval = getUserPassword();
		return retval;
	}
	// TODO refactor to reflection-way
	private void set(String key, String value) {
		if ("userName".equals(key))
			setUserName(value);
		if ("userPassword".equals(key))
			setUserPassword(value);
	}
}
