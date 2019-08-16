package ru.egoraganin.githubsample.di.module

import dagger.Module
import dagger.Provides
import ru.egoraganin.githubsample.di.qualifier.NavigationQualifiers
import ru.egoraganin.githubsample.di.scope.BootScope
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Named

@Module
class AppNavigationModule {

    private val appCicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @BootScope
    internal fun provideRouter(): Router {
        return appCicerone.router
    }

    @Provides
    @BootScope
    @Named(NavigationQualifiers.APP_NAVIGATION)
    internal fun provideNavigationHolder(): NavigatorHolder {
        return appCicerone.navigatorHolder
    }
}