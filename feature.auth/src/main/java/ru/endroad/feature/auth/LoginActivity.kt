package ru.endroad.feature.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.mvi.storage.subscribe
import ru.endroad.feature.auth.mvi.*
import ru.endroad.feature.auth.presenter.AuthViewModel

class LoginActivity : BaseActivity() {

	private val viewModel by viewModel<AuthViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		setSupportActionBar(toolbar)

		setupViewModel()
		setupViewComponent()
	}

	private fun setupViewModel() {
		viewModel.state.subscribe(this)
		{
			when (it) {
				ProgressLoad         -> showProgressDialog()
				SuccessAuthorization -> authSuccess()
			}
		}
	}

	private fun setupViewComponent() {
		bt_auth_vk.setOnClickListener {
			showProgressDialog()
			VKSdk.login(this, *sMyScope)
		}

		bt_auth_google.setOnClickListener { viewModel.event(ClickOnGoogle) }
		bt_auth_anon.setOnClickListener { viewModel.event(ClickOnAnonymous) }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		viewModel.event(ActivityResultReceieve(requestCode, resultCode, data))
		super.onActivityResult(requestCode, resultCode, data)
	}

	private fun authSuccess() {
		hideProgressDialog()
		//startActivity(new Intent(LoginActivity.this, MainActivity.class));
		Toast.makeText(this@LoginActivity, "Авторизация успешна", Toast.LENGTH_SHORT).show()
		//finish();
	}

	companion object {
		private val sMyScope = arrayOf(
			VKScope.FRIENDS,
			VKScope.EMAIL
		)
	}
}