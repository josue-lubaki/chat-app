package ca.josue_lubaki.common.domain.usecases.read_firebase_token

import ca.josue_lubaki.common.domain.repository.DataStoreOperations

class ReadFirebaseTokenUseCase(
    private val repository: DataStoreOperations
) {
    operator fun invoke() : String {
        return try {
            repository.readFirebaseToken()
        } catch (e: Exception) {
            Exception(e).message.toString()
        }
    }
}