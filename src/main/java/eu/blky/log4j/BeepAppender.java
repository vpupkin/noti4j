package eu.blky.log4j;

import java.awt.Toolkit;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  20.10.2011::11:57:21<br> 
 
 * Этот аппендер пикает PC-спикером и выводит на консоль сообщение.
 */
public class BeepAppender extends AppenderSkeleton {
    /**
     * Пикаем и выводим сообщение.
     * @param event отсюда берётся сообщение.
     */
    @Override
    protected void append(LoggingEvent event) {
        Toolkit.getDefaultToolkit().beep();
        System.out.println(event.getMessage());
    }

    /**
     * ресурсы не выделялись - закрывать ничего не надо.
     */
    @Override
    public void close() {

    }
    /**
     * Layout не используется.
     */
    @Override
    public boolean requiresLayout() {
        return false;
    }
}


 