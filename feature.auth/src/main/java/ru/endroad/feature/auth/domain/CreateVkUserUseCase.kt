package ru.endroad.feature.auth.domain

import com.google.firebase.database.DatabaseReference
import ru.endroad.feature.auth.model.UserVK

class CreateVkUserUseCase(private val firebaseDatabase: DatabaseReference) {

	operator fun invoke(user: UserVK) {
		firebaseDatabase.child("usersVK").child(user.url).setValue(user)
	}
}