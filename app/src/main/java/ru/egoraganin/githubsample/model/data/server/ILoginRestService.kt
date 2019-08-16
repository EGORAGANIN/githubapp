package ru.egoraganin.githubsample.model.data.server

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.egoraganin.githubsample.entity.login.AccessTokenModel

interface ILoginRestService {

    @FormUrlEncoded
    @POST("access_token")
    @Headers("Accept: application/json")
    suspend fun requestAccessToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("state") state: String,
        @Field("redirect_uri") redirectUrl: String
    ): Response<AccessTokenModel>
}