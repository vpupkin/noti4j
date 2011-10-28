package eu.blky.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.10.2011::11:33:04<br> 
 */
public class SkypeAppender extends AppenderSkeleton {

	
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

	/**
	 * initiate the connection with local Skype-instance
	 */
	@Override
	public void activateOptions(){
		;
	}
	
	@Override
	public void close() {
//		Skype.voiceMail(arg0)
	}

	@Override
    public boolean requiresLayout() {
        return true;
    }

}


 