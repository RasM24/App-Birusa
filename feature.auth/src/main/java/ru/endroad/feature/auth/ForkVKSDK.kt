package ru.endroad.feature.auth

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.VKServiceActivity.*
import java.util.*

private const val KEY_TYPE = "arg1"
private const val KEY_SCOPE_LIST = "arg2"
private const val KEY_SDK_CUSTOM_INITIALIZE = "arg4"

fun Fragment.startVkLoginActivity(vararg scope: String) {

	val intent = requireContext().createIntent.apply {
		putStringArrayListExtra(KEY_SCOPE_LIST, preparingScopeList(*scope))
	}

	startActivityForResult(intent, VKServiceType.Authorization.outerCode)
}

private val Context.createIntent
	get() = Intent(this, VKServiceActivity::class.java).apply {
		putExtra(KEY_TYPE, VKServiceType.Authorization.name)
		putExtra(KEY_SDK_CUSTOM_INITIALIZE, VKSdk.isCustomInitialize())
	}

private fun preparingScopeList(vararg scope: String): ArrayList<String> =
	arrayListOf(*scope)
		.apply { if (!contains(VKScope.OFFLINE)) add(VKScope.OFFLINE) }
