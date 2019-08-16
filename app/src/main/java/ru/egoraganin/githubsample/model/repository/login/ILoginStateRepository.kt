package ru.egoraganin.githubsample.model.repository.login

import ru.egoraganin.githubsample.entity.login.AccessTokenModel
import ru.egoraganin.githubsample.entity.login.UserIdentity
import ru.egoraganin.githubsample.model.data.Result

interface ILoginStateRepository {
    suspend fun login(
        code: String,
        clientId: String,
        clientSecret: String,
        state: String,
        redirectUrl: String
    ): Result<AccessTokenModel, Exception>

    fun getCurrentUserIdentity(): UserIdentity?

    fun logout()
}