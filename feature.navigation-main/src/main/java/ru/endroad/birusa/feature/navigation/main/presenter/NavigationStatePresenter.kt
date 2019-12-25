package ru.endroad.birusa.feature.navigation.main.presenter

import ru.endroad.birusa.feature.navigation.main.model.BadgeState

interface NavigationStatePresenter {

	fun hideBottomNavigation()
	fun showBottomNavigation()
	fun addBadge(badgeState: BadgeState)
}