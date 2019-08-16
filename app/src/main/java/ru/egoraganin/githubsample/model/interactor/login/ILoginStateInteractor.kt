package ru.egoraganin.githubsample.model.interactor.login

import android.net.Uri
import ru.egoraganin.githubsample.entity.login.AccessTokenModel
import ru.egoraganin.githubsample.model.data.Result

interface ILoginStateInteractor {
    val oauthUri: Uri

    suspend fun login(redirectUri: Uri): Result<AccessTokenModel, Exception>
    fun isRedirectUrl(redirectUri: Uri): Boolean
    suspend fun logout()
}