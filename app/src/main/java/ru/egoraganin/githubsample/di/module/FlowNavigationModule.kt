package ru.egoraganin.githubsample.di.module

import dagger.Module
import dagger.Provides
import ru.egoraganin.githubsample.di.qualifier.GraphInternal
import ru.egoraganin.githubsample.di.qualifier.NavigationQualifiers
import ru.egoraganin.githubsample.di.scope.FlowScope
import ru.egoraganin.githubsample.model.system.navigation.FlowRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Named

@Module
class FlowNavigationModule {

    @Provides
    @FlowScope
    @GraphInternal
    internal fun provideCicerone(router: Router): Cicerone<FlowRouter> {
        return Cicerone.create(FlowRouter(router))
    }

    @Provides
    @FlowScope
    internal fun provideFlowRouter(@GraphInternal cicerone: Cicerone<FlowRouter>): FlowRouter {
        return cicerone.router
    }

    @Provides
    @FlowScope
    @Named(NavigationQualifiers.FLOW_NAVIGATION)
    internal fun provideAppNavigationHolder(@GraphInternal cicerone: Cicerone<FlowRouter>): NavigatorHolder {
        return cicerone.navigatorHolder
    }
}