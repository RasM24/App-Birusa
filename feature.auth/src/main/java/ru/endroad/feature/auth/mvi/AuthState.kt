package ru.endroad.feature.auth.mvi

sealed class AuthState

object ProgressLoad : AuthState()
object SuccessAuthorization : AuthState()