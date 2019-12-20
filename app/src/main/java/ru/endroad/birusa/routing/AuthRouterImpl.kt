package ru.endroad.birusa.routing

import androidx.fragment.app.FragmentManager
import endroad.birusa.FragmentChat
import kotlinx.android.synthetic.main.base_layout.*
import ru.endroad.birusa.R
import ru.endroad.feature.auth.AuthFragment
import ru.endroad.feature.auth.AuthRouter
import ru.endroad.navigation.changeRoot

//TODO придумать, как протащить fragmentManager
class AuthRouterImpl(private val fragmentManager: FragmentManager) : AuthRouter {

	override fun openMainScreen() {
		fragmentManager.changeRoot(FragmentChat(), R.id.root)
	}
}