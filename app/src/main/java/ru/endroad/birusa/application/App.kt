package ru.endroad.birusa.application

import android.app.Application
import com.vk.sdk.VKSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.endroad.birusa.feature.navigation.main.featureMenuNavigation
import ru.endroad.birusa.routing.routingModule
import ru.endroad.feature.auth.featureAuthModule

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		VKSdk.initialize(this)

		startKoin {
			androidContext(this@App)
			modules(routingModule,
					featureAuthModule,
					featureMenuNavigation)
		}
	}
}