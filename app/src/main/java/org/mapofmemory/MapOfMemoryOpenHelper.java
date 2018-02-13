package org.mapofmemory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mapofmemory.entities.AboutEntity;
import org.mapofmemory.entities.AboutEntityTable;
import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.DayOfMemoryTable;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.PersonEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;
import org.mapofmemory.entities.RouteEntity;
import org.mapofmemory.entities.RouteEntityTable;

/**
 * Created by The Tronuo on 25.01.2018.
 */

public class MapOfMemoryOpenHelper extends SQLiteOpenHelper {
    final private static String DATABASE_NAME = "Data";
    final private static int DATABASE_VERSION = 23;

    public MapOfMemoryOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        MonumentEntityTable.createTable(db);
        PlaceEntityTable.createTable(db);
        PersonEntityTable.createTable(db);
        DayOfMemoryTable.createTable(db);
        AboutEntityTable.createTable(db);
        RouteEntityTable.createTable(db);
    }


    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MonumentEntityTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PlaceEntityTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PersonEntityTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DayOfMemoryTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AboutEntityTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RouteEntityTable.NAME);
            onCreate(db);
        }
        MonumentEntityTable.updateTable(db, oldVersion);
        PlaceEntityTable.updateTable(db, oldVersion);
    }
}
