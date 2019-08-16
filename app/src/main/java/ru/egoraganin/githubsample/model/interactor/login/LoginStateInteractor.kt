package ru.egoraganin.githubsample.model.interactor.login

import android.net.Uri
import kotlinx.coroutines.withContext
import ru.egoraganin.githubsample.di.IAppGraphManager
import ru.egoraganin.githubsample.entity.login.AccessTokenModel
import ru.egoraganin.githubsample.entity.login.UserIdentity
import ru.egoraganin.githubsample.model.data.Result
import ru.egoraganin.githubsample.model.data.cache.common.GithubDb
import ru.egoraganin.githubsample.model.repository.login.ILoginStateRepository
import ru.egoraganin.githubsample.model.system.provider.IConfigProvider
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import java.util.*

class LoginStateInteractor(
    private val configProvider: IConfigProvider,
    private val dispatcherProvider: IDispatcherProvider,
    private val loginStateRepository: ILoginStateRepository,
    private val db: GithubDb,
    private val graphManager: IAppGraphManager
) : ILoginStateInteractor {

    private val randomState = UUID.randomUUID().toString()

    override val oauthUri: Uri = Uri.Builder().scheme("https")
        .authority("github.com")
        .appendPath("login")
        .appendPath("oauth")
        .appendPath("authorize")
        .appendQueryParameter("client_id", configProvider.oauthClientId)
        .appendQueryParameter("redirect_uri", configProvider.oauthRedirectUrl)
        .appendQueryParameter("scope", "repo")
        .appendQueryParameter("state", randomState)
        .build()

    override suspend fun login(redirectUri: Uri): Result<AccessTokenModel, Exception> = withContext(dispatcherProvider.io) {
        val state = redirectUri.getQueryParameter("state")
        val code = redirectUri.getQueryParameter("code")

        if (state != randomState || code == null) {
            return@withContext Result.Failure(Exception("Redirect url is incorrect"))
        }

        val result = loginStateRepository.login(
            code,
            configProvider.oauthClientId,
            configProvider.oauthClientSecret,
            randomState,
            configProvider.oauthRedirectUrl
        )

        when (result) {
            is Result.Success -> {
                switchAccount(result.payload)
            }
        }

        return@withContext result
    }

    override fun isRedirectUrl(redirectUri: Uri): Boolean {
        return redirectUri.toString().startsWith(configProvider.oauthRedirectUrl)
    }

    override suspend fun logout() = withContext(dispatcherProvider.io) {
        loginStateRepository.logout()
        db.clearAllTables()
        graphManager.deinitUserGraph()
    }

    private fun switchAccount(token: AccessTokenModel) {
        graphManager.initUserGraph(UserIdentity(token.accessToken))
    }
}
