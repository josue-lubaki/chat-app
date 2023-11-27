package ca.josue_lubaki.common.domain.usecases

import ca.josue_lubaki.common.domain.usecases.read_current_user_id.ReadCurrentUserIdUseCase
import ca.josue_lubaki.common.domain.usecases.read_firebase_token.ReadFirebaseTokenUseCase
import ca.josue_lubaki.common.domain.usecases.save_current_user_id.SaveCurrentUserIdUseCase
import ca.josue_lubaki.common.domain.usecases.save_firebase_token.SaveFirebaseTokenUseCase

/**
 * created by Josue Lubaki
 * date : 2023-11-27
 * version : 1.0.0
 */

data class SharedPreferencesUseCase(
    val readCurrentUserIdUseCase: ReadCurrentUserIdUseCase,
    val saveCurrentUserIdUseCase: SaveCurrentUserIdUseCase,
    val readFirebaseTokenUseCase: ReadFirebaseTokenUseCase,
    val saveFirebaseTokenUseCase: SaveFirebaseTokenUseCase
)