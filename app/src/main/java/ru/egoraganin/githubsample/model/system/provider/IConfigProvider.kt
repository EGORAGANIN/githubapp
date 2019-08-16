package ru.egoraganin.githubsample.model.system.provider

import android.net.Uri
import androidx.core.net.toUri
import ru.egoraganin.githubsample.BuildConfig

interface IConfigProvider {
    val endpointUrl: Uri
    val oauthClientId: String
    val oauthClientSecret: String
    val oauthRedirectUrl: String
}

class ConfigProvider: IConfigProvider {
    override val endpointUrl = BuildConfig.ENDPOINT_URL.toUri()
    override val oauthClientId = BuildConfig.OAUTH_CLIENT_ID
    override val oauthClientSecret = BuildConfig.OAUTH_CLIENT_SECRET
    override val oauthRedirectUrl = BuildConfig.OAUTH_REDIDECT_URL
}