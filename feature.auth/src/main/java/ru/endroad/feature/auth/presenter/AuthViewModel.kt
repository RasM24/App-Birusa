package ru.endroad.feature.auth.presenter

import android.app.Activity.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.*
import org.json.JSONException
import ru.endroad.feature.auth.model.User
import ru.endroad.feature.auth.model.UserVK
import ru.endroad.feature.auth.mvi.*

class AuthViewModel : ViewModel() {

	private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
	private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

	val state: MutableLiveData<AuthState> = MutableLiveData()

	init {
		mAuth.currentUser?.let {
			state.value = ProgressLoad
			::onAuthSuccess
		}
	}

	//TODO сделать нормальный биндинг
	fun event(event: Event) {
		when (event) {
			ClickOnVK                 -> TODO()
			ClickOnGoogle             -> TODO()
			ClickOnAnonymous          -> signAnonymous()
			is ActivityResultReceieve -> event.reduce()
		}
	}

	private fun ActivityResultReceieve.reduce() {
		if (requestCode == VKServiceActivity.VKServiceType.Authorization.outerCode) {
			if (resultCode == RESULT_OK) {
				val token = VKAccessToken.currentToken()
				val user = UserVK()
				user.email = token.email
				val request = VKApi.users()[VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,contacts")]
				request.executeWithListener(object : VKRequest.VKRequestListener() {
					override fun onComplete(response: VKResponse) {
						try {
							user.jsonLoad(response.json.getJSONArray("response").optJSONObject(0))
						} catch (e: JSONException) {
							e.printStackTrace()
						}
						authVK(user)
					}

					override fun onError(error: VKError) {}
					override fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {}
				})
			} else if (resultCode == RESULT_CANCELED) {
				//TODO handle Error
				//vkCallback.onError(VKObject.getRegisteredObject(data?.getLongExtra(VKSdk.EXTRA_ERROR_ID, 0) ?: 0) as VKError)
			}

		}

	}

	private fun signAnonymous() {
		state.value = ProgressLoad
		mAuth.signInAnonymously().addOnCompleteListener {
			if (it.isSuccessful) {
				writeUser(it.result.user.uid, "Anonymous")
				onAuthSuccess(it.result.user)
			}
		}
	}

	private fun authVK(user: UserVK) {
		mDatabase.child("usersVK").child(user.url).setValue(user)
		mAuth.signInAnonymously().addOnCompleteListener {
			if (it.isSuccessful) {
				writeUser(it.result.user.uid, user.name)
				onAuthSuccess(it.result.user)
			}
		}
	}

	private fun writeUser(userId: String, name: String) {
		val user = User(userId, name)
		mDatabase.child("users").child(userId).setValue(user)
	}

	private fun onAuthSuccess(user: FirebaseUser) {
		val queryEvent: Query = mDatabase.child("users").child(user.uid)
		queryEvent.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(dataSnapshot: DataSnapshot) {
				state.value = SuccessAuthorization
			}

			override fun onCancelled(databaseError: DatabaseError) {}
		})
	}
}