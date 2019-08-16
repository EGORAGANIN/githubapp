package ru.egoraganin.githubsample.model.data.cache.login

import android.content.SharedPreferences
import ru.egoraganin.githubsample.entity.login.AccessTokenModel
import ru.egoraganin.githubsample.model.data.IDataMapper
import ru.egoraganin.githubsample.model.data.cache.common.*

class AccessTokenStorage private constructor(
    private val cache: ICache<String, AccessTokenModel>
) : IAccessTokenStorage {

    override fun getAccessToken(): AccessTokenModel? {
        return this.cache[KEY_USER_TOKEN]
    }

    override fun setAccessToken(token: AccessTokenModel) {
        this.cache[KEY_USER_TOKEN] = token
    }

    override fun removeAccessToken() {
        this.cache.remove(KEY_USER_TOKEN)
    }

    companion object {

        fun newInstance(sharedPreferences: SharedPreferences, mapper: IDataMapper): IAccessTokenStorage {
            val memoryCache = MemoryCache<String, AccessTokenModel>()

            val preferencesTransformer = ModelPreferenceTransformer(
                mapper,
                AccessTokenModel::class.java
            )
            val diskCache = SharedPreferencesCache(
                sharedPreferences,
                preferencesTransformer
            )

            val composedCache = CompositeCache(memoryCache, diskCache)

            return AccessTokenStorage(composedCache)
        }


        private const val KEY_USER_TOKEN = "key_user_token"
    }
}