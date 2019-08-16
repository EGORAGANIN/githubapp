package ru.egoraganin.githubsample.model.repository.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import androidx.paging.createStatusLiveData
import kotlinx.coroutines.*
import ru.egoraganin.githubsample.entity.repo.*
import ru.egoraganin.githubsample.extention.getServerException
import ru.egoraganin.githubsample.model.data.Result
import ru.egoraganin.githubsample.model.data.cache.repo.IUserReposDao
import ru.egoraganin.githubsample.model.data.server.IUserRestService
import ru.egoraganin.githubsample.model.data.server.SequenceParameters
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.model.system.provider.IExecutorProvider
import ru.egoraganin.githubsample.ui.common.PagingBoundaryCallback
import timber.log.Timber

class UserReposRepository(
    private val dispatcherProvider: IDispatcherProvider,
    executorProvider: IExecutorProvider,
    private val userRestService: IUserRestService,
    private val userReposDao: IUserReposDao
) : IUserReposRepository, CoroutineScope {

    override val coroutineContext = SupervisorJob() + dispatcherProvider.io

    private val helper = PagingRequestHelper(executorProvider.io)
    private val networkState = helper.createStatusLiveData()

    private var lastPageLoaded: Boolean = false

    override fun getUserRepos(pageSize: Int): Listing<RepoModel> {
        if (pageSize < 1) throw IllegalArgumentException("pageSize must be > 0")

        val boundaryCallback = PagingBoundaryCallback<RepoModel>(
            onZeroItemsLoaded = {
                helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
                    launch { requestUserReposAndInsert(1, pageSize, it) }
                }
            },
            onItemEndLoaded = { _ ->
                launch {
                    val reposCount = userReposDao.itemsCount()

                    if (lastPageLoaded) {
                        return@launch
                    }

                    val page = (reposCount / pageSize) + 1

                    helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                        runBlocking { requestUserReposAndInsert(page, pageSize, it) }
                    }
                }
            }
        )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()

        val livePagedList = LivePagedListBuilder(userReposDao.getReposDataSourceFactory(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = switchMap(refreshTrigger) {
            refreshUserRepos(1, pageSize)
        }

        return Listing(
            pagedList = livePagedList,
            networkState = networkState,
            retry = { helper.retryAllFailed() },
            refresh = { refreshTrigger.value = Unit },
            refreshState = refreshState
        )
    }

    fun refreshUserRepos(page: Int, pageSize: Int): LiveData<NetworkStatus> {
        val networkState = MutableLiveData<NetworkStatus>()
        networkState.value = NetworkStatus.Running
        launch {
            val result = requestUserRepos(page, pageSize)

            when (result) {
                is Result.Success -> {
                    result.payload?.let {
                        userReposDao.deleteAllAndInsert(it)
                        lastPageLoaded = it.size < pageSize
                    }

                    networkState.postValue(NetworkStatus.Success)
                }
                is Result.Failure -> {
                    networkState.postValue(NetworkStatus.Failed(result.error))
                }
            }
        }
        return networkState
    }

    private suspend fun requestUserReposAndInsert(
        page: Int,
        pageSize: Int,
        callback: PagingRequestHelper.Request.Callback
    ) = withContext(dispatcherProvider.io) {
        val result = requestUserRepos(page, pageSize)

        when (result) {
            is Result.Failure -> callback.recordFailure(result.error)
            is Result.Success -> {
                val items = result.payload
                if (items != null) {
                    userReposDao.insert(items)
                    if (items.size < pageSize) {
                        lastPageLoaded = true
                    }
                }
                callback.recordSuccess()
            }
        }
    }

    private suspend fun requestUserRepos(page: Int, networkPageSize: Int): Result<List<RepoModel>?, Exception> =
        withContext(dispatcherProvider.io) {
            try {
                val response = userRestService.getRepos(
                    Visibility.ALL,
                    SequenceParameters(
                        arrayOf(
                            Affiliation.OWNER,
                            Affiliation.COLLABORATOR,
                            Affiliation.ORGANIZATION_MEMBER
                        )
                    ),
                    Sort.FULL_NAME,
                    Direction.ASC,
                    page,
                    networkPageSize
                )

                if (response.isSuccessful) {
                    Result.Success(response.body())
                } else {
                    val exception = response.getServerException() ?: Exception("Get repos list failed")
                    throw exception
                }
            } catch (e: Exception) {
                Timber.e(e, "Get repos list failed")
                Result.Failure(e)
            }
        }
}
