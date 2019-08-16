package ru.egoraganin.githubsample.model.data.cache.common

interface ICache<in Key, Value> {
    operator fun get(key: Key): Value?
    operator fun set(key: Key, value: Value)
    fun remove(key: Key)
    fun removeAll()
}
