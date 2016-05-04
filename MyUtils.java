package diogo.democon.util;

import android.content.*;
import android.net.*;
import android.util.*;

import java.text.*;
import java.util.*;

/**
 * Util class
 * <p/>
 * Created by diogohenrique on 18/02/2016.
 */
public class MyUtils {

    private static final String TAG = "app";
    private SimpleDateFormat simpleDateFormat;

    public MyUtils() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);//default format
    }

    /**
     * Format a date
     * <p/>
     * Example: EEE, dd MMM yyyy HH:mm
     *
     * @param date
     * @return
     */
    public String formatDate(Date date, String format) {

        String dateFormat;
        SimpleDateFormat formatDate = new SimpleDateFormat(format, Locale.ENGLISH);

        try {
            dateFormat = formatDate.format(date);
        } catch (Exception e) {
            Log.i(TAG, "formatDate: error dateformat = " + e.getMessage());
            dateFormat = "----------";
        }

        return dateFormat;
    }

    /**
     * Check if there is internet
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    //******** GETS
    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
