package eu.blky.log4j.SMS;

import java.util.Date;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  22.10.2011::22:45:52<br> 
 */
//DemoSms.java
public class DemoSms
{
public static void main(String args[])
{
SMSClient sc = new SMSClient(SMSClient.SYNCHRONOUS);
sc.sendMessage("004916098989882","Hello "+new Date()+"\n\n\n");
}

}
//ends here