package ru.endroad.birusa.application

import ru.endroad.arena.viewlayer.activity.BaseActivity
import ru.endroad.arena.viewlayer.extension.startScreen
import ru.endroad.birusa.R
import ru.endroad.feature.auth.LoginActivity

class SingleActivity : BaseActivity() {

	override val layout = R.layout.activity_main

	override val theme = R.style.AppTheme

	override fun onFirstCreate() {
		startScreen(LoginActivity::class.java)
	}
}