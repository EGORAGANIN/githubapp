package ru.egoraganin.githubsample

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.egoraganin.githubsample.di.AppGraphManager
import timber.log.Timber

class GitHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initThreeTean()
        initBootstrap()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initBootstrap() {
        AppGraphManager.initInstance(this)
        AppGraphManager.instance().initBootstrapGraph()
    }

    private fun initThreeTean() {
        AndroidThreeTen.init(this)
    }
}