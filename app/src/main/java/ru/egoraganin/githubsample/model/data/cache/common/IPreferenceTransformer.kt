package ru.egoraganin.githubsample.model.data.cache.common

import android.content.SharedPreferences
import ru.egoraganin.githubsample.model.data.IDataMapper

interface IPreferenceTransformer<in Key, Value> {
    fun getValue(sharedPreferences: SharedPreferences, key: String): Value
    fun setValue(editor: SharedPreferences.Editor, key: String, value: Value)
    fun transformKey(key: Key): String
}

class ModelPreferenceTransformer<Model>(
    private val mapper: IDataMapper,
    private val clazz: Class<Model>
): IPreferenceTransformer<String, Model> {

    override fun getValue(sharedPreferences: SharedPreferences, key: String): Model {
        val json = sharedPreferences.getString(transformKey(key), null)
        return mapper.fromJson(json, clazz)
    }

    override fun setValue(editor: SharedPreferences.Editor, key: String, value: Model) {
        val json = mapper.toJson(value, clazz)
        editor.putString(transformKey(key), json)
    }

    override fun transformKey(key: String) = key
}