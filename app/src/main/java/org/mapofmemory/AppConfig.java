package org.mapofmemory;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.PersonInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.Observable;

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

    public static List<String> removeTheDuplicates(List<String> myList) {
        for(ListIterator<String> iterator = myList.listIterator(); iterator.hasNext();) {
            String str = iterator.next();
            if(Collections.frequency(myList, str) > 1) {
                iterator.remove();
            }
        }
        return myList;
    }

    public static List<PersonInfo> removePersonDuplicates(List<PersonInfo> personInfos) {
        List<String> res = new ArrayList<>();
        List<PersonInfo> persons = new ArrayList<>();
        for (PersonInfo person : personInfos){
            if (!res.contains(person.getName())){
                res.add(person.getName());
                persons.add(person);
            }
        }
        return persons;
    }

    public static List<String> getDuplicates(List<PersonInfo> myList) {
        List<String> res = new ArrayList<>();
        List<String> persons = Observable.fromIterable(myList)
                .map(personInfo -> personInfo.getName())
                .toList()
                .blockingGet();
        for(ListIterator<String> iterator = persons.listIterator(); iterator.hasNext();) {
            String str = iterator.next();
            if(Collections.frequency(persons, str) >= 2) {
                res.add(str);
            }
        }
        return removeTheDuplicates(res);
    }

    public static int findPersonInfoByName(List<PersonInfo> personInfos, String name) {
        int index = -1;
        for (int i = 0; i <= personInfos.size() - 1; i++){
            if (personInfos.get(i).getName().equals(name)) return i;
        }
        return index;

    }
}
