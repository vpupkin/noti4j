package eu.blky.log4j.SMS; 

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;  
import eu.blky.log4j.SMS.SMSClient;
import eu.blky.log4j.SMS.Sender;

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
public class SMSAppender extends AppenderSkeleton {  

	//csca
	public  void setCsca(String csca) {
		Sender.csca = csca;
	} 
	
	
	public  void setDatabits(int databits) {
		Sender.databits = databits;
	} 

	public  void setParity(int parity) {
		Sender.parity = parity;
	} 

	public  void setBaudRate(int baudRate) {
		Sender.baudRate = baudRate;
	}
 
	public  void setFlowControlOut(int flowControlOut) {
		Sender.flowControlOut = flowControlOut;
	} 
	public  void setPortName(String portName) {
		Sender.portName = portName;
	} 

	public  void setFlowControlIn(int flowControlIn) {
		Sender.flowControlIn = flowControlIn;
	}
	public  void setStopbits(int stopbits) {
		Sender.stopbits = stopbits;
	} 
	   
	    private String recipient;

		public String getRecipient() { 
				return recipient;
		}

		public void setRecipient(String recipient) {
			this.recipient = recipient;
		}

		@Override
	  protected void append(LoggingEvent event) {
	    	SMSClient scTmp = this.getOrCreateSMSClient();
	    	String toTmp = recipient;
	    	String message = event.getMessage().toString();
			scTmp.sendMessage(toTmp,message);
	  }

		SMSClient sender = null;
		private SMSClient getOrCreateSMSClient() {
			sender = sender==null?new SMSClient(SMSClient.SYNCHRONOUS):sender;
			return  sender;
			
		}

		@Override
		public void close() {
			if (sender !=null){
				sender .destroy();
			}
		}

		@Override
		public boolean requiresLayout() { 
			return false;
			 
		}
	}



 