package ca.josue_lubaki.common.domain.repository

interface DataStoreOperations {
    suspend fun saveFirebaseToken(token: String)
    fun readFirebaseToken() : String
    suspend fun saveCurrentUserID(idUser: String)
    fun readCurrentUserID() : String
}