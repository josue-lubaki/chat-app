package ca.josue_lubaki.common.di

import ca.josue_lubaki.common.BuildConfig
import ca.josue_lubaki.common.data.api.NotificationApi
import ca.josue_lubaki.common.network.HttpClientFactory
import ca.josue_lubaki.common.utils.Constants.NETWORK_TIME_OUT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

val firebaseModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseDatabase> { FirebaseDatabase.getInstance() }
    single<FirebaseStorage> { FirebaseStorage.getInstance() }
    single<FirebaseMessaging> { FirebaseMessaging.getInstance() }
}

val utilityModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
}

val networkModule = module {
    single<HttpClientFactory> { HttpClientFactory() }
    single<OkHttpClient.Builder> { provideOkHttpClientBuilder(httpClientFactory = get()) }
    single<OkHttpClient> { provideOkHttpClient(clientBuilder = get()) }
    single<Retrofit> { provideRetrofit(okHttpClient = get()) }
    single<NotificationApi> { provideNotificationApi(retrofit = get()) }
}

internal fun provideOkHttpClientBuilder(
    httpClientFactory: HttpClientFactory
): OkHttpClient.Builder {
    return httpClientFactory.create()
}

internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gson = GsonBuilder().setLenient().create()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.FIREBASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

internal fun provideOkHttpClient(
    clientBuilder : OkHttpClient.Builder
): OkHttpClient {

    clientBuilder
        .connectTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIME_OUT, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
    }

    return clientBuilder.build()
}

internal fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
    return retrofit.create(NotificationApi::class.java)
}


val commonModules = listOf(firebaseModule, utilityModule, networkModule)