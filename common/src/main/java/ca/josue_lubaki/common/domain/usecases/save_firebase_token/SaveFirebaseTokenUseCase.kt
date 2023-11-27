package ca.josue_lubaki.common.domain.usecases.save_firebase_token

import ca.josue_lubaki.common.domain.repository.DataStoreOperations

class SaveFirebaseTokenUseCase(
    private val repository: DataStoreOperations
) {
    suspend operator fun invoke(token: String) = repository.saveFirebaseToken(token)
}