package ca.josue_lubaki.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * created by Josue Lubaki
 * date : 2023-10-26
 * version : 1.0.0
 */

class RegisterViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnRegister -> event.reduce()
        }
    }

    private fun RegisterEvent.OnRegister.reduce() {
        _state.value = RegisterState.Loading
        try {
            viewModelScope.launch(dispatcher) {

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                          // Sign in success, update UI with the signed-in user's information
                            val user = firebaseAuth.currentUser
                            val userId = user!!.uid

                            val databaseReference = firebaseDatabase.getReference("users")

                            val hashMap : HashMap<String, String> = HashMap()
                            hashMap["userId"] = userId
                            hashMap["username"] = username
                            hashMap["email"] = email
                            hashMap["profileImage"] = ""

                            databaseReference
                                .child(userId)
                                .setValue(hashMap)
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        _state.value = RegisterState.Success
                                    } else {
                                        _state.value = RegisterState.Error(it.exception as Exception)
                                    }
                                }
                        } else {
                            // If sign in fails, display a message to the user.
                            _state.value = RegisterState.Error(task.exception as Exception)
                        }
                    }
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = RegisterState.Error(e)
        }
    }
}