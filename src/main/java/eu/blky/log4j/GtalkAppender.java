package eu.blky.log4j;

import javax.security.auth.callback.CallbackHandler;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
 

/** 
 * 
 * @see http://www.javaexpress.pl/article/show/Log4J_a_komunikatory_internetowe
 * 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.10.2011::11:33:04<br> 
 */
public class GtalkAppender extends AppenderSkeleton {
	  private String user;
	  private String password;
	  private String receiver;
	  // tu umieść gettery i settery dla powyższych trzech zmiennych

	  private boolean isFirst = true;
	  XMPPConnection connection;
	   
	    @Override
	  protected void append(LoggingEvent event) {
	    if (isFirst) {
	      makeConnection();
	      isFirst = !isFirst;
	    }
	    Message msg = new Message(receiver, Message.Type.chat);
	    msg.setBody(getLayout().format(event));
	    connection.sendPacket(msg);
	    
	  }


		void makeConnection() {
			String host = "talk.google.com";
			int port = 5222;
			String sName = "gmail.com";
			ProxyInfo proxy = new ProxyInfo(ProxyType.SOCKS4 , "proxy", 8080, null, null);
			ConnectionConfiguration  connConfig = new ConnectionConfiguration(host, port, sName,proxy);
			 connection = new XMPPConnection(connConfig);
			  try {
			    connection.connect();
			    connection.login(user, password);
			  } catch (XMPPException ex) {
				  ex.printStackTrace();
			  }
			  while (!connection.isConnected()) {
			  }
		}

	  @Override
	  public boolean requiresLayout() {
	    return true;
	  }

	  @Override
	  public void close() {
		connection.disconnect();
	  }

	public String getPassword() { 
			return password;
	}

	public String getReceiver() { 
			return receiver;
	}

	public String getUser() { 
			return user;
	}
 

	public void setPassword(String password) {
		this.password = password;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * initiate the connection with local Skype-instance
	 */
	@Override
	public void activateOptions(){
		makeConnection() ;
	}
	
	private final void _toDoConnectionVieProxy(){
    	//ProxyInfo proxy = new ProxyInfo(ProxyType.HTTP, "proxy.host", 8080, null, null);
//    	ProxyInfo proxy = new ProxyInfo(ProxyType.HTTP, "proxy.host", 8080, null, null);
      if (isFirst) {
//          connConfig = new ConnectionConfiguration("talk.google.com", 5222, "googlemail.com",proxy);
    	  ConnectionConfiguration  connConfig = new ConnectionConfiguration("talk.google.com", 5222, "googlemail.com" );
        connConfig.setServiceName("googlemail.com");
        connConfig.setSASLAuthenticationEnabled(false);
        // You have to put this code before you login

        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        CallbackHandler cb = new LocalCallbackHandler();
		//connection = new XMPPConnection(connConfig , cb );

        // turn on the enhanced debugger
        XMPPConnection.DEBUG_ENABLED = true;
        connection = new XMPPConnection(connConfig   );
        try {
          connection.connect();
        } catch (XMPPException ex) {
        	ex.printStackTrace();
        }
        while (!connection.isConnected()) {
        	try {
                connection.login(user, password);

        		connection.connect();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        isFirst = !isFirst;
      }
	}
	
	    protected void _append(LoggingEvent event) {
	      Message msg = new Message(receiver, Message.Type.chat);
	      msg.setBody(getLayout().format(event));
	      connection.sendPacket(msg);
	    }
	}



 