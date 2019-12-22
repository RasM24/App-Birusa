package ru.endroad.birusa.routing

import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import ru.endroad.birusa.feature.navigation.main.MenuNavigationRouter
import ru.endroad.feature.auth.AuthRouter

val routingModule = module {
	singleBy<AuthRouter, AuthRouterImpl>()
	singleBy<MenuNavigationRouter, MenuNavigationRouter>()
}