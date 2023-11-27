package ca.josue_lubaki.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.common.domain.model.Message
import ca.josue_lubaki.common.domain.model.User
import ca.josue_lubaki.common.domain.usecases.SharedPreferencesUseCase
import ca.josue_lubaki.common.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
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
    private val firebaseDatabase : FirebaseDatabase,
    private val firebaseMessaging : FirebaseMessaging,
    private val sharedPreferencesUseCase: SharedPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ContactState>(ContactState.Idle)
    val state: StateFlow<ContactState> = _state.asStateFlow()

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.OnLoadData -> getAllContacts()
        }
    }

    private fun getAllContacts() {
        _state.value = ContactState.Loading

        try {
            viewModelScope.launch(dispatcher) {
                val currentUser: FirebaseUser? = firebaseAuth.currentUser
                val usersReference = firebaseDatabase.getReference(Constants.REF_USERS)
                val messagesReference = firebaseDatabase.getReference(Constants.REF_MESSAGES)

                // subscribe to topic
                firebaseMessaging.subscribeToTopic("${Constants.TOPICS}/${currentUser?.uid}")
                sharedPreferencesUseCase.saveCurrentUserIdUseCase(currentUser?.uid!!)

                val lastMessages: HashMap<String, String> = hashMapOf()

                usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        val users: MutableList<User> = ArrayList()
                        var me: User? = null

                        for (userSnapshot in usersSnapshot.children) {
                            val user: User? = userSnapshot.getValue(User::class.java)

                            if (user!!.userId != currentUser.uid) {
                                users.add(user)
                            }
                            else {
                                Firebase.messaging.subscribeToTopic("${Constants.TOPICS}/${user.userId}")
                                me = user
                            }
                        }

                        messagesReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(messagesSnapshot: DataSnapshot) {
                                for (messageSnapshot in messagesSnapshot.children) {
                                    val message = messageSnapshot.getValue(Message::class.java) ?: continue

                                    for (userSnapshot in usersSnapshot.children) {
                                        val user: User? = userSnapshot.getValue(User::class.java)
                                        if(message.senderId == currentUser.uid && message.receiverId == user?.userId ||
                                            message.senderId == user?.userId && message.receiverId == currentUser.uid){
                                            if (message.senderId == currentUser.uid) {
                                                lastMessages[message.receiverId] = message.message
                                            } else {
                                                lastMessages[message.senderId] = message.message
                                            }
                                        }
                                    }
                                }

                                _state.value = ContactState.Success(
                                    data = users,
                                    me = me,
                                    lastMessages = lastMessages
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {
                                _state.value = ContactState.Error(error.toException())
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = ContactState.Error(error.toException())
                    }
                })
            }
        } catch (e: Exception) {
            _state.value = ContactState.Error(e)
        }
    }
}