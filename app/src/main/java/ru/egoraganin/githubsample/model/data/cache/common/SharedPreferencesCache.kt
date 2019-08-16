package ru.egoraganin.githubsample.model.data.cache.common

import android.content.SharedPreferences

class SharedPreferencesCache<in Key, Value>(
    private val sharedPreferences: SharedPreferences,
    private val transformer: IPreferenceTransformer<Key, Value>
) : ICache<Key, Value> {

    override fun get(key: Key): Value? {
        val keyAsString = transformer.transformKey(key)
        return transformer.getValue(sharedPreferences, keyAsString)
    }

    override fun set(key: Key, value: Value) {
        val keyAsString = transformer.transformKey(key)
        val editor = sharedPreferences.edit()
        transformer.setValue(editor, keyAsString, value)
        editor.apply()
    }

    override fun remove(key: Key) {
        val keyAsString = transformer.transformKey(key)
        sharedPreferences.edit()
            .remove(keyAsString)
            .apply()
    }

    override fun removeAll() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}