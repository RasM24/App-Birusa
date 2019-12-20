package ru.endroad.feature.auth.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.vk.sdk.VKServiceActivity
import kotlinx.coroutines.launch
import ru.endroad.feature.auth.AuthRouter
import ru.endroad.feature.auth.domain.CreateSessionUseCase
import ru.endroad.feature.auth.domain.CreateUserUseCase
import ru.endroad.feature.auth.domain.GetVkProfileUseCase
import ru.endroad.feature.auth.domain.SignInAnonymousUseCase
import ru.endroad.feature.auth.model.UserVK
import ru.endroad.feature.auth.mvi.*

class AuthViewModel(private val signInAnonymous: SignInAnonymousUseCase,
					private val getVkProfile: GetVkProfileUseCase,
					private val createUser: CreateUserUseCase,
					private val createSession: CreateSessionUseCase,
					private val firebaseDatabase: DatabaseReference,
					private val router: AuthRouter,
					firebaseAuth: FirebaseAuth) : ViewModel() {

	val state: MutableLiveData<AuthState> = MutableLiveData()

	init {
		viewModelScope.launch {
			firebaseAuth.currentUser?.let {
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
		firebaseDatabase.child("usersVK").child(user.url).setValue(user)
		signInAnonymous()?.let {
			createUser(it.user.uid, user.name)
			createSession(it.user)
			event(SuccefulAuth)
		}
	}
}