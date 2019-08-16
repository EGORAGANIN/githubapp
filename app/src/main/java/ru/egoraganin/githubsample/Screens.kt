package ru.egoraganin.githubsample

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import ru.egoraganin.githubsample.ui.auth.AuthFlowFragment
import ru.egoraganin.githubsample.ui.auth.ChoiceLoginMethodFragment
import ru.egoraganin.githubsample.ui.repos.ReposFragment
import ru.egoraganin.githubsample.ui.repos.UserFlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object AuthFlow : SupportAppScreen() {
        override fun getFragment() = AuthFlowFragment.newInstance()
    }

    object UserFlow : SupportAppScreen() {
        override fun getFragment() = UserFlowFragment.newInstance()
    }

    object UserReposList: SupportAppScreen() {
        override fun getFragment() = ReposFragment.newInstance()
    }

    object ChoiceLoginMethod : SupportAppScreen() {
        override fun getFragment() = ChoiceLoginMethodFragment.newInstance()
    }

    data class CustomTabsScreen(
        val customTabsIntent: CustomTabsIntent
    ): SupportAppScreen() {
        override fun getActivityIntent(context: Context?) = customTabsIntent.intent
    }

    data class ExternalBrowser(
        val url: Uri
    ): SupportAppScreen() {
        override fun getActivityIntent(context: Context?) = Intent(Intent.ACTION_VIEW, url)
    }
}