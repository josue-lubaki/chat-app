package ca.josue_lubaki.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.common.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

class ContactViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth : FirebaseAuth,
    private val firebaseDatabase : FirebaseDatabase
) : ViewModel() {

    private val _state = MutableStateFlow<ContactState>(ContactState.Idle)
    val state: StateFlow<ContactState> = _state.asStateFlow()

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.OnLoadData -> getAllData()
        }
    }

    private fun getAllData() {
        _state.value = ContactState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference("users")

                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users: MutableList<User> = ArrayList()
                        var me : User? = null
                        for (dataSnapshot in snapshot.children) {
                            val user : User? = dataSnapshot.getValue(User::class.java)
                            if (user!!.userId != firebaseUser?.uid) users.add(user)
                            else me = user
                        }
                        _state.value = ContactState.Success(
                            data = users,
                            me = me
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = ContactState.Error(error.toException())
                    }
                })
            }
        } catch (e: Exception) {
            // handle errors
            _state.value = ContactState.Error(e)
        }
    }
}