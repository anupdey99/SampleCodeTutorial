
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Utility {


    private Context context;

    public Utility(Context context) {
        this.context = context;
    }



    /////////////// verifyReminder ////////////

    public  boolean verifyReminder(String userInputDate) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar todayData = Calendar.getInstance();
        String dateMain = dateFormatter.format(todayData.getTime());



        int userInputYear = Integer.parseInt(userInputDate.substring(0, 4));
        int userInputMonth = Integer.parseInt(userInputDate.substring(5, 7));
        int userInputDay = Integer.parseInt(userInputDate.substring(8, 10));


        int verifyReminder = getAge(userInputYear, userInputMonth, userInputDay);

        return verifyReminder < 0 || dateMain.equals(userInputDate) || userInputDate.equals("0000-00-00");
    }


    /////////////// verifyBirthday  ////////////

    public boolean verifyBirthday(String userInputDate) {

        int userInputYear = Integer.parseInt(userInputDate.substring(0, 4));
        int userInputMonth = Integer.parseInt(userInputDate.substring(5, 7));
        int userInputDay = Integer.parseInt(userInputDate.substring(8, 10));

        int verifyBirthday = getAge(userInputYear, userInputMonth, userInputDay);

        return verifyBirthday >= 0;
    }


    /////////////// Age Calculation From String ////////////

    public int getAgeFromString(String userInputDate) {

        if (userInputDate != null && !userInputDate.isEmpty()){

            int userInputYear = Integer.parseInt(userInputDate.substring(0, 4));
            int userInputMonth = Integer.parseInt(userInputDate.substring(5, 7));
            int userInputDay = Integer.parseInt(userInputDate.substring(8, 10));

            return getAge(userInputYear, userInputMonth, userInputDay);
        }

        return 0;


    }




    /////////////// Age Calculation From int ////////////


    public int getAge(int year, int month, int date) {

        Calendar today = Calendar.getInstance();

        int todayDate = today.get(Calendar.DATE);
        int todayMonth = today.get(Calendar.MONTH);
        int todayYear = today.get(Calendar.YEAR);


        int daysOfMonth[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (year % 400 == 0) {
            daysOfMonth[2] = 29;
        } else if (year % 400 != 0) {
            if (year % 100 == 0) {
                daysOfMonth[2] = 28;
            } else if (year % 4 == 0) {
                daysOfMonth[2] = 29;
            }
        }


        date = daysOfMonth[month] - date + todayDate + 1;

        month = (12 - month) + todayMonth;

        if (date > daysOfMonth[todayMonth + 1]) {
            date = date - daysOfMonth[todayMonth];
            month = month + 1;
        }

        year = todayYear - year - 1;

        if (month > 11) {
            month = month - 12;
            year = year + 1;
        }



        return year;
    }

    private float calculateBMIValue(String weightStr, String heightStr) {

        if (heightStr != null && !"".equals(heightStr)
                && weightStr != null  &&  !"".equals(weightStr)) {
            // cm to m conversion
            //float heightValue = Float.parseFloat(heightStr) / 100;
            float heightValue = Float.parseFloat(heightStr);
            float weightValue = Float.parseFloat(weightStr);

            return weightValue / (heightValue * heightValue);
        }
        return (float) -1;

    }

    public String calculateBMI(String weight_KG, String height_Meter) {

        float bmi = calculateBMIValue(weight_KG,height_Meter);
        String bmiLabel;

        if (bmi != (float) -1){

            if (Float.compare(bmi, 15f) <= 0) {
                bmiLabel = "Underweight (Very Severely)";
            } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
                bmiLabel = "Underweight (Severely)";
            } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
                bmiLabel = "Underweight";
            } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
                bmiLabel = "Normal";
            } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
                bmiLabel = "Overweight";
            } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
                bmiLabel = "Obese Class I";
            } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
                bmiLabel = "Obese Class II";
            } else {
                bmiLabel = "Obese Class III";
            }


            return new DecimalFormat("##.##").format(bmi) + " " + bmiLabel;
        }

        return null;



    }


    public static String convert12Hour (String time24){

        if (time24 != null && !time24.trim().isEmpty() ){
            try {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm",Locale.US);
                Date date = format.parse(time24);
                SimpleDateFormat format12 = new SimpleDateFormat("hh:mm a",Locale.US);
                return format12.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

        }else return null;

    }

    public static String convert24Hours(String time){

        if (time != null && !time.trim().isEmpty() ){
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.US);
                Date date = timeFormat.parse(time);

                return timeFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

        }else return null;


    }

    public static Boolean verifyRemainder(String date, String time)  {

        String date_time = String.valueOf(date + " " + time);
        try {
            return !new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).parse(date_time).before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static int dateWhen(String date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        long milliSeconds = System.currentTimeMillis();
        Date now = new Date(milliSeconds);
        Date testDate = null;

        try {
            testDate = simpleDateFormat.parse(date);
            String newDate = simpleDateFormat.format(now);
            now = simpleDateFormat.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (testDate != null){

            return now.compareTo(testDate);


        }else return -2; // error

    }

    public static int[] getYearMonthDay (String date){
        // yyyy-MM-dd
        int[] calender = new int[3];
        calender[0] = Integer.parseInt(date.substring(0, 4));
        calender[1] = Integer.parseInt(date.substring(5, 7));
        calender[2] = Integer.parseInt(date.substring(8, 10));
        return calender;
    }

    public static int[] getHourMinute(String time){

        // HH:mm
        int[] calender = new int[2];
        calender[0] = Integer.parseInt(time.substring(0, 2));
        calender[1] = Integer.parseInt(time.substring(3, 5));
        return  calender;
    }

    public static Drawable getTintedDrawable(@NonNull final Context context,
                                             @DrawableRes int drawableRes,
                                             @ColorRes int colorRes) {
        Drawable d = AppCompatResources.getDrawable(context, drawableRes);
        d = DrawableCompat.wrap(d);
        DrawableCompat.setTint(d.mutate(), ContextCompat.getColor(context, colorRes));
        return d;
    }



}//
