package org.mapofmemory;

import android.app.Application;

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;

import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLitePutResolver;

/**
 * Created by The Tronuo on 25.01.2018.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
