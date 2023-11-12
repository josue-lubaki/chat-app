package ca.josue_lubaki.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.common.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
 * date : 2023-11-11
 * version : 1.0.0
 */

class ChatViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth : FirebaseAuth,
    private val firebaseDatabase : FirebaseDatabase
) : ViewModel() {

    private val _state = MutableStateFlow<ChatState>(ChatState.Idle)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    fun onEvent(event: ChatEvent) {
        when (event) {
            //==== if you need execute actions ====//
            is ChatEvent.OnLoadData -> event.reduce()
            is ChatEvent.OnSendMessage -> event.reduce()
        }
    }

    private fun ChatEvent.OnSendMessage.reduce() {
        _state.value = ChatState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference().child("Chat")

                val hashMap: HashMap<String, String> = HashMap()
                hashMap["senderId"] = firebaseUser?.uid!!
                hashMap["receiverId"] = receiverId
                hashMap["message"] = message

                databaseReference.push().setValue(hashMap)

            }
        } catch (e: Exception) {
            // handle errors
            _state.value = ChatState.Error(e)
        }
    }

    private fun ChatEvent.OnLoadData.reduce() {
        _state.value = ChatState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference("users").child(firebaseUser?.uid!!)

                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        _state.value = ChatState.Success(me = user)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = ChatState.Error(error.toException())
                    }
                })

            }
        } catch (e: Exception) {
            // handle errors
            _state.value = ChatState.Error(e)
        }
    }
}