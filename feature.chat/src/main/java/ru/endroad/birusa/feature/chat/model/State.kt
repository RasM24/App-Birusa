package ru.endroad.birusa.feature.chat.model

sealed class State

class InputText(val enabled: Boolean, val error: String? = null) : State()