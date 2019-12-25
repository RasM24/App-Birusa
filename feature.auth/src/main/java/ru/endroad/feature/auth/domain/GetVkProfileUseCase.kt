package ru.endroad.feature.auth.domain

import com.vk.sdk.VKAccessToken
import com.vk.sdk.api.*
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.endroad.feature.auth.model.UserVK
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class GetVkProfileUseCase {

	companion object {
		const val RESULT_CANCELED = 0
		const val RESULT_OK = -1

		const val VK_USER_FIELDS = "id,first_name,last_name,sex,bdate,city,contacts"
	}

	suspend operator fun invoke(resultCode: Int): UserVK? =
		suspendCancellableCoroutine { continuation ->
			when (resultCode) {
				RESULT_OK       -> continuation.resumeWithProfile()
				RESULT_CANCELED -> continuation.resume(null)
				else            -> continuation.resume(null)
			}
		}

	private fun Continuation<UserVK?>.resumeWithProfile() {

		val user = UserVK().apply {
			email = VKAccessToken.currentToken().email
		}

		VKApi.users()[VKParameters.from(VKApiConst.FIELDS, VK_USER_FIELDS)]
			.executeWithListener(object : VKRequest.VKRequestListener() {
				override fun onComplete(response: VKResponse) {
					runCatching {
						user.jsonLoad(response.json.getJSONArray("response").optJSONObject(0))
						resume(user)
					}
						.onFailure { resume(null) }
				}

				override fun onError(error: VKError) = resume(null)
				override fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) = resume(null)
			})
	}

}