package ru.egoraganin.githubsample.ui.repos

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.di.InjectionHolder
import ru.egoraganin.githubsample.entity.repo.RepoModel
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.presentation.repos.ReposPresenter
import ru.egoraganin.githubsample.ui.common.BaseFragment
import javax.inject.Inject

@EFragment(R.layout.fragment_repos)
open class ReposFragment: BaseFragment(), IReposView {

    @InjectPresenter
    internal lateinit var presenter: ReposPresenter

    @Inject
    internal lateinit var daggerPresenter: ReposPresenter

    @Inject
    override lateinit var dispatcherProvider: IDispatcherProvider

    @ProvidePresenter
    fun provideReposPresenter(): ReposPresenter {
        return daggerPresenter
    }

    @ViewById(R.id.layout_toolbar)
    internal lateinit var toolbar: Toolbar

    @ViewById(R.id.repos_recycler)
    internal lateinit var reposRecycler: RecyclerView

    @ViewById(R.id.swipe_refresh)
    internal lateinit var swipeRefreshView: SwipeRefreshLayout

    private val adapter = ReposAdapter(ReposDiffCallback()) { presenter.retry() }

    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionHolder.userFlowNavigationComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onInitInterface() {
        super.onInitInterface()

        toolbar.apply {
            inflateMenu(R.menu.sign_out_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sign_out_action -> { presenter.logout() }
                }
                true
            }
        }

        swipeRefreshView.setOnRefreshListener {
            presenter.refresh()
        }

        reposRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        reposRecycler.adapter = adapter
    }

    override fun submitList(list: PagedList<RepoModel>) {
        adapter.submitList(list)
    }

    override fun updateNetworkState(state: NetworkStateViewModel) {
        adapter.setNetworkState(state)
    }

    override fun updateRefreshState(state: NetworkStateViewModel) {
        swipeRefreshView.isRefreshing = state == NetworkStateViewModel.LOADING
    }

    companion object {
        fun newInstance(): ReposFragment {
            return ReposFragment_()
        }
    }
}