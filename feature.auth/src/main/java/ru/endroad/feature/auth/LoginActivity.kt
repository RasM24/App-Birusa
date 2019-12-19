package ru.endroad.feature.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.VKRequest.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.feature.auth.model.User
import ru.endroad.feature.auth.model.UserVK
import ru.endroad.feature.auth.presenter.AuthViewModel

class LoginActivity : BaseActivity(), View.OnClickListener, OnCompleteListener<AuthResult> {

	private val viewModel by viewModel<AuthViewModel>()

	private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
	private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
	private var mName = "anonim"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		setSupportActionBar(toolbar)

		//Check authVK on Activity start
		mAuth.currentUser?.let {
			showProgressDialog()
			::onAuthSuccess
		}
	}

	override fun onClick(view: View) {
		if (view.id == R.id.bt_auth_vk) {
			showProgressDialog()
			VKSdk.login(this, *sMyScope)
		}
		if (view.id == R.id.bt_auth_google) {
		}
		if (view.id == R.id.bt_auth_anon) {
			showProgressDialog()
			mAuth.signInAnonymously().addOnCompleteListener(this)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
				override fun onResult(res: VKAccessToken) {
					val user = UserVK()
					user.email = res.email
					val request = VKApi.users()[VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,contacts")]
					request.executeWithListener(object : VKRequestListener() {
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
					//                request.secure = false;
//                request.useSystemLanguage = false;
//
//startApiCall(request);
				}

				override fun onError(error: VKError) {}
			})) {
			super.onActivityResult(requestCode, resultCode, data)
		}
	}

	private fun authVK(user: UserVK) {
		mName = user.name
		mDatabase.child("usersVK").child(user.url).setValue(user)
		mAuth.signInAnonymously().addOnCompleteListener(this)
	}

	override fun onComplete(task: Task<AuthResult>) {
		if (task.isSuccessful) {
			writeUser(task.result.user.uid, mName)
			onAuthSuccess(task.result.user)
		}
	}

	private fun writeUser(userId: String, name: String) {
		val user = User(userId, name)
		mDatabase.child("users").child(userId).setValue(user)
	}

	private fun onAuthSuccess(user: FirebaseUser?) {
		val queryEvent: Query = mDatabase.child("users").child(user!!.uid)
		queryEvent.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(dataSnapshot: DataSnapshot) {
				hideProgressDialog()
				//startActivity(new Intent(LoginActivity.this, MainActivity.class));
				Toast.makeText(this@LoginActivity, "Авторизация успешна", Toast.LENGTH_SHORT).show()
				//finish();
			}

			override fun onCancelled(databaseError: DatabaseError) {}
		})
	}

	companion object {
		private val sMyScope = arrayOf(
			VKScope.FRIENDS,
			VKScope.EMAIL
		)
	}
}