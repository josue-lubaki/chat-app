package ca.josue_lubaki.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.common.domain.model.Message
import ca.josue_lubaki.common.domain.model.User
import ca.josue_lubaki.common.utils.Constants.REF_MESSAGES
import ca.josue_lubaki.common.utils.Constants.REF_USERS
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
                val databaseReference = firebaseDatabase.getReference().child(REF_MESSAGES)

                val hashMap: HashMap<String, String> = HashMap()
                val messageId = databaseReference.push().key
                hashMap["messageId"] = messageId!!
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
                val databaseReferenceUsers = firebaseDatabase.getReference(REF_USERS).child(firebaseUser?.uid!!)
                val databaseReferenceMessage = firebaseDatabase.getReference(REF_MESSAGES)
                var user : User? = null

                databaseReferenceUsers.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(User::class.java)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = ChatState.Error(error.toException())
                    }
                })

                databaseReferenceMessage.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages: MutableList<Message> = ArrayList()
                        for (dataSnapshot in snapshot.children) {
                            val message = dataSnapshot.getValue(Message::class.java)
                            message?.let {
                                if (message.senderId == firebaseUser.uid && message.receiverId == userId ||
                                    message.senderId == userId && message.receiverId == firebaseUser.uid
                                ) {
                                    messages.add(it)
                                }
                            }
                        }
                        _state.value = ChatState.Success(
                            data = messages,
                            me = user
                        )
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