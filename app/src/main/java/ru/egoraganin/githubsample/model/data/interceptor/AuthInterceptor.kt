package ru.egoraganin.githubsample.model.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.egoraganin.githubsample.entity.login.UserIdentity
import java.io.IOException

class AuthInterceptor(private val userIdentity: UserIdentity) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val builder = originalRequest.newBuilder().apply {
            header(Headers.AUTORIZATION, "token ${userIdentity.authToken}")
        }

        val modifiedRequest = builder.build()

        return chain.proceed(modifiedRequest)
    }
}

object Headers {
    const val AUTORIZATION = "Authorization"
}