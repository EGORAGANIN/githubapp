package ru.egoraganin.githubsample.model.data.exception

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.extention.getMessageStringRes
import ru.egoraganin.githubsample.model.data.ServerException
import ru.egoraganin.githubsample.model.interactor.login.ILoginStateInteractor
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.terrakok.cicerone.Router
import timber.log.Timber

class ExceptionHandler(
    private val dispatcherProvider: IDispatcherProvider,
    private val router: Router,
    private val loginStateInteractor: ILoginStateInteractor
) : IExceptionHandler {

    override fun handleException(exception: Throwable, messageCallback: (messageStringRes: Int) -> Unit) {
        Timber.e(exception)

        if (exception is ServerException && exception.statusCode == 401) {
            logout()
        }
        else {
            messageCallback.invoke(exception.getMessageStringRes())
        }
    }

    private fun logout() = CoroutineScope(dispatcherProvider.main).launch {
        loginStateInteractor.logout()
        router.newRootScreen(Screens.AuthFlow)
    }
}
