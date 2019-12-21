package ru.endroad.feature.auth.model

sealed class AuthState

object ProgressLoad : AuthState()
object SuccessAuth : AuthState()