package ru.endroad.birusa.feature.chat.view

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.mvi.storage.subscribe
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.birusa.feature.chat.R
import ru.endroad.birusa.feature.chat.view.ViewHolders.*
import ru.endroad.birusa.feature.chat.model.InputText
import ru.endroad.birusa.feature.chat.model.Message
import ru.endroad.birusa.feature.chat.model.SubmitMessage
import ru.endroad.birusa.feature.chat.presenter.ChatViewModel

//TODO Нужен объемный рефактор, а может и даже написание с 0
class ChatFragment : BaseFragment() {

	override val layout = R.layout.fragment_chat

	private val viewModel by viewModel<ChatViewModel>()

	private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
	private val query = databaseReference.child(DB_CHAT).limitToLast(
		MAX_CHAT_MESSAGE)

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
		view_submit.setOnClickListener { viewModel.reduce(SubmitMessage(view_edit_text.text.toString())) }

		(list.layoutManager as? LinearLayoutManager)?.stackFromEnd = true

		list.adapter = mAdapter
	}

	override fun setupViewModel() {
		viewModel.state.subscribe(this) { state ->
			when (state) {
				is InputText -> renderInputLayout(state)
			}
		}
	}

	private fun renderInputLayout(state: InputText) {
		state.enabled.let { view_edit_text.isVisible = it }
		state.error?.let { view_edit_text.error = it }
	}

	val uid: String
		get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""

	companion object {
		const val DB_CHAT = "chat"
		const val MAX_CHAT_MESSAGE = 100
	}
}