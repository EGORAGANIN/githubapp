package ru.egoraganin.githubsample.presentation.repos

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.entity.repo.RepoModel
import ru.egoraganin.githubsample.model.data.exception.IExceptionHandler
import ru.egoraganin.githubsample.model.interactor.login.ILoginStateInteractor
import ru.egoraganin.githubsample.model.repository.repo.IUserReposRepository
import ru.egoraganin.githubsample.model.repository.repo.Listing
import ru.egoraganin.githubsample.model.repository.repo.NetworkStatus
import ru.egoraganin.githubsample.model.system.navigation.FlowRouter
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.presentation.common.ScopePresenter
import ru.egoraganin.githubsample.ui.repos.IReposView
import ru.egoraganin.githubsample.ui.repos.NetworkStateViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class ReposPresenter @Inject constructor(
    dispatcherProvider: IDispatcherProvider,
    private val exceptionHandler: IExceptionHandler,
    private val router: Router,
    private val flowRouter: FlowRouter,
    private val loginStateInteractor: ILoginStateInteractor,
    private val reposRepository: IUserReposRepository
) : ScopePresenter<IReposView>(dispatcherProvider) {

    private lateinit var reposListing: Listing<RepoModel>

    private val pagedListObserver: Observer<PagedList<RepoModel>> = Observer { t ->
        viewState.submitList(t)
    }

    private val networkStateObserver: Observer<NetworkStatus> = Observer { t ->
        processingNetworkStatus(t) { viewState.updateNetworkState(it) }
    }

    private val refreshStateObserver: Observer<NetworkStatus> = Observer { t ->
        processingNetworkStatus(t) { viewState.updateRefreshState(it) }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        reposListing = reposRepository.getUserRepos(50)
        reposListing.pagedList.observeForever(pagedListObserver)
        reposListing.networkState.observeForever(networkStateObserver)
        reposListing.refreshState.observeForever(refreshStateObserver)
        refresh()
    }


    fun refresh() {
        reposListing.refresh()
    }

    fun retry() {
        reposListing.retry()
    }

    fun logout() = CoroutineScope(coroutineContext + Job()).launch {
        loginStateInteractor.logout()
        router.newRootScreen(Screens.AuthFlow)
    }

    override fun onDestroy() {
        super.onDestroy()
        reposListing.pagedList.removeObserver(pagedListObserver)
        reposListing.networkState.removeObserver(networkStateObserver)
        reposListing.refreshState.removeObserver(refreshStateObserver)
    }

    private fun processingNetworkStatus(networkStatus: NetworkStatus, callback: (NetworkStateViewModel) -> Unit) {
        when (networkStatus) {
            is NetworkStatus.Running -> callback(NetworkStateViewModel.LOADING)
            is NetworkStatus.Success -> callback(NetworkStateViewModel.LOADED)
            is NetworkStatus.Failed -> {
                exceptionHandler.handleException(networkStatus.exception) {
                    callback(NetworkStateViewModel.error(it))
                }
            }
        }
    }
}
