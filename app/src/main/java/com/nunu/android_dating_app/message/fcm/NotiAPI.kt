package com.nunu.android_dating_app.message.fcm

import com.nunu.android_dating_app.message.fcm.Repo.Companion.CONTENT_TYPE
import com.nunu.android_dating_app.message.fcm.Repo.Companion.SERVER_KEY
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotiAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}