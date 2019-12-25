package ru.enroad.birusa.feature.map.mapLayer

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import org.json.JSONArray
import ru.endroad.arena.data.openString
import ru.enroad.birusa.feature.map.model.Build
import java.util.*

class BuildLayer(private val context: Context) {

	private var builds: MutableList<Build> = ArrayList()

	init {
		runCatching {
			builds.clear()
			val array = JSONArray(context.assets.openString("buildLayer"))
			for (i in 0 until array.length()) {
				val `object` = array.getJSONObject(i)
				builds.add(Build.fromJson(`object`))
			}
		}
	}

	fun draw(googleMap: GoogleMap) {
		for (b in builds) {
			val polygoneOptions = PolygonOptions()
			var i = 0
			while (i < b.path.size) {
				polygoneOptions.add(LatLng(b.path[i].toDouble(), b.path[i + 1].toDouble()))
				i += 2
			}
			polygoneOptions.strokeColor(b.getColorStroke(context)).strokeWidth(4f).fillColor(b.getColorFill(context))
			polygoneOptions.clickable(true)
			googleMap.addPolygon(polygoneOptions).tag = b
		}
	}
}