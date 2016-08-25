
package com.hp.test.framework.staf;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author $hpedservice
 */
public class GetDate {
    //public static String currentdate;
    public  static String getdate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       Date date = new Date();
         return dateFormat.format(date);
    }
    
}
