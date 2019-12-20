package ru.endroad.birusa.application

import ru.endroad.arena.viewlayer.activity.BaseActivity
import ru.endroad.birusa.R
import ru.endroad.feature.auth.AuthFragment
import ru.endroad.navigation.changeRoot

class SingleActivity : BaseActivity() {

	override val layout = R.layout.base_layout

	override val theme = R.style.AppTheme

	override fun onFirstCreate() {
		supportFragmentManager.changeRoot(AuthFragment(), R.id.root)
	}
}