package ru.enroad.birusa.feature.map.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Road {

    //автодорога от сцены до хоз.зданий
    public final static int WEIGHT_ROAD = 0;
    //деревянные тротуары
    public final static int WEIGHT_SIDEWALK = 1;
    public final static int WEIGHT_PATHWAY = 2;
    public final static int WEIGHT_SMALL = 3;

    public float[] path;

    public int weight;

    public String name;

    Road(String name, int weight, float[] path) {
        this.name = name;
        this.weight = weight;
        this.path = path;
    }

    public static Road fromJson(final JSONObject object) throws JSONException {

        final String name = object.optString("text", "");
        final int weight = object.optInt("weight", 3);
        final float[] path = fillArrayFloat(object.getJSONArray("path"));

        return new Road(name, weight, path);
    }

    private static float[] fillArrayFloat(JSONArray jArray) throws JSONException {

        float[] fData = new float[jArray.length()];
        for (int i = 0; i < fData.length; i++)
            fData[i] = Float.parseFloat(jArray.getString(i));

        return fData;
    }

}
