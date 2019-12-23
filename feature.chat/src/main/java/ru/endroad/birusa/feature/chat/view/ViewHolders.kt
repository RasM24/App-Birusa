package ru.endroad.birusa.feature.chat.view

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.endroad.birusa.component.session.model.User
import ru.endroad.birusa.feature.chat.R
import ru.endroad.birusa.feature.chat.model.Message

class ViewHolders {
	class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val mTextField = itemView.findViewById<TextView>(R.id.view_text)
		private val mDateField = itemView.findViewById<TextView>(R.id.view_date)
		private val mDateFieldMyself = itemView.findViewById<TextView>(R.id.view_date_myself)
		private val mAuthorFieldText = itemView.findViewById<TextView>(R.id.view_author_text)
		//return  mAuthorFieldText.getText();
		var author: String? = null
			private set
		private val mCardView = itemView.findViewById<CardView>(R.id.view_card)

		fun bindData(message: Message, onClickListener: View.OnClickListener?) {
			if (FirebaseAuth.getInstance().currentUser?.uid == message.uid) {
				mDateField.visibility = View.GONE
				mDateFieldMyself.visibility = View.VISIBLE
				mDateFieldMyself.text = message.dateText
				setAvatar(message.uid)
			} else {
				mDateFieldMyself.visibility = View.GONE
				mDateField.visibility = View.VISIBLE
				mDateField.text = message.dateText
				setAvatar(message.uid)
				mCardView.setOnClickListener(onClickListener)
			}
			mTextField.text = message.text
		}

		private fun setAvatar(userId: String?) {
			FirebaseDatabase.getInstance().reference.child("users").child(userId ?: "").addListenerForSingleValueEvent(
				object : ValueEventListener {
					override fun onDataChange(dataSnapshot: DataSnapshot) { // Get user value
						val user = dataSnapshot.getValue(User::class.java) ?: return
						mAuthorFieldText.text = user.nick
						author = user.nick
					}

					override fun onCancelled(databaseError: DatabaseError) {}
				})
		}

	}
}