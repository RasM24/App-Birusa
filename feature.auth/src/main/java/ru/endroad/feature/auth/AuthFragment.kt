package ru.endroad.feature.auth

import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import com.vk.sdk.VKScope
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.mvi.storage.subscribe
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.feature.auth.mvi.*
import ru.endroad.feature.auth.presenter.AuthViewModel
import ru.endroad.navigation.changeRoot

class AuthFragment : BaseFragment() {

	companion object {
		private val sMyScope = arrayOf(
			VKScope.FRIENDS,
			VKScope.EMAIL
		)
	}

	override val layout = R.layout.activity_login

	private val viewModel by viewModel<AuthViewModel>()

	override fun setupViewModel() {
		viewModel.state.subscribe(this)
		{
			when (it) {
				ProgressLoad         -> showProgressDialog()
			}
		}
	}

	override fun setupViewComponents() {
		bt_auth_vk.setOnClickListener {
			showProgressDialog()
			startVkLoginActivity(*sMyScope)
		}

		bt_auth_google.setOnClickListener { viewModel.event(ClickOnGoogle) }
		bt_auth_anon.setOnClickListener { viewModel.event(ClickOnAnonymous) }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		viewModel.event(ActivityResultReceive(requestCode, resultCode, data))
		super.onActivityResult(requestCode, resultCode, data)
	}

	private fun authSuccess() {
		hideProgressDialog()
		//startActivity(new Intent(LoginActivity.this, MainActivity.class));
		Toast.makeText(requireContext(), "Авторизация успешна", Toast.LENGTH_SHORT).show()
		//finish();
	}

	@Deprecated("Переделать дизайн и выпилить прогресс бар")
	private val mProgressDialog by lazy {
		ProgressDialog(requireContext())
			.apply {
				setCancelable(false)
				setMessage("Loading...")
			}
	}

	@Deprecated("Переделать дизайн и выпилить прогресс бар")
	private fun showProgressDialog() {
		mProgressDialog.show()
	}

	@Deprecated("Переделать дизайн и выпилить прогресс бар")
	private fun hideProgressDialog() {
		mProgressDialog.run {
			if (isShowing) dismiss()
		}
	}
}