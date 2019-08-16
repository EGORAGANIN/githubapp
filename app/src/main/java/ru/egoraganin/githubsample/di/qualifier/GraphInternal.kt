package ru.egoraganin.githubsample.di.qualifier

import javax.inject.Qualifier

/**
 * Should never be used outside dagger module classes
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class GraphInternal