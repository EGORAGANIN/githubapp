package ru.egoraganin.githubsample.presentation

import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.model.interactor.boot.IBootstrapInteractor
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.egoraganin.githubsample.presentation.common.ScopePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AppPresenter @Inject constructor(
    dispatcherProvider: IDispatcherProvider,
    private val bootstrapInteractor: IBootstrapInteractor,
    private val router: Router
): ScopePresenter<IAppView>(dispatcherProvider) {

    fun launchRootScreen() {
        val isSignIn = bootstrapInteractor.isSignIn()

        val screen = if (isSignIn) Screens.UserFlow else Screens.AuthFlow
        router.newRootScreen(screen)
    }

    fun onBackPressedClicked() = router.exit()
}