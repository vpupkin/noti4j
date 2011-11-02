package eu.blky.log4j.Gdata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream; 
import java.io.IOException;
import java.io.InputStream;  
import junit.framework.TestCase; 

import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainFactory;
import org.jdesktop.swingx.auth.KeyChain;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  26.10.2011::13:05:32<br> 
 */
public class KeyChainTest extends TestCase {
	char[] masterPassword = "test".toCharArray();
	
	public void testKeysInit() throws IOException {
		// InputStream inputStream =
		// this.getClass().getResourceAsStream("kc.dat"); 
		KeyChain kc = new KeyChain(masterPassword, null);
		String user = "junit";
		char[] password = "secret".toCharArray();
		String server = "example.com";
		kc.addPassword(user, server, password);
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		kc.store(ostream);
		ostream.close();
		//System.out.println(ostream.toString());
	}
	
	
	public void testKeysStore() throws IOException { 
		// InputStream inputStream =
		// this.getClass().getResourceAsStream("kc.dat"); 
		KeyChain kc = new KeyChain(masterPassword, null);
		String user = "junit";
		char[] password = "secret".toCharArray();
		String server = "example.com";
		kc.addPassword(user, server, password);
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		kc.store(ostream);
		ostream.close();
		File storeTmp = calcTmpStore();
		FileOutputStream fwTmp = new FileOutputStream(storeTmp);
		ostream.writeTo(fwTmp);
		ostream.close(); 
	}
	//https://pegasus.peras.fiducia.de/JSPWiki/Wiki.jsp?page=KeyChain
	public void testKeysRestore() throws IOException { 
		File storeTmp = calcTmpStore();		
		InputStream inputStream = new FileInputStream(storeTmp); 
		
		
		KeyChain kc = new KeyChain(masterPassword , inputStream);
		String user = "junit";
		char[] password = "secret".toCharArray();
		String server = "example.com"; 
		char[] actual = kc.getPassword(user, server).toCharArray();
		assertEquals(new String(password),new String( actual)); 
	}
	
	/**
	 * TODO read externally
	 * 
	 * @author vipup
	 * @throws IOException
	 */
	public void testKeysChain() throws IOException { 
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("kc.dat");
		char[] masterPasswordTmp = //System.getenv("MAKEY").toCharArray();
							 masterPassword ;
		KeyChain kc = new KeyChain(masterPasswordTmp, inputStream);
		String user = "junit";
		char[] password = "secret".toCharArray();
		String server = "example.com"; 
		char[] actual = kc.getPassword(user, server).toCharArray();
		assertEquals(new String(password),new String( actual)); 
	}

	public File  calcTmpStore() throws IOException {
		File storeTmp = File.createTempFile("keys.store", "ser");
		File parentDir = storeTmp.getParentFile();
		storeTmp.delete();
		storeTmp  = new File (parentDir, "KeysStore.tmp");
		return storeTmp;
	}
	
 
}


 