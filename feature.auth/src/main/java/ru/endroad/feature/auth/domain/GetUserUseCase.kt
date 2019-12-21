package ru.endroad.feature.auth.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class GetUserUseCase(private val firebaseAuth: FirebaseAuth) {

	operator fun invoke(): FirebaseUser? =
		firebaseAuth.currentUser
}