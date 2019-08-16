package ru.egoraganin.githubsample.model.interactor.boot

import ru.egoraganin.githubsample.di.IAppGraphManager
import ru.egoraganin.githubsample.model.repository.login.ILoginStateRepository

class BootstrapInteractor(
    private val loginStateRepository: ILoginStateRepository,
    private val appGraphManager: IAppGraphManager
) : IBootstrapInteractor {

    override fun isSignIn(): Boolean {
        val userIdentity = loginStateRepository.getCurrentUserIdentity()

        userIdentity?.let { appGraphManager.initUserGraph(it) }

        return userIdentity != null
    }
}