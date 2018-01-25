package org.mapofmemory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by The Tronuo on 30.05.2017.
 */

public class SharedPrefs {
    private SharedPreferences sharedPreferences;

    public SharedPrefs(Context context){
        this.sharedPreferences = context.getSharedPreferences("local", Context.MODE_PRIVATE);
    }

    public SharedPrefs(Activity activity){
        this.sharedPreferences = activity.getSharedPreferences("local", Context.MODE_PRIVATE);
    }

    public SharedPrefs(Activity activity, String preferenceType){
        this.sharedPreferences = activity.getSharedPreferences(preferenceType, Context.MODE_PRIVATE);
    }

    public SharedPrefs(Context context, String preferenceType){
        this.sharedPreferences = context.getSharedPreferences(preferenceType, Context.MODE_PRIVATE);
    }

    public boolean contains(String value){
        if (this.sharedPreferences.contains(value)){
            return true;
        }
        else{
            return false;
        }
    }

    public void save(String title, String value){
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putString(title, value);
        ed.commit();
        ed.apply();
    }
    public void saveBoolean (String title, boolean value){
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putBoolean(title, value);
        ed.commit();
        ed.apply();
    }
    public void saveInt (String title, int value){
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putInt(title, value);
        ed.commit();
        ed.apply();
    }
    public void saveLong(String title, long value){
        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putLong(title, value);
        ed.commit();
        ed.apply();
    }
    public long getLong(String title){
        return sharedPreferences.getLong(title, 0);
    }
    public int getInt(String title){
        return sharedPreferences.getInt(title, 0);
    }

    public String get(String title){
        return sharedPreferences.getString(title, "[]");
    }

    public boolean getBoolean(String title){
        return sharedPreferences.getBoolean(title, false);
    }
}
