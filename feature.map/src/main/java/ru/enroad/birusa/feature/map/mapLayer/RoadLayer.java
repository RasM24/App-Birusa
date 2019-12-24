package ru.enroad.birusa.feature.map.mapLayer;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.enroad.birusa.feature.map.model.Road;

public class RoadLayer extends BaseLayer {

    List<Road> roads = new ArrayList<>();

    public RoadLayer(Context context) {
        super.context = context;
        loadData();
    }

    private void loadData() {

        //Загрузка данных из Assets
        try {
            roads.clear();
            JSONArray array = new JSONArray(getStringFromAssetFile("roadLayer"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                roads.add(Road.fromJson(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void draw(GoogleMap googleMap) {

        for (Road r : roads) {
            PolylineOptions polylineOptions = new PolylineOptions();
            for (int i = 0; i < r.path.length; i += 2)
                polylineOptions.add(new LatLng(r.path[i], r.path[i + 1]));
            polylineOptions.color(getColor(r.weight)).width(getWidth(r.weight));
            googleMap.addPolyline(polylineOptions);

        }
    }


    //Настройка цвета дороги
    private int getColor(int weight) {
        if (weight == Road.WEIGHT_ROAD)
            return Color.GRAY;
        if (weight == Road.WEIGHT_SIDEWALK)
            return Color.DKGRAY;
        if (weight == Road.WEIGHT_PATHWAY)
            return Color.GRAY;
        if (weight == Road.WEIGHT_SMALL)
            return Color.LTGRAY;

        return Color.BLACK;
    }

    //Настройка ширины дороги
    private int getWidth(int weight) {

        if (weight == Road.WEIGHT_ROAD)
            return 15;
        if (weight == Road.WEIGHT_SIDEWALK)
            return 10;
        if (weight == Road.WEIGHT_PATHWAY)
            return 8;
        if (weight == Road.WEIGHT_SMALL)
            return 5;

        return 10;
    }

}