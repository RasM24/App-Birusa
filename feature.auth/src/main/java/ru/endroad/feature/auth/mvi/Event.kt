package ru.endroad.feature.auth.mvi

import android.content.Intent

sealed class Event

object ClickOnVK : Event()
object ClickOnGoogle : Event()
object ClickOnAnonymous : Event()

class ActivityResultReceive(val requestCode: Int, val resultCode: Int, val data: Intent?) : Event()