package ru.endroad.birusa.feature.chat.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ru.endroad.birusa.feature.chat.ChatFragment
import ru.endroad.birusa.feature.chat.model.*
import java.util.*

class ChatViewModel(private val databaseReference: DatabaseReference) : ViewModel() {

	val state = MutableLiveData<State>()

	internal fun reduce(event: Event) {
		when (event) {
			is SubmitMessage -> submitMessage(event)
		}
	}

	private fun submitMessage(event: SubmitMessage) {
		if (event.text.isEmpty()) {
			state.value = InputText(true, "Введите текст")
			return
		}
		state.value = InputText(false)
		val userId = uid

		val key = databaseReference.child(ChatFragment.DB_CHAT).push().key
		val message = Message(event.text,
							  userId,
							  System.currentTimeMillis() / 1000L)
		val values = message.toMap()
		val childUpdates: MutableMap<String, Any> = HashMap()
		childUpdates["${ChatFragment.DB_CHAT}/$key"] = values
		databaseReference.updateChildren(childUpdates)
		//view_edit_text.setText("")
		state.value = InputText(true)
	}

	private val uid: String
		get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
}