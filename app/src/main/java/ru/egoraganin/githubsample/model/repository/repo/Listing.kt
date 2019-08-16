package ru.egoraganin.githubsample.model.repository.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkStatus>,
    val refreshState: LiveData<NetworkStatus>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)