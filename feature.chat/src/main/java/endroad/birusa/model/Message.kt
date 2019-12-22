package endroad.birusa.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
class Message @JvmOverloads constructor(text: String? = null, uid: String? = null, date: Long = 0) {

	var text: String? = text
		private set
	var uid: String? = uid
		private set
	var date: Long = date
		private set

	@Exclude
	fun toMap(): Map<String, Any?> {
		val result = HashMap<String, Any?>()
		result["text"] = text
		result["uid"] = uid
		result["date"] = date
		return result
	}

	// its need to be in milisecond
	val dateText: String
		get() {
			val a = date.toInt() / 86400
			val b = (System.currentTimeMillis() / 1000L).toInt() / 86400
			var vv = ""
			val dv = java.lang.Long.valueOf(date) * 1000 // its need to be in milisecond
			val df = Date(dv)
			val mouth = arrayOf("-янв",
								"-фев",
								"-март",
								"-апр",
								"-май",
								"-июнь",
								"-июль",
								"-авг",
								"-сен",
								"-окт",
								"-ноя",
								"-дек")
			if (a != b) vv = SimpleDateFormat("dd" + mouth[df.month] + "\n").format(df)
			vv += SimpleDateFormat("HH:mm").format(df)
			return vv
		}
}