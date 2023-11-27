package ca.josue_lubaki.common.domain.usecases.read_current_user_id

import ca.josue_lubaki.common.domain.repository.DataStoreOperations

class ReadCurrentUserIdUseCase(
    private val repository: DataStoreOperations
) {
    operator fun invoke() : String {
        return try {
            repository.readCurrentUserID()
        } catch (e: Exception) {
            Exception(e).message.toString()
        }
    }
}