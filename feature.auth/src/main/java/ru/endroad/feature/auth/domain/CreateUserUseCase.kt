package ru.endroad.feature.auth.domain

import com.google.firebase.database.DatabaseReference
import ru.endroad.feature.auth.model.User

class CreateUserUseCase(private val firebaseDatabase: DatabaseReference) {

	//TODO вынести в dataSource
	operator fun invoke(userId: String, name: String) {
		val user = User(userId, name)
		firebaseDatabase.child("users").child(userId).setValue(user)
	}
}