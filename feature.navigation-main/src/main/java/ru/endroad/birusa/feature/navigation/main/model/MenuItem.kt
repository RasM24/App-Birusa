package ru.endroad.birusa.feature.navigation.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

data class MenuItem(@IdRes val itemId: Int,
					@StringRes val name: Int,
					@DrawableRes val icon: Int,
					val position: Int,
					val badgeState: BadgeState,
					val fragment: () -> Fragment)

sealed class BadgeState {
	object None : BadgeState()
	object Show : BadgeState()
	class Number(val number: Int) : BadgeState()
}