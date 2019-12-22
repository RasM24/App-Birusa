package ru.endroad.birusa.feature.navigation.main.model

import androidx.annotation.IdRes

sealed class BadgeState {
	class None(@IdRes val itemId: Int) : BadgeState()
	class Show(@IdRes val itemId: Int) : BadgeState()
	class Number(@IdRes val itemId: Int, val number: Int) : BadgeState()
}