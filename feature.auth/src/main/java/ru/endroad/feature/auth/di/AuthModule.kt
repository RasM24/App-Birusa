package ru.endroad.feature.auth.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.endroad.feature.auth.presenter.AuthViewModel

val featureAuthModule = module {



	viewModel { AuthViewModel() }
}