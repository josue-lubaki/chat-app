package ca.josue_lubaki.common.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ca.josue_lubaki.common.domain.repository.DataStoreOperations
import ca.josue_lubaki.common.utils.Constants.FIREBASE_TOKEN_KEY
import ca.josue_lubaki.common.utils.Constants.ID_USER
import ca.josue_lubaki.common.utils.Constants.PREFERENCES_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationsImpl(context: Context) : DataStoreOperations {

    private val dataStore : DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val firebaseTokenKey : Preferences.Key<String> = stringPreferencesKey(name = FIREBASE_TOKEN_KEY)
        val idUserCurrentKey : Preferences.Key<String> = stringPreferencesKey(name = ID_USER)
    }

    override suspend fun saveFirebaseToken(token: String) {
        dataStore.edit { preferencesArray ->
            preferencesArray[PreferencesKeys.firebaseTokenKey] = token
        }
    }

    override fun readFirebaseToken(): String {
        return runBlocking {
            dataStore.data.catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferencesArray ->
                preferencesArray[PreferencesKeys.firebaseTokenKey] ?: ""
            }.first()
        }
    }

    override suspend fun saveCurrentUserID(idUser: String) {
        dataStore.edit { preferencesArray ->
            preferencesArray[PreferencesKeys.idUserCurrentKey] = idUser
        }
    }

    override fun readCurrentUserID(): String {
        return runBlocking(Dispatchers.IO) {
            dataStore.data.catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferencesArray ->
                preferencesArray[PreferencesKeys.idUserCurrentKey] ?: ""
            }.first()
        }
    }

}