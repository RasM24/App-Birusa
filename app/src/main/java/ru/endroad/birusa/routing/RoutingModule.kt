package ru.endroad.birusa.routing

import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import ru.endroad.birusa.routing.AuthRouterImpl
import ru.endroad.feature.auth.AuthRouter

val routingModule = module {
	singleBy<AuthRouter, AuthRouterImpl>()
}