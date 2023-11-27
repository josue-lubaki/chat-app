package ca.josue_lubaki.common.domain.usecases.save_current_user_id

import ca.josue_lubaki.common.domain.repository.DataStoreOperations

class SaveCurrentUserIdUseCase(
    private val repository: DataStoreOperations
) {
    suspend operator fun invoke(idUser: String) {
        return try {
            repository.saveCurrentUserID(idUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}