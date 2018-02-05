package org.mapofmemory.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by The Tronuo on 04.02.2018.
 */
@StorIOSQLiteType(table = "Route")
public class RouteEntity {
    @StorIOSQLiteColumn(name = "id", key = true)
    @SerializedName("id")
    @Expose
    private String id;
    @StorIOSQLiteColumn(name = "place_id")
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @StorIOSQLiteColumn(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @StorIOSQLiteColumn(name = "bus_route")
    @SerializedName("bus_route")
    @Expose
    private String busRoute;
    @StorIOSQLiteColumn(name = "car_route")
    @SerializedName("car_route")
    @Expose
    private String carRoute;
    @StorIOSQLiteColumn(name = "start_lat")
    @SerializedName("start_lat")
    @Expose
    private String startLat;
    @StorIOSQLiteColumn(name = "start_lng")
    @SerializedName("start_lng")
    @Expose
    private String startLng;

    @StorIOSQLiteCreator
    public RouteEntity(String id, String placeId, String address, String busRoute, String carRoute, String startLat, String startLng){
        this.id = id;
        this.placeId = placeId;
        this.address = address;
        this.busRoute = busRoute;
        this.carRoute = carRoute;
        this.startLat = startLat;
        this.startLng = startLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public String getCarRoute() {
        return carRoute;
    }

    public void setCarRoute(String carRoute) {
        this.carRoute = carRoute;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLng() {
        return startLng;
    }

    public void setStartLng(String startLng) {
        this.startLng = startLng;
    }
}
