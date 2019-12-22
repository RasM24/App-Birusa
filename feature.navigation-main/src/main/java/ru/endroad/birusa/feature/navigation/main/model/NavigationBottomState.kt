package ru.endroad.birusa.feature.navigation.main.model

import androidx.annotation.IdRes

sealed class NavigationBottomPartialState

class SetupMenuNavigation(val items: List<MenuItem>) : NavigationBottomPartialState()
class ChangeVisible(val isShow: Boolean) : NavigationBottomPartialState()
class ChangeBadge(@IdRes val itemId: Int, val badgeState: BadgeState) : NavigationBottomPartialState()