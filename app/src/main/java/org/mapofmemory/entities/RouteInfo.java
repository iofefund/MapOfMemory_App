package org.mapofmemory.entities;

import android.graphics.drawable.Drawable;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class RouteInfo {
    private String header, message;
    private Drawable icon;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getHeader() {
        return header;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getMessage() {
        return message;
    }
}
