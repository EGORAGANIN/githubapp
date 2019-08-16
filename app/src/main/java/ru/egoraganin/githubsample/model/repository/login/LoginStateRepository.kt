package ru.egoraganin.githubsample.model.repository.login

import kotlinx.coroutines.withContext
import ru.egoraganin.githubsample.entity.login.AccessTokenModel
import ru.egoraganin.githubsample.entity.login.UserIdentity
import ru.egoraganin.githubsample.extention.getServerException
import ru.egoraganin.githubsample.model.data.Result
import ru.egoraganin.githubsample.model.data.cache.login.IAccessTokenStorage
import ru.egoraganin.githubsample.model.data.server.ILoginRestService
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import timber.log.Timber

class LoginStateRepository(
    private val dispatcherProvider: IDispatcherProvider,
    private val restService: ILoginRestService,
    private val tokenStorage: IAccessTokenStorage
) : ILoginStateRepository {

    override suspend fun login(
        code: String,
        clientId: String,
        clientSecret: String,
        state: String,
        redirectUrl: String
    ): Result<AccessTokenModel, Exception> = withContext(dispatcherProvider.io) {

        try {
            val response = restService.requestAccessToken(code, clientId, clientSecret, state, redirectUrl)
            val token = response.body()

            return@withContext if (response.isSuccessful && token != null) {
                tokenStorage.setAccessToken(token)
                Result.Success(token)
            } else {
                val exception = response.getServerException() ?: Exception("Get access token failed")
                throw exception
            }
        } catch (e: Exception) {
            Timber.e(e, "Get access token failed")
            Result.Failure(e)
        }
    }

    override fun logout() {
        tokenStorage.removeAccessToken()
    }

    override fun getCurrentUserIdentity(): UserIdentity? {
        return tokenStorage.getAccessToken()?.let { UserIdentity(it.accessToken) }
    }
}