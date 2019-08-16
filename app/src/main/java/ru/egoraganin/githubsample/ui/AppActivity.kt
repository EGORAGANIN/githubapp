package ru.egoraganin.githubsample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.androidannotations.annotations.EActivity
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.di.InjectionHolder
import ru.egoraganin.githubsample.di.qualifier.NavigationQualifiers
import ru.egoraganin.githubsample.presentation.AppPresenter
import ru.egoraganin.githubsample.presentation.IAppView
import ru.egoraganin.githubsample.ui.common.BaseActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject
import javax.inject.Named

@SuppressLint("Registered")
@EActivity(R.layout.layout_container)
open class AppActivity : BaseActivity(), IAppView {

    @InjectPresenter
    internal lateinit var presenter: AppPresenter

    @Inject @field:Named(NavigationQualifiers.APP_NAVIGATION)
    internal lateinit var navigatorHolder: NavigatorHolder

    @Inject
    internal lateinit var daggerPresenter: AppPresenter

    @ProvidePresenter
    fun provideAppPresenter(): AppPresenter {
        return daggerPresenter
    }

    private val navigator: Navigator = SupportAppNavigator(this, supportFragmentManager, R.id.layout_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        InjectionHolder.bootstrapComponent.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            presenter.launchRootScreen()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressedClicked()
    }
}