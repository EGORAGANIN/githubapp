package ru.egoraganin.githubsample.ui.repos

import androidx.paging.PagedList
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.egoraganin.githubsample.entity.repo.RepoModel

@StateStrategyType(AddToEndSingleStrategy::class)
interface IReposView: MvpView {
    fun submitList(list: PagedList<RepoModel>)
    fun updateNetworkState(state: NetworkStateViewModel)
    fun updateRefreshState(state: NetworkStateViewModel)
}