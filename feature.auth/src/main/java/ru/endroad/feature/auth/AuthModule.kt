package ru.endroad.feature.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module
import org.koin.experimental.builder.single
import ru.endroad.arena.viewmodellayer.viewModel
import ru.endroad.feature.auth.domain.*
import ru.endroad.feature.auth.presenter.AuthViewModel

val featureAuthModule = module {
	single { FirebaseDatabase.getInstance().reference }
	single { FirebaseAuth.getInstance() }

	single<CreateUserUseCase>()
	single<GetUserUseCase>()
	single<SignInAnonymousUseCase>()
	single<GetVkProfileUseCase>()
	single<CreateSessionUseCase>()
	single<CreateVkUserUseCase>()

	viewModel<AuthViewModel>()
}