package ru.egoraganin.githubsample.model.system.customtabs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import ru.egoraganin.githubsample.model.system.customtabs.shared.CustomTabsHelper
import ru.egoraganin.githubsample.model.system.customtabs.shared.ServiceConnection
import ru.egoraganin.githubsample.model.system.customtabs.shared.ServiceConnectionCallback
import java.lang.ref.WeakReference

class CustomTabManager(private val context: Context) : ICustomTabManager, ServiceConnectionCallback {

    private var hostContextRef: WeakReference<Context>? = null
    private var connection: CustomTabsServiceConnection? = null
    private var client: CustomTabsClient? = null
    private var session: CustomTabsSession? = null

    override fun customTabIsSupported(customTabsIntent: CustomTabsIntent, url: Uri): Boolean {
        var result = false

        val packageName = CustomTabsHelper.getPackageNameToUse(context)

        packageName?.let {
            result = true

            CustomTabsHelper.addKeepAliveExtra(
                context,
                customTabsIntent.intent
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                customTabsIntent.intent
                    .putExtra(
                        Intent.EXTRA_REFERRER,
                        Uri.parse(Intent.URI_ANDROID_APP_SCHEME.toString() + "//" + context.packageName)
                    )
            }

            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.intent.setData(url)
        }

        return result
    }

    override fun bindCustomTabService(hostContext: Context) {
        val currentHostContext = hostContextRef?.get()
        if (currentHostContext != null && currentHostContext != hostContext) {
            unbindCustomTabService(currentHostContext)
        }

        if (client != null) return

        hostContextRef = WeakReference(hostContext)

        val packageName = CustomTabsHelper.getPackageNameToUse(
            hostContext
        ) ?: return

        connection = ServiceConnection(this)
        CustomTabsClient.bindCustomTabsService(hostContext, packageName, connection)
    }

    override fun unbindCustomTabService(hostContext: Context) {
        if (connection == null) return

        val currentHostContext = hostContextRef?.get()
        if (currentHostContext == hostContext) {
            hostContextRef?.clear()
        }

        hostContext.unbindService(connection)
        client = null
        session = null
        connection = null
    }

    override fun createTabBuilder(): CustomTabsIntent.Builder {
        return CustomTabsIntent.Builder(createSession())
    }

    override fun onServiceConnected(client: CustomTabsClient) {
        this.client = client
        this.client?.warmup(0L)
    }

    override fun onServiceDisconnected() {
        this.client = null
        this.session = null
    }

    private fun createSession(): CustomTabsSession? {
        if (client == null) {
            session = null
        } else if (session == null) {
            session = client?.newSession(null)
        }
        return session
    }
}