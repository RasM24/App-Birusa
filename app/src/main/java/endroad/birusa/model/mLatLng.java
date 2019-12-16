package endroad.birusa.model;

import com.google.android.gms.maps.model.LatLng;

public class mLatLng {

    private double latitude;
    private double longitude;
    private float accuracy;

    public float getAccuracy() {
        return accuracy;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public mLatLng() {

    }

    public mLatLng(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public mLatLng(double latitude, double longitude, float accuracy) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    static public mLatLng get(LatLng pos) {
        return new mLatLng(pos.latitude, pos.longitude);
    }
}