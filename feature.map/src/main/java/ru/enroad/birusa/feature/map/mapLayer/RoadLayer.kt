package ru.enroad.birusa.feature.map.mapLayer

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONArray
import org.json.JSONException
import ru.endroad.arena.data.openString
import ru.enroad.birusa.feature.map.model.Road
import java.util.*

class RoadLayer(context: Context) {
	var roads: MutableList<Road> = ArrayList()

	init {
		try {
			roads.clear()
			val array = JSONArray(context.assets.openString("roadLayer"))
			for (i in 0 until array.length()) {
				val `object` = array.getJSONObject(i)
				roads.add(Road.fromJson(`object`))
			}
		} catch (e: JSONException) {
			e.printStackTrace()
		}
	}

	fun draw(googleMap: GoogleMap) {
		for (r in roads) {
			val polylineOptions = PolylineOptions()
			var i = 0
			while (i < r.path.size) {
				polylineOptions.add(LatLng(r.path[i].toDouble(), r.path[i + 1].toDouble()))
				i += 2
			}
			polylineOptions.color(getColor(r.weight)).width(getWidth(r.weight).toFloat())
			googleMap.addPolyline(polylineOptions)
		}
	}

	//Настройка цвета дороги
	private fun getColor(weight: Int): Int {
		if (weight == Road.WEIGHT_ROAD) return Color.GRAY
		if (weight == Road.WEIGHT_SIDEWALK) return Color.DKGRAY
		if (weight == Road.WEIGHT_PATHWAY) return Color.GRAY
		return if (weight == Road.WEIGHT_SMALL) Color.LTGRAY else Color.BLACK
	}

	//Настройка ширины дороги
	private fun getWidth(weight: Int): Int {
		if (weight == Road.WEIGHT_ROAD) return 15
		if (weight == Road.WEIGHT_SIDEWALK) return 10
		if (weight == Road.WEIGHT_PATHWAY) return 8
		return if (weight == Road.WEIGHT_SMALL) 5 else 10
	}
}