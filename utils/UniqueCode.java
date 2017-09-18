

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Anup Dey on 27-4-16.
 */
public class UniqueCode {

    public static int getCode(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        return calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH)
                                            + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE)
                                            + calendar.get(Calendar.SECOND) +  calendar.get(Calendar.MILLISECOND);
    }


}
