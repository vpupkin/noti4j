// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecDispatcher.java

package eu.blky.log4j.sec;


// Referenced classes of package org.sonatype.plexus.components.sec.dispatcher:
//            SecDispatcherException

public interface SecDispatcher
{

    public abstract String decrypt(String s)
        throws SecDispatcherException;

//    public static final String ROLE = (_cls1.class.org.sonatype.plexus.components.sec.dispatcher.SecDispatcher != null ? _cls1.class.org.sonatype.plexus.components.sec.dispatcher.SecDispatcher : (_cls1.class.org.sonatype.plexus.components.sec.dispatcher.SecDispatcher = _cls1.class.("org.sonatype.plexus.components.sec.dispatcher.SecDispatcher"))).getName();
    public static final String SYSTEM_PROPERTY_MASTER_PASSWORD[] = {
        "settings.master.password", "settings-master-password"
    };
    public static final String SYSTEM_PROPERTY_SERVER_PASSWORD[] = {
        "settings.server.password", "settings-server-password"
    };


    // Unreferenced inner class org/sonatype/plexus/components/sec/dispatcher/SecDispatcher$1

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
//        static Class class$org$sonatype$plexus$components$sec$dispatcher$SecDispatcher; /* synthetic field */
//    }

}
