package ru.endroad.birusa.feature.navigation.main

import androidx.fragment.app.Fragment

interface MenuNavigationRouter {

	fun openMenuItem(fragment: () -> Fragment)

}