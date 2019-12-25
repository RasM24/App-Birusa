package ru.enroad.birusa.feature.map.mapLayer

import android.content.Context
import android.text.TextUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.enroad.birusa.feature.map.R
import ru.enroad.birusa.feature.map.model.Event
import java.util.*

class EventLayer(private val context: Context) : ValueEventListener {

	private val events: MutableList<Event> = ArrayList()
	private val markers: MutableList<Marker> = ArrayList()

	var listener: EventChangeListener? = null

	init {
		val queryEventTwit = FirebaseDatabase.getInstance().reference.child(DB_EVENT).orderByChild(
			"date").startAt(time - Event.TIME_ACTUAL.toDouble())
		queryEventTwit.addValueEventListener(this)
	}

	fun draw(map: GoogleMap?, uid: String) {
		map?: return

		markers.forEach { it.remove() }
		markers.clear()

		events.forEach { event ->
			val markerOptions = MarkerOptions()
				.position(event.posGMS)
				.title(event.text)
				.icon(BitmapDescriptorFactory.fromResource(getIcon(event.icon)))

			if (event.uid == uid) markerOptions.snippet("Нажмите чтобы удалить")

			map.addMarker(markerOptions)
				.run {
					tag = event
					markers.add(this)
				}
		}

	}

	interface EventChangeListener {
		fun onEventChange()
	}

	fun setEventChangeListener(listener: EventChangeListener) {
		this.listener = listener
	}

	private fun markersContain(e: Event): Boolean =
		markers.map(Marker::getTag).contains(e)

	override fun onDataChange(dataSnapshot: DataSnapshot) {
		events.clear()
		dataSnapshot.children.forEach { snapshot ->
			snapshot.getValue(Event::class.java)?.let {
				events.add(it)
				it.postKey = snapshot.key
			}
		}
		listener?.onEventChange()
	}

	override fun onCancelled(databaseError: DatabaseError) {}
	private fun getIcon(id: String): Int {
		val array = context.resources.getStringArray(R.array.type_points)
		for (i in array.indices) if (TextUtils.equals(id, array[i])) return Event.drawable[i]
		return R.drawable.event_flag
	}

	fun deleteEvent(event: Event): Boolean {
		for (e in events) if (event.date == e.date) {
			events.remove(e)
			return true
		}
		return false
	}

	companion object {
		const val DB_EVENT = "events"
		val time: Long
			get() = System.currentTimeMillis() / 1000L
	}
}