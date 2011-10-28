// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PasswordDecryptor.java

package eu.blky.log4j.Gdata;

import java.util.Map;

// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher:
//            SecDispatcherException

public interface PasswordDecryptor
{

    public abstract String decrypt(String s, Map map, Map map1)
        throws SecDispatcherException;

 //   public static final String ROLE = (_cls1.class.org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor != null ? _cls1.class.org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor : (_cls1.class.org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor = _cls1.class.("org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor"))).getName();


    // Unreferenced inner class org/sonatype/plexus/components/sec/dispatcher/PasswordDecryptor$1

/* anonymous class */
    static class _cls1
    {

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

        static Class class$org$sonatype$plexus$components$sec$dispatcher$PasswordDecryptor; /* synthetic field */
    }

}
