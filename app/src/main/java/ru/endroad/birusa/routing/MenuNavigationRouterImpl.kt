package ru.endroad.birusa.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.endroad.birusa.R
import ru.endroad.birusa.feature.navigation.main.MenuNavigationRouter
import ru.endroad.navigation.changeRoot

class MenuNavigationRouterImpl(private val fragmentManager: FragmentManager) : MenuNavigationRouter {

	override fun openMenuItem(fragment: () -> Fragment) {
		fragmentManager.changeRoot(fragment(), R.id.root)
	}
}