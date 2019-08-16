package ru.egoraganin.githubsample.model.system.customtabs

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

interface ICustomTabManager {
    fun bindCustomTabService(hostContext: Context)
    fun unbindCustomTabService(hostContext: Context)
    fun createTabBuilder(): CustomTabsIntent.Builder
    fun customTabIsSupported(customTabsIntent: CustomTabsIntent, url: Uri): Boolean
}