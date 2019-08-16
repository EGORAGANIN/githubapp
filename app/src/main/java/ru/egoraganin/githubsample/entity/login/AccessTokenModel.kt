package ru.egoraganin.githubsample.entity.login

import com.google.gson.annotations.SerializedName

data class AccessTokenModel(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)