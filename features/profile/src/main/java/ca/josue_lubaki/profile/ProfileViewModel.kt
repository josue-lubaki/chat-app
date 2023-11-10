package ca.josue_lubaki.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.josue_lubaki.common.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

class ProfileViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnLoadData -> event.reduce()
            is ProfileEvent.OnUploadImage -> event.reduce()
        }
    }

    private fun ProfileEvent.OnUploadImage.reduce() {
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference("users").child(firebaseUser?.uid!!)
                val firebaseStorageReference = firebaseStorage.reference.child("users").child("image/${firebaseUser.uid}")

                firebaseStorageReference.putFile(uri)
                    .addOnSuccessListener {
                        firebaseStorageReference.downloadUrl.addOnSuccessListener {
                            databaseReference.child("profileImage").setValue(it.toString())
                        }.addOnFailureListener {
                            _state.value = ProfileState.Error(it)
                        }
                    }
            }
        } catch (e: Exception) {
            _state.value = ProfileState.Error(e)
        }
    }

    private fun ProfileEvent.OnLoadData.reduce() {
        _state.value = ProfileState.Loading
        try {
            viewModelScope.launch(dispatcher) {
                val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
                val databaseReference = firebaseDatabase.getReference("users").child(firebaseUser?.uid!!)

                databaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        _state.value = ProfileState.Success(data = user)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _state.value = ProfileState.Error(error.toException())
                    }
                })
            }
        } catch (e: Exception) {
            _state.value = ProfileState.Error(e)
        }
    }
}