package ru.egoraganin.githubsample.model.data.cache.login

import ru.egoraganin.githubsample.entity.login.AccessTokenModel

interface IAccessTokenStorage {
    fun getAccessToken(): AccessTokenModel?
    fun setAccessToken(token: AccessTokenModel)
    fun removeAccessToken()
}