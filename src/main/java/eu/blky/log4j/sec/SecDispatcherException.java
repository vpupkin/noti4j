// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecDispatcherException.java

package eu.blky.log4j.sec;


public class SecDispatcherException extends Exception
{

    public SecDispatcherException()
    {
    }

    public SecDispatcherException(String message)
    {
        super(message);
    }

    public SecDispatcherException(Throwable cause)
    {
        super(cause);
    }

    public SecDispatcherException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
