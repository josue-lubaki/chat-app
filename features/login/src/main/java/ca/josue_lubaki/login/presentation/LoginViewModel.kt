package ca.josue_lubaki.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineDispatcher

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

class LoginViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnSignIn -> event.reduce()
        }
    }

    private fun LoginEvent.OnSignIn.reduce() {
        _state.value = LoginState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _state.value = LoginState.Success
                        } else {
                            _state.value = LoginState.Error(Exception(task.exception))
                        }
                    }
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = LoginState.Error(e)
        }
    }
}