package ca.josue_lubaki.common.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

val firebaseModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseDatabase> { FirebaseDatabase.getInstance() }
    single<FirebaseStorage> { FirebaseStorage.getInstance() }
}

val utilityModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
}

val commonModules = listOf(firebaseModule, utilityModule)