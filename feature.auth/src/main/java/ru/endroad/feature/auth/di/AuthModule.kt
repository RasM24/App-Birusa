package ru.endroad.feature.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.single
import ru.endroad.feature.auth.domain.CreateSessionUseCase
import ru.endroad.feature.auth.domain.CreateUserUseCase
import ru.endroad.feature.auth.domain.GetVkProfileUseCase
import ru.endroad.feature.auth.domain.SignInAnonymousUseCase
import ru.endroad.feature.auth.presenter.AuthViewModel

val featureAuthModule = module {
	single { FirebaseDatabase.getInstance().reference }
	single { FirebaseAuth.getInstance() }

	single<CreateUserUseCase>()
	single<SignInAnonymousUseCase>()
	single<GetVkProfileUseCase>()
	single<CreateSessionUseCase>()

	viewModel { AuthViewModel(get(), get(), get(), get(), get(), get()) }
}