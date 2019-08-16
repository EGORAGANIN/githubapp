package ru.egoraganin.githubsample.presentation.auth

import android.content.Intent
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.model.data.Result
import ru.egoraganin.githubsample.model.data.exception.IExceptionHandler
import ru.egoraganin.githubsample.model.interactor.login.ILoginStateInteractor
import ru.egoraganin.githubsample.model.system.customtabs.ICustomTabManager
import ru.egoraganin.githubsample.model.system.navigation.FlowRouter
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.presentation.common.ScopePresenter
import ru.egoraganin.githubsample.ui.auth.IChoiceLoginMethodView
import javax.inject.Inject

@InjectViewState
class ChoiceLoginMethodPresenter @Inject constructor(
    dispatcherProvider: IDispatcherProvider,
    private val customTabManager: ICustomTabManager,
    private val router: FlowRouter,
    private val exceptionHandler: IExceptionHandler,
    private val loginStateInteractor: ILoginStateInteractor
) : ScopePresenter<IChoiceLoginMethodView>(dispatcherProvider) {

    fun onSignInWithBrowserClicked(toolbarColor: Int) {
        val customTabIntent = customTabManager.createTabBuilder()
            .setToolbarColor(toolbarColor)
            .setShowTitle(true)
            .build()

        val oauthUrl = loginStateInteractor.oauthUri

        val customTabIsSupported = customTabManager.customTabIsSupported(customTabIntent, oauthUrl)

        val screen = if (customTabIsSupported)
            Screens.CustomTabsScreen(customTabIntent)
        else
            Screens.ExternalBrowser(oauthUrl)

        router.navigateTo(screen)
    }

    fun onHandleIntent(intent: Intent): Boolean {
        var result = false

        val redirectUri = intent.data ?: return result

        if (loginStateInteractor.isRedirectUrl(redirectUri)) {
            result = true
            launch {
                viewState.showProgress(true)

                val loginResult = loginStateInteractor.login(redirectUri)

                viewState.showProgress(false)

                when (loginResult) {
                    is Result.Success -> {
                        router.newRootFlow(Screens.UserFlow)
                    }
                    is Result.Failure -> {
                        exceptionHandler.handleException(loginResult.error) { viewState.showMessage(it) }
                    }
                }
            }
        }

        return result
    }

    fun onBackPressed() = router.exit()
}