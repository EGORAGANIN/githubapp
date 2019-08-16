package ru.egoraganin.githubsample.ui.common

import androidx.annotation.MainThread
import androidx.paging.PagedList
import timber.log.Timber

class PagingBoundaryCallback<T>(
    private val onZeroItemsLoaded: (() -> Unit)? = null,
    private val onItemFrontLoaded: ((T) -> Unit)? = null,
    private val onItemEndLoaded: ((T) -> Unit)? = null
) : PagedList.BoundaryCallback<T>() {

    @MainThread
    override fun onZeroItemsLoaded() {
        onZeroItemsLoaded?.invoke()
    }

    @MainThread
    override fun onItemAtFrontLoaded(itemAtFront: T) {
        Timber.e("Начало запрос")
        onItemFrontLoaded?.invoke(itemAtFront)
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: T) {
        Timber.e("Конец запрос")
        onItemEndLoaded?.invoke(itemAtEnd)
    }
}