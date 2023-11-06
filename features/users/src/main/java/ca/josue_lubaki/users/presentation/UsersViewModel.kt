package ca.josue_lubaki.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.users.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineDispatcher

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

class UsersViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth : FirebaseAuth,
    private val firebaseDatabase : FirebaseDatabase
) : ViewModel() {

    private val _state = MutableStateFlow<UsersState>(UsersState.Idle)
    val state: StateFlow<UsersState> = _state.asStateFlow()

    fun onEvent(event: UsersEvent) {
        when (event) {
            is UsersEvent.OnLoadData -> getAllData()
        }
    }

    private fun getAllData() {
        _state.value = UsersState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference("users")

                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users: MutableList<User> = ArrayList()
                        for (dataSnapshot in snapshot.children) {
                            val user : User? = dataSnapshot.getValue(User::class.java)
                            if (user!!.userId != firebaseUser?.uid) {
                                users.add(user)
                            }
                        }
                        _state.value = UsersState.Success(users)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = UsersState.Error(error.toException())
                    }
                })
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = UsersState.Error(e)
        }
    }
}