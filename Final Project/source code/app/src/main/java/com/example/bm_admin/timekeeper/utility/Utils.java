package com.example.bm_admin.timekeeper.utility;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.PuzzleBean;
import com.example.bm_admin.timekeeper.bean.SettingsBean;
import com.example.bm_admin.timekeeper.bean.TimeZoneBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by bm-admin on 28/3/17.
 */
public class Utils {


    public static String createDirIfNotExists(String path) {
        File file = new File("");

        if (path != null) {
            file = new File(Environment.getExternalStorageDirectory(), "/"
                    + path);
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
            }
        } else {

            if (!file.mkdirs()) {
                file.mkdirs();
            }
        }
        return file.getAbsolutePath() + "/";

    }

    public static Date getCurrentDayWithTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long compareDates(Date date1, Date date2) {
        //SimpleDateFormat myFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        try {
            long diff = date2.getTime() - date1.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Integer getColors(int number) {
        try {
            String[] strings = new String[]{"#000000", "#d32f2f", "#ff9800", "#0288d1"};
            if (strings.length > number) {
                return Color.parseColor(strings[number]);
            } else {
                return Color.parseColor("#0288d1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Color.parseColor("#0288d1");
        }
    }

    public static Date getDate(Calendar calendar) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            String dateStr = sdf.format(calendar.getTime());
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Calendar parseReturnDate(String returnDate)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = formatter.parse(returnDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String dateFormat(Date date) {
        try {
            //SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            return dt1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return date.toString();
        }
    }

    public static Calendar getCalender(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").parse(dateStr);
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static SettingsBean getSettings(AppConfig appConfig, Context context) {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.SETTINGSTB, null, "email=?",
                    new String[]{appConfig.loginMail}, null, false, false, false, context);
            for (SettingsBean settingsBean : Converter.convertJsonToSettingsBeans(dbStr)) {
                if (settingsBean.getEmail().equalsIgnoreCase(appConfig.loginMail)) {
                    return settingsBean;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SettingsBean();
        }
        return new SettingsBean();
    }

    public static Integer get12HourTime(int hourOfDay) {
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR, hourOfDay);
            return c.get(Calendar.HOUR);
        } catch (Exception e) {
            e.printStackTrace();
            return hourOfDay;
        }
    }

    public static ArrayList<PuzzleBean> setPuzzleBean() {
        ArrayList<PuzzleBean> puzzleBeen = new ArrayList<>();
        puzzleBeen.add(new PuzzleBean("PU001", "Daya has a brother Anil, Daya is the son of Chandra. Bimal is Chandra`s father. In term of relationship, what is Anil of Bimal ?",
                "Son", "Grandson", "Brother", "Grandfather"));
        puzzleBeen.add(new PuzzleBean("PU002", "A  B  C  D  E  F  G  H  I  J  K  L  M N  O  P  Q  R  S  T  U  V  W  X  Y  Z Which letter is the seventh to the right of the thirteenth letter from your left ?",
                "S", "T", "U", "None of these"));
        puzzleBeen.add(new PuzzleBean("PU003", "Arrange the given words Alphabetical Order and choose the one that comes first.",
                "Didactic", "Dictum", "Diastole", "Dictate"));
        puzzleBeen.add(new PuzzleBean("PU004", "In a school, there were five teachers.\n A and B were teaching Hindi and English.\n C and D were teaching English and Geography.\n D and A were teaching Mathematics and Hindi\n E and B were teaching History and French. \n Who among the teachers was teaching maximum number of subjects ?",
                "A and B", "B and C", "C and A", "None of these"));
        puzzleBeen.add(new PuzzleBean("PU005", "Five girls are sitting in a row. Rashi is not adjacent to Sulekha or Abha. Anuradha is not adhacent to Sulekha. Rashi is adjacent to Monika. Monika is at the middle in the row. Then, Anuradha is adjacent to whom out of the following ?",
                "Rashi", "Sulekha", "Monika", "Cannot be determined"));
        puzzleBeen.add(new PuzzleBean("PU006", "Among five boys, Vineet is taller than Manick, but not as tall as Ravi. Jacob is taller than Dilip but shorter than Manick. Who is the tallest in their group ?",
                "Ravi", "Manick", "Vineet", "Cannot be determined"));
        puzzleBeen.add(new PuzzleBean("PU007", "Number of letters skipped in between adjacent letters in the series is two. Which of the following series observes this rule ?",
                "MPSVYBE", "QSVYZCF", "SVZCGJN", "ZCGKMPR"));
        puzzleBeen.add(new PuzzleBean("PU008", "Assertion (A) : In India, people elect their own representatives. Reason (R) : India is a democracy.",
                "Both A and R are true and R is the correct explanation of A.", "A is true but R is false.", "A is false but R is true.", "Both A and R are false."));
        puzzleBeen.add(new PuzzleBean("PU009", "Find out the two signs to be interchanged for making following equation correct :5 + 3 x 8 - 12 ÷ 4 = 3",
                "+ and -", "- and ÷", "+ and x", "+ and ÷"));
        puzzleBeen.add(new PuzzleBean("PU010", "In alphabet series, some alphabets are missing which are given in that order as one of the alternatives below it. Choose the correct alternative.: \nG4T, J10R, M20P, P43N, S90",
                "G4T", "J10R", "M20P", "P43N"));

        setResults();
        return puzzleBeen;
    }

    public static void setResults() {
        AppConfig appConfig = AppConfig.getInstance();
        appConfig.answerMap.put("PU001", "Grandson");
        appConfig.answerMap.put("PU002", "T");
        appConfig.answerMap.put("PU003", "Diastole");
        appConfig.answerMap.put("PU004", "None of these");
        appConfig.answerMap.put("PU005", "Rashi");
        appConfig.answerMap.put("PU006", "Ravi");
        appConfig.answerMap.put("PU007", "  MPSVYBE");
        appConfig.answerMap.put("PU008", "Both A and R are true and R is the correct explanation of A.");
        appConfig.answerMap.put("PU009", "- and ÷");
        appConfig.answerMap.put("PU010", "J10R");
    }

    public static boolean checkPermission(Context context) {
        int permission = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<TimeZoneBean> loadTimeZones() {
        try {
            ArrayList<TimeZoneBean> timeZoneList = new ArrayList<>();
            String[] ids = TimeZone.getAvailableIDs();
            for (String id : ids) {
                timeZoneList.add(new TimeZoneBean(TimeZone.getTimeZone(id),displayTimeZone(TimeZone.getTimeZone(id)), java.util.TimeZone.getTimeZone(id).getDisplayName()));
                System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
            }
            return timeZoneList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static String displayTimeZone(TimeZone tz) {
        String result = "";
        try {
            long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                    - TimeUnit.HOURS.toMinutes(hours);
            // avoid -4:-30 issue
            minutes = Math.abs(minutes);

            if (hours > 0) {
                result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
            } else {
                result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setTimeZone(String country){
        try {
            ArrayList<TimeZoneBean> timeZoneBeen = loadTimeZones();
            for (TimeZoneBean bean : timeZoneBeen) {
                System.out.println("Time ZOne 1:" + country);
                System.out.println("Time ZOne 2:" + bean.getSubTitle());
                if (country.contains(bean.getSubTitle())) {
                    System.out.println("Time ZOne 3:" + bean.getSubTitle());

                    TimeZone.setDefault(TimeZone.getTimeZone(bean.getTitle()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
