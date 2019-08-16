package ru.egoraganin.githubsample.model.system.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.egoraganin.githubsample.BuildConfig
import ru.egoraganin.githubsample.entity.login.UserIdentity
import ru.egoraganin.githubsample.model.data.interceptor.AuthInterceptor

object OkHttpClientFactory {

    fun createClient(userIdentity: UserIdentity?): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        if (userIdentity != null) {
            okHttpClientBuilder.addInterceptor(AuthInterceptor(userIdentity))
        }

        return okHttpClientBuilder.build()
    }
}