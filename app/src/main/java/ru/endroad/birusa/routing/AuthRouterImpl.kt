package ru.endroad.birusa.routing

import androidx.fragment.app.FragmentManager
import ru.endroad.birusa.R
import ru.endroad.birusa.feature.navigation.main.view.MainFragment
import ru.endroad.feature.auth.AuthRouter
import ru.endroad.navigation.changeRoot

class AuthRouterImpl(private val fragmentManager: FragmentManager) : AuthRouter {

	override fun openMainScreen() {
		fragmentManager.changeRoot(MainFragment(), R.id.root)
	}
}