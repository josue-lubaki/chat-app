package ca.josue_lubaki.common.data.api

import ca.josue_lubaki.common.BuildConfig
import ca.josue_lubaki.common.domain.model.PushNotification
import ca.josue_lubaki.common.utils.Constants.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * created by Josue Lubaki
 * date : 2023-11-14
 * version : 1.0.0
 */

interface NotificationApi {

    @Headers("Authorization: Bearer ${BuildConfig.FIREBASE_API_KEY}", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification : PushNotification
    ) : Response<ResponseBody>

}