package org.mapofmemory;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class AppConfig {
    final public static String BASE_URL = "https://mapofmemory.org/api/v1/";
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static String convertDate(String currentDate){
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("d MMMM yyyy");
        try {
            Date date = originalFormat.parse(currentDate);
            return targetFormat.format(date);
        }
        catch (Exception e){
            return "";
        }
    }
}
