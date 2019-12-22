package ru.endroad.birusa.feature.chat

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.birusa.feature.chat.ViewHolders.*
import ru.endroad.birusa.feature.chat.model.Message
import java.util.*

class FragmentChat : BaseFragment() {

	override val layout = R.layout.fragment_chat

	private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
	private val query = databaseReference.child(DB_CHAT).limitToLast(MAX_CHAT_MESSAGE)

	private val mAdapter = object : FirebaseRecyclerAdapter<Message, MessageHolder>(
		Message::class.java,
		R.layout.item_message,
		MessageHolder::class.java,
		query) {
		public override fun populateViewHolder(vh: MessageHolder, message: Message, position: Int) {
			vh.bindData(message, View.OnClickListener { view ->
				if (view.id == R.id.view_card
					&& uid != message.uid
					&& view_edit_text.text.toString().isEmpty()) {
					view_edit_text.setText("${vh.author}, ")
					view_edit_text.setSelection(view_edit_text.text.length)
				}
			})
		}

		override fun onDataChanged() {
			list.adapter?.itemCount?.let { list.smoothScrollToPosition(it - 1) }
		}
	}

	override fun setupViewComponents() {
		view_submit.setOnClickListener { submitMessage() }

		(list.layoutManager as? LinearLayoutManager)?.stackFromEnd = true

		list.adapter = mAdapter
	}

	private fun submitMessage() {
		val text = view_edit_text.text.toString()
		if (text.isEmpty()) {
			view_edit_text.error = "Введите текст"
			return
		}
		setEditingEnabled(false)
		//writeNewPost(userId, text);
//writeNewPost(text);
		val userId = uid
		writeNewPost(userId, text)
	}

	private fun setEditingEnabled(enabled: Boolean) { //mTextField.setEnabled(enabled);
		view_submit.isEnabled = enabled
	}

	// [START write_fan_out]
	private fun writeNewPost(userId: String, text: String) {
		val key = databaseReference.child(DB_CHAT).push().key
		val message = Message(text,
							  userId,
							  System.currentTimeMillis() / 1000L)
		val values = message.toMap()
		val childUpdates: MutableMap<String, Any> = HashMap()
		childUpdates["$DB_CHAT/$key"] = values
		databaseReference.updateChildren(childUpdates)
		view_edit_text.setText("")
		setEditingEnabled(true)
	}

	val uid: String
		get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

	companion object {
		const val DB_CHAT = "chat"
		const val MAX_CHAT_MESSAGE = 100
	}
}