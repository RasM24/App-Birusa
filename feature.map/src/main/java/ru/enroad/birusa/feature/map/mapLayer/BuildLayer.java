package ru.enroad.birusa.feature.map.mapLayer;


import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.enroad.birusa.feature.map.model.Build;

public class BuildLayer extends BaseLayer {

	List<Build> builds = new ArrayList<>();

	public BuildLayer(Context context) {
		super.context = context;
		loadData();
	}

	void loadData() {

		//Загрузка данных из Assets
		try {
			builds.clear();
			JSONArray array = new JSONArray(getStringFromAssetFile("buildLayer"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				builds.add(Build.fromJson(object));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void draw(GoogleMap googleMap) {

		for (Build b : builds) {

			PolygonOptions polygoneOptions = new PolygonOptions();
			for (int i = 0; i < b.path.length; i += 2)
				polygoneOptions.add(new LatLng(b.path[i], b.path[i + 1]));
			polygoneOptions.strokeColor(b.getColorStroke(context)).strokeWidth(4).fillColor(b.getColorFill(context));
			polygoneOptions.clickable(true);
			googleMap.addPolygon(polygoneOptions).setTag(b);
		}
	}


}