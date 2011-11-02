 
package eu.blky.log4j.sec;


// Referenced classes of package org.sonatype.plexus.components.cipher:
//            PlexusCipherException

public interface PlexusCipher
{

    public abstract String encrypt(String s, String s1)
        throws PlexusCipherException;

    public abstract String encryptAndDecorate(String s, String s1)
        throws PlexusCipherException;

    public abstract String decrypt(String s, String s1)
        throws PlexusCipherException;

    public abstract String decryptDecorated(String s, String s1)
        throws PlexusCipherException;

    public abstract boolean isEncryptedString(String s);

    public abstract String unDecorate(String s)
        throws PlexusCipherException;

    public abstract String decorate(String s);

//    public static final String ROLE = (_cls1.class.org.sonatype.plexus.components.cipher.PlexusCipher != null ? _cls1.class.org.sonatype.plexus.components.cipher.PlexusCipher :  PlexusCipher.class.getName();
    public static final char ENCRYPTED_STRING_DECORATION_START = 123;
    public static final char ENCRYPTED_STRING_DECORATION_STOP = 125;


    // Unreferenced inner class org/sonatype/plexus/components/cipher/PlexusCipher$1

/* anonymous class */
//    static class _cls1
//    {
//
//        static Class _mthclass$(String x0)
//        {
//            try
//            {
//                return Class.forName(x0);
//            }
//            catch(ClassNotFoundException x1)
//            {
//                throw (new NoClassDefFoundError()).initCause(x1);
//            }
//        }
//
//        static Class class$org$sonatype$plexus$components$cipher$PlexusCipher; /* synthetic field */
//    }

}
