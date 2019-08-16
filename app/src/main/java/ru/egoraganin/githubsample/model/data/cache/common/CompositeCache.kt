package ru.egoraganin.githubsample.model.data.cache.common

import ru.egoraganin.githubsample.extention.withLock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class CompositeCache<in Key, Value>(
    private val fastCache: ICache<Key, Value>,
    private val slowCache: ICache<Key, Value>
) : ICache<Key, Value> {

    private val fastCacheLockMap = ConcurrentHashMap<Key, ReentrantReadWriteLock>()
    private val slowCacheLockMap = ConcurrentHashMap<Key, ReentrantReadWriteLock>()

    private val emptyKeysForSlowCache = ConcurrentHashMap<Key, Any>()

    private val invalidationReadWriteLock = ReentrantReadWriteLock()

    override fun get(key: Key): Value? {

        return invalidationReadWriteLock.read {

            val fastCacheValue = getFastCacheLock(key).read {
                if (emptyKeysForSlowCache.contains(key)) null else fastCache[key]
            }

            if (fastCacheValue == null) {
                getFastCacheLock(key).write {

                    val fastCacheValue2 = getFastCacheLock(key).read {
                        if (emptyKeysForSlowCache.contains(key)) null else fastCache[key]
                    }

                    if (fastCacheValue2 == null) {
                        val slowCacheValue = getSlowCacheLock(key).read {
                            slowCache[key]
                        }

                        if (slowCacheValue == null) {
                            emptyKeysForSlowCache[key] =
                                EMPTY_VALUE
                        } else {
                            fastCache[key] = slowCacheValue
                        }

                        slowCacheValue
                    } else {
                        fastCacheValue2
                    }
                }
            } else {
                fastCacheValue
            }
        }
    }

    override fun set(key: Key, value: Value) {
        withLock(
            invalidationReadWriteLock.readLock(),
            getFastCacheLock(key).writeLock(),
            getSlowCacheLock(key).writeLock()
        ) {
            fastCache[key] = value
            slowCache[key] = value
            emptyKeysForSlowCache.remove(key)
        }
    }

    override fun remove(key: Key) {
        withLock(
            invalidationReadWriteLock.readLock(),
            getFastCacheLock(key).writeLock(),
            getSlowCacheLock(key).writeLock()
        ) {
            fastCache.remove(key)
            slowCache.remove(key)
            emptyKeysForSlowCache[key] =
                EMPTY_VALUE
        }

    }

    override fun removeAll() {
        invalidationReadWriteLock.write {
            fastCache.removeAll()
            slowCache.removeAll()
            emptyKeysForSlowCache.clear()
        }
    }

    private fun getFastCacheLock(key: Key) = fastCacheLockMap.getOrPut(key) { ReentrantReadWriteLock() }
    private fun getSlowCacheLock(key: Key) = slowCacheLockMap.getOrPut(key) { ReentrantReadWriteLock() }

    companion object {

        private val EMPTY_VALUE = Any()
    }
}