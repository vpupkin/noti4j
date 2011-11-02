package eu.blky.log4j;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import org.apache.log4j.spi.LoggingEvent;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.10.2011::11:33:04<br> 
 */
public class SkypeAppender extends AbstractActivableAppender {

	private String  receiver;
	public String getReceiver() { 
			return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Override
	protected void append(LoggingEvent event) {
		String format = getLayout().format(event);
		sendMessage(format);
	}
	
	public void sendMessage(String content) {
	  try {
	    Chat chatTmp = Skype.chat(receiver);
		chatTmp.send(content);
	  } catch (SkypeException ex) {
		  ex.printStackTrace();

	  }
	}
	
	@Override
	public void close() {
//		Skype.voiceMail(arg0)
	}

	@Override
    public boolean requiresLayout() {
        return true;
    }
	@Override
	protected String[] getBeanPropertyNames() {
		return new String[]{"receiver"};
	}

}


 