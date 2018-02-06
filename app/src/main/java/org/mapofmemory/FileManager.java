package org.mapofmemory;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by The Tronuo on 06.02.2018.
 */

public class FileManager {
    final static String TARGET_BASE_PATH = Environment.getExternalStorageDirectory() + "/osmdroid/tiles/";

    private AssetManager assetManager;

    public FileManager(AssetManager assetManager){
        this.assetManager = assetManager;
    }


    public void copyFileIfNeeded(String filename) {
        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        File f = new File(TARGET_BASE_PATH + filename);
        if (f.exists()) return;
        try {
            Log.i("tag", "copyFile() " + filename);
            in = assetManager.open(filename);
            newFileName = TARGET_BASE_PATH + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of "+newFileName);
            Log.e("tag", "Exception in copyFile() "+e.toString());
        }

    }
}
