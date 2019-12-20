package ru.endroad.feature.auth.domain

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SignInAnonymousUseCase(private val firebaseAuth: FirebaseAuth) {

	suspend operator fun invoke(): AuthResult? =
		suspendCancellableCoroutine { continuation ->
			firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
				if (task.isSuccessful)
					continuation.resume(task.result)
				else
					continuation.resume(null)
			}
		}
}