package ru.endroad.feature.auth.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.sdk.VKServiceActivity
import kotlinx.coroutines.launch
import ru.endroad.feature.auth.AuthRouter
import ru.endroad.feature.auth.domain.*
import ru.endroad.feature.auth.model.UserVK
import ru.endroad.feature.auth.mvi.*

class AuthViewModel(private val signInAnonymous: SignInAnonymousUseCase,
					private val getVkProfile: GetVkProfileUseCase,
					private val createUser: CreateUserUseCase,
					private val createSession: CreateSessionUseCase,
					private val getUser: GetUserUseCase,
					private val createVkUser: CreateVkUserUseCase,
					private val router: AuthRouter) : ViewModel() {

	val state: MutableLiveData<AuthState> = MutableLiveData()

	init {
		viewModelScope.launch {
			getUser()?.let {
				state.value = ProgressLoad
				createSession(it)
				event(SuccefulAuth)
			}
		}
	}

	//TODO сделать нормальный биндинг
	fun event(event: Event) {
		when (event) {
			ClickOnVK                -> TODO()
			ClickOnGoogle            -> TODO()
			ClickOnAnonymous         -> viewModelScope.launch { signAnonymous() }
			is ActivityResultReceive -> viewModelScope.launch { event.reduce() }

			SuccefulAuth             -> router.openMainScreen()
		}
	}

	private suspend fun ActivityResultReceive.reduce() {
		if (requestCode == VKServiceActivity.VKServiceType.Authorization.outerCode)
			getVkProfile(requestCode)?.let { authVK(it) }
	}

	private suspend fun signAnonymous() {
		state.value = ProgressLoad
		signInAnonymous()?.let {
			createUser(it.user.uid, "Anonymous")
			createSession(it.user)
			event(SuccefulAuth)
		}
	}

	private suspend fun authVK(user: UserVK) {
		createVkUser(user)
		signInAnonymous()?.let {
			createUser(it.user.uid, user.name)
			createSession(it.user)
			event(SuccefulAuth)
		}
	}
}