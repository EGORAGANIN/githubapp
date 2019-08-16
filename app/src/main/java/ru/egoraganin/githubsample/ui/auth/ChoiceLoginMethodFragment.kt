package ru.egoraganin.githubsample.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.di.InjectionHolder
import ru.egoraganin.githubsample.extention.getPrimaryColor
import ru.egoraganin.githubsample.extention.isVisibleInvisible
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.presentation.auth.ChoiceLoginMethodPresenter
import ru.egoraganin.githubsample.ui.common.BaseFragment
import javax.inject.Inject

@EFragment(R.layout.fragment_choice_login_method)
open class ChoiceLoginMethodFragment : BaseFragment(), IChoiceLoginMethodView {

    @InjectPresenter
    internal lateinit var presenter: ChoiceLoginMethodPresenter

    @Inject
    override lateinit var dispatcherProvider: IDispatcherProvider

    @Inject
    internal lateinit var daggerPresenter: ChoiceLoginMethodPresenter

    @ViewById(R.id.sign_in_container)
    internal lateinit var signInContainer: LinearLayout

    @ViewById(R.id.progress_container)
    internal lateinit var progressContainer: LinearLayout

    @ViewById(R.id.progress_loading)
    internal lateinit var progressView: ProgressBar

    @ProvidePresenter
    fun provideChoiceLoginMethodPresenter(): ChoiceLoginMethodPresenter {
        return daggerPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionHolder.anonymousFlowNavigationComponent?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed(): Boolean {
        super.onBackPressed()
        presenter.onBackPressed()
        return true
    }

    override fun onNewIntent(intent: Intent): Boolean {
        return presenter.onHandleIntent(intent)
    }

    override fun showProgress(isVisible: Boolean) {
        signInContainer.isVisibleInvisible(!isVisible)
        progressContainer.isVisibleInvisible(isVisible)
    }

    override fun showMessage(messageResId: Int) {
        getDialogManager().showMessageDialog(messageResId, R.string.button_ok)
    }

    @Click(R.id.button_sign_in_with_browser)
    fun onSignInWithBrowserClick() {
        context?.let {
            val toolbarColor = getPrimaryColor(it)
            if (toolbarColor != null) {
                presenter.onSignInWithBrowserClicked(toolbarColor)
            }
        }
    }

    @Click(R.id.button_sign_in_with_access_token)
    fun onSignInWithAccessTokenClick() {
        showMessage(R.string.label_feature_in_development)
    }

    companion object {

        fun newInstance(): ChoiceLoginMethodFragment {
            return ChoiceLoginMethodFragment_()
        }
    }
}