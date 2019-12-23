package ru.endroad.birusa.feature.chat.model

internal sealed class Event

internal class SubmitMessage(val text: String) : Event()