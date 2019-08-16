package ru.egoraganin.githubsample.model.data.cache.common

import java.util.concurrent.ConcurrentHashMap

class MemoryCache<in Key, Value> : ICache<Key, Value> {

    private val cache = ConcurrentHashMap<Key, Value>()

    override fun get(key: Key) = cache[key]

    override fun set(key: Key, value: Value) {
        cache[key] = value
    }

    override fun remove(key: Key) {
        cache.remove(key)
    }

    override fun removeAll() {
        cache.clear()
    }
}