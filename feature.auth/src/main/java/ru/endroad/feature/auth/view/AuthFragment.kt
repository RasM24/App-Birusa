package ru.endroad.feature.auth.view

import android.app.ProgressDialog
import android.content.Intent
import com.vk.sdk.VKScope
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.endroad.arena.mvi.storage.subscribe
import ru.endroad.arena.viewlayer.fragment.BaseFragment
import ru.endroad.arena.viewlayer.vksdk.startVkLoginActivity
import ru.endroad.feature.auth.R
import ru.endroad.feature.auth.model.*
import ru.endroad.feature.auth.presenter.AuthViewModel

class AuthFragment : BaseFragment() {

	override val layout = R.layout.activity_login

	private val viewModel by viewModel<AuthViewModel>()

	override fun setupViewModel() {
		viewModel.state.subscribe(this)
		{
			when (it) {
				ProgressLoad -> showProgressDialog()
				SuccessAuth  -> hideProgressDialog()
			}
		}
	}

	override fun setupViewComponents() {
		bt_auth_vk.setOnClickListener { startVkLoginActivity(VKScope.FRIENDS, VKScope.EMAIL) }
		bt_auth_google.setOnClickListener { viewModel.event(ClickOnGoogle) }
		bt_auth_anon.setOnClickListener { viewModel.event(ClickOnAnonymous) }
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		viewModel.event(ActivityResultReceive(requestCode, resultCode, data))
		super.onActivityResult(requestCode, resultCode, data)
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
	private fun showProgressDialog() = mProgressDialog.show()

	@Deprecated("Переделать дизайн и выпилить прогресс бар")
	private fun hideProgressDialog() = mProgressDialog.run { if (isShowing) dismiss() }
}