package ru.endroad.feature.auth.domain

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class CreateSessionUseCase(private val firebaseDatabase: DatabaseReference) {

	suspend operator fun invoke(user: FirebaseUser): Unit =
		suspendCancellableCoroutine { continuation ->
			firebaseDatabase.child("users").child(user.uid)
				.addListenerForSingleValueEvent(object : ValueEventListener {
					override fun onDataChange(dataSnapshot: DataSnapshot) {
						continuation.resume(Unit)
					}

					override fun onCancelled(databaseError: DatabaseError) {}
				})
		}
}