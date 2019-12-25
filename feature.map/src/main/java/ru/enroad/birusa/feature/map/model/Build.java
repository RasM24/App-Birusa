package ru.enroad.birusa.feature.map.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.enroad.birusa.feature.map.R;

public class Build {

	public float[] path;
	public String name;
	public String type;

	Build() {
	}

	Build(String name, String type, float[] path) {
		this.name = name;
		this.path = path;
		this.type = type;
	}

	public static Build fromJson(final JSONObject object) throws JSONException {

		final String name = object.optString("name", "");
		final String type = object.optString("type", "");
		final float[] path = fillArrayFloat(object.getJSONArray("path"));

		return new Build(name, type, path);
	}

	private static float[] fillArrayFloat(JSONArray jArray) throws JSONException {

		float[] fData = new float[jArray.length()];
		for (int i = 0; i < fData.length; i++)
			fData[i] = Float.parseFloat(jArray.getString(i));

		return fData;
	}

	public static LatLng center(Polygon polygon) {
		float x = 0;
		float y = 0;
		for (LatLng pos : polygon.getPoints()
		) {
			x += pos.latitude;
			y += pos.longitude;
		}
		x = x / polygon.getPoints().size();
		y = y / polygon.getPoints().size();
		return new LatLng(x, y);
	}


	final static String TYPE_HOUSE = "house";
	final static String TYPE_EDUCATION = "education";
	final static String TYPE_EVENT = "event";
	final static String TYPE_OTHER = "city";

	public int getColorStroke(Context context) {
		if (TextUtils.equals(type, TYPE_HOUSE))
			return context.getResources().getColor(R.color.map_house_stroke);
		if (TextUtils.equals(type, TYPE_EDUCATION))
			return context.getResources().getColor(R.color.map_education_stroke);
		if (TextUtils.equals(type, TYPE_EVENT))
			return context.getResources().getColor(R.color.map_event_stroke);
		if (TextUtils.equals(type, TYPE_OTHER))
			return context.getResources().getColor(R.color.map_other_stroke);

		return context.getResources().getColor(R.color.map_default_stroke);
	}

	public int getColorFill(Context context) {
		if (TextUtils.equals(type, TYPE_HOUSE))
			return context.getResources().getColor(R.color.map_house_fill);
		if (TextUtils.equals(type, TYPE_EDUCATION))
			return context.getResources().getColor(R.color.map_education_fill);
		if (TextUtils.equals(type, TYPE_EVENT))
			return context.getResources().getColor(R.color.map_event_fill);
		if (TextUtils.equals(type, TYPE_OTHER))
			return context.getResources().getColor(R.color.map_other_fill);
		return context.getResources().getColor(R.color.map_default_fill);
	}
}