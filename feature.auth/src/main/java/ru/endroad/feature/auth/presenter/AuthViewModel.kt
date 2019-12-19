package ru.endroad.feature.auth.presenter

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {

	private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
	private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

}